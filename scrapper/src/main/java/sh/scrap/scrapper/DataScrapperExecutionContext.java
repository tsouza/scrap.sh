package sh.scrap.scrapper;

import java.util.Map;

public interface DataScrapperExecutionContext {

    String fieldName();
    Map<String, Object> metadata();
    Object data();

    DataScrapperExecutionContext withData(Object data);

    void objectProcessed(Object data);
}
