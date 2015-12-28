package sh.scrap.scraplet.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.ignite.Ignite;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sh.scrap.scrapper.DataScrapperBuilder;
import sh.scrap.scrapper.DataScrapperBuilderFactory;

import java.util.Map;

@RestController
public class ScrapletController {

    @Autowired Ignite ignite;
    @Autowired AmazonDynamoDB dynamoDB;

    @RequestMapping(path="/scraplet/on-change", method=RequestMethod.POST,
        consumes="application/json", produces="application/json")
    public void onChange(@RequestBody OnChangeEvent event) throws Throwable {

        Table scrapletTable = new Table(dynamoDB, "scraplet");

        if (event.newImage == null)
            ignite.services().cancel(event.oldImage.apiKey + "/" + event.oldImage.name);

        else if ("PENDING_VALIDATION".equals(event.newImage.status))
            try {
                DataScrapperBuilderFactory
                        .fromScript(event.newImage.script)
                        .createBuilder();

                updateStatus(event, "PENDING_VALIDATION", "PENDING_DEPLOYMENT");

            } catch (ParseCancellationException e) {
                updateStatus(event, "PENDING_VALIDATION", "INVALID");
            }

        else if ("PENDING_DEPLOYMENT".equals(event.newImage.status))
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

                    updateStatus(event, "PENDING_DEPLOYMENT", "DEPLOYED");

                    tx.commit();

                } catch (Exception e) {
                    tx.rollback();
                    throw e;
                }
            } catch (ParseCancellationException e) {
                updateStatus(event, "PENDING_DEPLOYMENT", "INVALID");
            }

    }

    @RequestMapping(path="/scraplet/invoke", method=RequestMethod.POST,
        consumes="application/json", produces="application/json")
    public ScrapletInvoker.Result invoke(@RequestBody InvokeRequest request)
            throws Throwable {

        ScrapletInvoker invoker = ignite.services()
                    .serviceProxy(request.apiKey + "/" + request.name,
                            ScrapletInvoker.class, false);

        return invoker.invoke(request.invoke.metadata, request.invoke.data);
    }

    private void updateStatus(OnChangeEvent event, String fromStatus, String toStatus) {
        Table scrapletTable = new Table(dynamoDB, "scraplet");
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
                        ":from_status", fromStatus,
                        ":to_status", toStatus,
                        ":increment", "1")));
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

    public static class ValidationResponse {

        @JsonProperty("Result")
        private final String result;

        @JsonProperty("Detail")
        private final Map<String, Object> detail;

        public ValidationResponse() {
            result = "VALID";
            detail = null;
        }

        public ValidationResponse(RecognitionException e) {
            result = "INVALID";

            Token offendingToken = e.getOffendingToken();
            detail = ImmutableMap.<String, Object>builder()
                    .put("Line", offendingToken.getLine())
                    .put("Column", offendingToken.getCharPositionInLine())
                    .put("Text", offendingToken.getText())
                    .build();
        }
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
    }

}
