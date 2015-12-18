package sh.scrapper.scraplet;

import com.amazonaws.services.lambda.runtime.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sh.scrap.scrapper.DataScrapper;
import sh.scrap.scrapper.DataScrapperBuilderFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ScrapletService {

    private final Logger log = LoggerFactory.getLogger(ScrapletService.class);

    private final DataScrapper scrapper;

    public ScrapletService() throws Exception  {
        try (InputStream script = loadScript()) {
            scrapper = DataScrapperBuilderFactory
                .fromScript(new InputStreamReader(script))
                .createBuilder()
                .build();
       }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> handle(Map<String, Object> input, Context context) throws Throwable {
        Map<String, Object> metadata = (Map<String, Object>) input.get("metadata");
        if (metadata == null)
            metadata = Collections.emptyMap();

        Object data = input.get("data");
        return scrapper.scrap(metadata, data)
                .await(context.getRemainingTimeInMillis(),
                        TimeUnit.MILLISECONDS);
    }

    private static InputStream loadScript() {
        InputStream script = ScrapletService.class.getResourceAsStream("META-INF/script");
        if (script == null)
            script = ScrapletService.class.getResourceAsStream("/META-INF/script");
        return script;
    }

}
