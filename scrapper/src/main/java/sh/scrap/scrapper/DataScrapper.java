package sh.scrap.scrapper;

import reactor.rx.Promise;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface DataScrapper {

    default Promise<Map<String, Object>> scrap(Object data) {
      return scrap(null, Collections.emptyMap(), data);
   }

    default Promise<Map<String, Object>> scrap(Map<String, Object> metadata, Object data) {
        return scrap(null, Collections.emptyMap(), data);
    }

    Promise<Map<String, Object>> scrap(AtomicInteger processedBytes, Map<String, Object> metadata, Object data);

}
