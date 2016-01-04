package sh.scrap.scraplet.service;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.scheduler.SchedulerFuture;
import org.apache.ignite.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.scrap.scraplet.store.DataScrapperCacheKey;
import sh.scrap.scrapper.DataScrapper;
import sh.scrap.scrapper.DataScrapperBuilder;
import sh.scrap.scrapper.DataScrapperBuilderFactory;
import sh.scrap.scrapper.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ScrapletController {

    private static final Logger log = LoggerFactory.getLogger(ScrapletController.class);

    @Autowired IgniteCache<DataScrapperCacheKey, DataScrapperBuilder> builderCache;
    @Autowired Ignite ignite;
    @Autowired Table scrapletTable;

    private final String callbackQueueId = UUID.randomUUID().toString();
    private final Map<UUID, SettableListenableFuture<InvokeResponse>> callbacks =
            new ConcurrentHashMap<>();

    @RequestMapping(path="/scraplet/on-delete", method=POST,
            consumes="application/json", produces="application/json")
    public void onDelete(@RequestBody OnChangeEvent event) throws Throwable {
        cancelService(event);
    }

    @RequestMapping(path="/scraplet/on-change/{toStatus}", method=POST,
        consumes="application/json", produces="application/json")
    public void onChange(
            @PathVariable("toStatus") String toStatus,
            @RequestBody OnChangeEvent event) throws Throwable {

        switch (toStatus) {
            case "PENDING_VALIDATION":
                try {
                    DataScrapperBuilderFactory
                            .fromScript(event.newImage.script)
                            .createBuilder();

                    updateStatus(event, "PENDING_DEPLOYMENT");

                } catch (ParseCancellationException e) {
                    updateStatus(event, "INVALID");
                }
                break;

            case "PENDING_DEPLOYMENT":
                try {
                    DataScrapperBuilder builder = DataScrapperBuilderFactory
                            .fromScript(event.newImage.script)
                            .createBuilder();

                    Transaction tx = ignite.transactions().txStart();

                    try {
                        builderCache.put(new DataScrapperCacheKey(
                                event.newImage.apiKey,
                                event.newImage.name), builder);

                        updateStatus(event, "DEPLOYED");

                        tx.commit();
                    } catch (Exception e) {
                        tx.rollback();
                        throw e;
                    }
                } catch (ParseCancellationException e) {
                    updateStatus(event, "INVALID");
                }
                break;
        }
    }

    @Async
    @RequestMapping(path="/scraplet/invoke", method=POST,
        consumes="application/json", produces="application/json")
    public Future<InvokeResponse> invoke(@RequestBody InvokeRequest request)
            throws Throwable {

        int receivedBytes = ObjectUtils.sizeOf(request.invoke.data);

        DataScrapperCacheKey key = new DataScrapperCacheKey(
                request.apiKey, request.name);

        UUID callbackId = UUID.randomUUID();
        SettableListenableFuture<InvokeResponse> response = new SettableListenableFuture<>();
        callbacks.put(callbackId, response);

        ignite.compute().withAsync().affinityRun("scraplet", key, () -> {
            DataScrapperBuilder builder = builderCache.get(key);
            if (builder == null) {
                ignite.message().send(callbackQueueId, new InvokeCallback(callbackId, "NOT_FOUND"));
                return;
            }
            DataScrapper scrapper = builder.build();
            AtomicInteger processedBytes = new AtomicInteger();

            scrapper.scrap(processedBytes, request.invoke.metadata, request.invoke.data)
                    .onSuccess(success -> {
                        success.putAll(request.invoke.metadata);
                        ignite.message().send(callbackQueueId,
                                new InvokeCallback(callbackId,
                                        new InvokeResponse(receivedBytes,
                                                processedBytes.get(), success)));
                    })
                    .onError(e -> ignite.message().send(callbackQueueId,
                            new InvokeCallback(callbackId, e)));
        });

        SchedulerFuture<?> timeout = ignite.scheduler().scheduleLocal(() -> {
            SettableListenableFuture<InvokeResponse> callback = callbacks.remove(callbackId);
            if (callback != null)
                callback.setException(new IllegalStateException("TIMEOUT"));
        }, "{60, 1} * * * * *");

        response.addCallback(success -> timeout.cancel(), failure -> timeout.cancel());

        return response;
    }


    private void updateStatus(OnChangeEvent event, String toStatus) {
        log.info("updating to status {} : {}", toStatus, event.newImage);
        try {
            scrapletTable.updateItem(new UpdateItemSpec()
                    .withPrimaryKey("ApiKey", event.newImage.apiKey,
                            "Name", event.newImage.name)
                    .withUpdateExpression(
                            "SET #Status = :to_status " +
                                    "ADD Version :increment")
                    .withConditionExpression(
                            "#Status = :from_status AND Version = :version")
                    .withNameMap(ImmutableMap
                            .of("#Status", "Status"))
                    .withValueMap(ImmutableMap.of(
                            ":from_status", event.newImage.status,
                            ":to_status", toStatus,
                            ":version", event.newImage.version,
                            ":increment", 1)));
        } catch (ConditionalCheckFailedException ignored) {}
    }


    private void cancelService(OnChangeEvent event) {
        try {
            ignite.services().cancel(event.oldImage.apiKey + "/" + event.oldImage.name);
        } catch (IgniteException ignored) {}
    }

    @PostConstruct
    public void start() {
        ignite.message().localListen(callbackQueueId, callback);
    }

    @PreDestroy
    public void stop() {
        ignite.message().stopLocalListen(callbackQueueId, callback);
    }

    private final IgniteBiPredicate<UUID, ?> callback = (UUID origin, InvokeCallback callback) -> {
        SettableListenableFuture<InvokeResponse> response = callbacks.remove(callback.id);
        if (response != null)
            callback.handle(response);
        return true;
    };

    public static class OnChangeEvent {

        @JsonProperty("OldImage")
        private Scraplet oldImage;

        @JsonProperty("NewImage")
        private Scraplet newImage;
    }

    public static class InvokeCallback implements Serializable {
        final UUID id;
        final InvokeResponse response;
        final Throwable exception;

        public InvokeCallback(UUID id, InvokeResponse response) {
            this.id = id;
            this.response = response;
            this.exception = null;
        }

        public InvokeCallback(UUID id, String message) {
            this(id, new IllegalArgumentException(message));
        }

        public InvokeCallback(UUID id, Throwable exception) {
            this.id = id;
            this.response = null;
            this.exception = exception;
        }

        public InvokeCallback(UUID id) {
            this.id = id;
            response = null;
            exception = null;
        }

        void handle(SettableListenableFuture<InvokeResponse> response) {
            if (exception != null)
                response.setException(exception);
            else
                response.set(this.response);
        }
    }

    public static class InvokeRequest implements Serializable {

        @JsonProperty("ApiKey")
        private String apiKey;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Invoke")
        private InvokeData invoke;
    }

    public static class InvokeData implements Serializable {

        @JsonProperty("Metadata")
        private Map<String, Object> metadata;

        @JsonProperty("Data")
        private Object data;
    }

    public static class Scraplet implements Serializable {

        @JsonProperty("ApiKey")
        private String apiKey;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Script")
        private String script;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("CreatedAt")
        private String createdAt;

        @JsonProperty("Version")
        private int version;

        @Override
        public String toString() {
            return "Scraplet{" +
                    "apiKey='" + apiKey + '\'' +
                    ", name='" + name + '\'' +
                    ", status='" + status + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", version=" + version +
                    '}';
        }
    }

    public static class InvokeResponse implements Serializable {

        @JsonProperty("ReceivedBytes")
        private final int receivedBytes;

        @JsonProperty("ProcessedBytes")
        private final int processedBytes;

        @JsonProperty("Output")
        private final Map<String, Object> output;

        public InvokeResponse(int receivedBytes, int processedBytes, Map<String, Object> output) {
            this.receivedBytes = receivedBytes;
            this.processedBytes = processedBytes;
            this.output = output;
        }

        public int getReceivedBytes() {
            return receivedBytes;
        }

        public int getProcessedBytes() {
            return processedBytes;
        }

        public Map<String, Object> getOutput() {
            return output;
        }
    }

}
