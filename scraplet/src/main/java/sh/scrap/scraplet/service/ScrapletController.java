package sh.scrap.scraplet.service;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.scrap.scrapper.DataScrapperBuilder;
import sh.scrap.scrapper.DataScrapperBuilderFactory;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ScrapletController {

    private static final Logger log = LoggerFactory.getLogger(ScrapletController.class);

    @Autowired Ignite ignite;
    @Autowired Table scrapletTable;


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
                        ServiceConfiguration scrapletService = new ServiceConfiguration();
                        scrapletService.setName(event.newImage.apiKey + "/" + event.newImage.name);
                        scrapletService.setMaxPerNodeCount(1);
                        scrapletService.setTotalCount(2);
                        scrapletService.setService(new ScrapletService(builder));

                        ignite.services().deploy(scrapletService);

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

    @RequestMapping(path="/scraplet/invoke", method=POST,
        consumes="application/json", produces="application/json")
    public ScrapletInvoker.Result invoke(@RequestBody InvokeRequest request)
            throws Throwable {

        ScrapletInvoker invoker = ignite.services()
                    .serviceProxy(request.apiKey + "/" + request.name,
                            ScrapletInvoker.class, false);

        return invoker.invoke(request.invoke.metadata, request.invoke.data);
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

    public static class OnChangeEvent {

        @JsonProperty("OldImage")
        private Scraplet oldImage;

        @JsonProperty("NewImage")
        private Scraplet newImage;
    }

    public static class InvokeRequest {

        @JsonProperty("ApiKey")
        private String apiKey;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Invoke")
        private InvokeData invoke;
    }

    public static class InvokeData {

        @JsonProperty("Metadata")
        private Map<String, Object> metadata;

        @JsonProperty("Data")
        private Object data;
    }

    public static class Scraplet {

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

}
