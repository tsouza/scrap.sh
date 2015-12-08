package sh.scrap.scrapper;

import reactor.rx.Promise;

import java.util.Collections;
import java.util.Map;

public interface DataScrapper {

   default Promise<Map<String, Object>> scrap(Object data) {
      return scrap(Collections.emptyMap(), data);
   }

   Promise<Map<String, Object>> scrap(Map<String, Object> metadata, Object data);

}
