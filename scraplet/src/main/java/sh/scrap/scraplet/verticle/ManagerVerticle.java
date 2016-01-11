package sh.scrap.scraplet.verticle;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.google.common.collect.ImmutableMap;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sh.scrap.scrapper.DataScrapperBuilderFactory;

import java.util.Collection;
import java.util.Map;

@Component
public class ManagerVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(ManagerVerticle.class);

    @Autowired AmazonDynamoDBAsync dynamodb;

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.get("/health").handler(this::handleHealthCheck);

        router.post("/scraplet/:apiKey/:name/:status/:version")
                .handler(this::handleStatusChange);

        router.delete("/scraplet/:deploymentID")
                .handler(this::handleDelete);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);

        TcpEventBusBridge.create(vertx, new BridgeOptions()
                .addInboundPermitted(new PermittedOptions()
                        .setAddressRegex("scraplet.*")))
                .listen(7000);

    }

    private void handleStatusChange(RoutingContext context) {
        String status = context.request().params().get("status");
        String script = context.getBodyAsString();
        switch (status) {
            case "PENDING_VALIDATION":
                try {
                    DataScrapperBuilderFactory
                            .fromScript(script)
                            .createBuilder();

                    updateStatus(context, "PENDING_DEPLOYMENT");

                } catch (ParseCancellationException e) {
                    updateStatus(context, "INVALID");
                }
                break;
            case "PENDING_DEPLOYMENT":
                String apiKey = context.request().params().get("apiKey");
                String name = context.request().params().get("name");

                JsonObject config = new JsonObject()
                        .put("apiKey", apiKey)
                        .put("name", name)
                        .put("script", script);

                vertx.deployVerticle(ScrapletVerticle.class.getName(),
                            new DeploymentOptions().setConfig(config)
                                    .setHa(true).setInstances(2),
                        (deployment) -> {
                            if (deployment.failed())
                                updateStatus(context, "INVALID");
                            else
                                updateStatus(context, "DEPLOYED", deployment.result());
                        });
                break;

            default:
                context.response().setStatusCode(200).end();
        }
    }

    private void handleDelete(RoutingContext context) {
        String deploymentId = context.request().params().get("deploymentID");

        vertx.undeploy(deploymentId, (result) ->
                context.response().setStatusCode(200).end());
    }

    private void updateStatus(RoutingContext context, String toStatus) {
        updateStatus(context, toStatus, "$NOT_DEPLOYED");
    }

    private void updateStatus(RoutingContext context, String toStatus, String deploymentId) {
        String apiKey = context.request().params().get("apiKey");
        String name = context.request().params().get("name");
        String status = context.request().params().get("status");
        String version = context.request().params().get("version");
        UpdateItemRequest request = createRequest(new UpdateItemSpec()
                .withPrimaryKey("ApiKey", apiKey, "Name", name)
                .withUpdateExpression(
                        "SET #Status = :to_status, DeploymentID = :deployment_id " +
                         "ADD Version :increment")
                .withConditionExpression(
                        "#Status = :from_status AND Version = :version")
                .withNameMap(ImmutableMap
                        .of("#Status", "Status"))
                .withValueMap(ImmutableMap.of(
                        ":deployment_id", deploymentId,
                        ":from_status", status,
                        ":to_status", toStatus,
                        ":version", Integer.parseInt(version),
                        ":increment", 1)));

        dynamodb.updateItemAsync(request, new AsyncHandler<UpdateItemRequest, UpdateItemResult>() {
            @Override
            public void onError(Exception exception) {
                log.error("Could not update status", exception);

                context.response().setStatusCode(200).end();
            }
            @Override
            public void onSuccess(UpdateItemRequest request, UpdateItemResult updateItemResult) {
                context.response().setStatusCode(200).end();
            }
        });
    }

    private void handleHealthCheck(RoutingContext context) {
        context.response().setStatusCode(200).end();
    }

    private UpdateItemRequest createRequest(UpdateItemSpec spec) {
        final UpdateItemRequest request = spec.getRequest();
        request.setKey(InternalUtils.toAttributeValueMap(spec.getKeyComponents()));
        request.setTableName("scraplet");
        final Collection<Expected> expected = spec.getExpected();
        final Map<String, ExpectedAttributeValue> expectedMap =
                InternalUtils.toExpectedAttributeValueMap(expected);
        request.setExpected(expectedMap);
        request.setAttributeUpdates(
                InternalUtils.toAttributeValueUpdate(spec.getAttributeUpdate()));
        request.setExpressionAttributeNames(spec.getNameMap());
        request.setExpressionAttributeValues(
                InternalUtils.fromSimpleMap(spec.getValueMap()));
        return request;
    }
}
