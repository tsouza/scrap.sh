package sh.scrap.scraplet.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import sh.scrap.scrapper.DataScrapper;
import sh.scrap.scrapper.DataScrapperBuilderFactory;
import sh.scrap.scrapper.util.ObjectUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ScrapletVerticle extends AbstractVerticle {

    private DataScrapper scrapper;
    private MessageConsumer<JsonObject> consumer;

    @Override
    public void start() throws Exception {

        String apiKey = config().getString("apiKey");
        String name = config().getString("name");
        String script = config().getString("script");

        scrapper = DataScrapperBuilderFactory
                .fromScript(script)
                .createBuilder()
                .build();

        consumer = vertx.eventBus().consumer("scraplet." + apiKey + "." + name,
                this::handleInvoke);
    }

    @Override
    public void stop() throws Exception {
        consumer.unregister();
        consumer = null;
    }

    private void handleInvoke(Message<JsonObject> invoke) {
        JsonObject metadata = invoke.body().getJsonObject("Metadata");
        Map<String, Object> mapMetadata = metadata == null ?
                Collections.emptyMap() : metadata.getMap();

        Object valueData = invoke.body().getValue("Data");
        Object data;
        if (valueData instanceof JsonObject)
            data = ((JsonObject) valueData).getMap();
        else if (valueData instanceof JsonArray)
            data = ((JsonArray) valueData).getList();
        else
            data = valueData;

        AtomicInteger processedBytes = new AtomicInteger();

        scrapper.scrap(processedBytes, mapMetadata, data)
                .onSuccess((output) -> {
                    int receivedBytes = ObjectUtils.sizeOf(data);
                    output.putAll(mapMetadata);
                    JsonObject result = new JsonObject();
                    result.put("ReceivedBytes", receivedBytes);
                    result.put("ProcessedBytes", processedBytes.get());
                    result.put("Output", output);

                    invoke.reply(result);
                })
                .onError((exception) -> {
                    invoke.fail(100, exception.getMessage());
                });
    }
}
