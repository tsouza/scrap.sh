package sh.scrap.scraplet;

import com.amazonaws.services.lambda.runtime.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sh.scrap.scrapper.DataScrapper;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InvokerService extends AbstractScrapletService {

    private final Logger log = LoggerFactory.getLogger(InvokerService.class);

    @SuppressWarnings("unchecked")
    public Map<String, Object> handle(Map<String, Object> input, Context context) throws Throwable {
        String apiKey = (String) input.get("apiKey");
        String name = (String) input.get("name");

        DataScrapper scrapper = createScrapper(apiKey, name);

        Map<String, Object> metadata = (Map<String, Object>) input.get("metadata");
        if (metadata == null)
            metadata = Collections.emptyMap();

        Object data = input.get("data");

        return scrapper.scrap(metadata, data)
                .await(context.getRemainingTimeInMillis(),
                        TimeUnit.MILLISECONDS);
    }

}
