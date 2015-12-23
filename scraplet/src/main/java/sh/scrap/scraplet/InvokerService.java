package sh.scrap.scraplet;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import sh.scrap.scrapper.DataScrapper;
import sh.scrap.scrapper.DataScrapperBuilderFactory;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InvokerService {

    private final Cache<String, DataScrapper> cache = CacheBuilder
            .<String, DataScrapper>newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build();

    @SuppressWarnings("unchecked")
    public Map<String, Object> handle(Map<String, Object> input, Context context)
            throws Exception {
        String key = (String) input.get("Key");
        String script = (String) input.get("Script");
        Map<String, Object> metadata = (Map<String, Object>) input.get("Metadata");
        Object data = input.get("Data");

        if (metadata == null)
            metadata = Collections.emptyMap();

        DataScrapper scrapper = cache.get(key, () ->
                DataScrapperBuilderFactory.fromScript(script)
                    .createBuilder().build());

        return scrapper.scrap(metadata, data)
                .await(context.getRemainingTimeInMillis(),
                        TimeUnit.MILLISECONDS);
    }

}
