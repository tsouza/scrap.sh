package sh.scrap.scrapper;

import org.reactivestreams.Publisher;

import java.util.Map;

public interface DataScrapperExecutionContext {

    String fieldName();
    Map<String, Object> metadata();
    Object data();

    DataScrapperExecutionContext withData(Object data);

    Publisher<DataScrapperExecutionContext> invoke(String functionName, Object... args);
}
