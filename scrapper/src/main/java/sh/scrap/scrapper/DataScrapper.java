package sh.scrap.scrapper;

import reactor.rx.Promise;

import java.util.Map;

public interface DataScrapper {

   Promise<Map<String, Object>> scrap(Map<String, Object> metadata, Object data);

}
