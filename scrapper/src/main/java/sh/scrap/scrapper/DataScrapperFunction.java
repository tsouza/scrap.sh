package sh.scrap.scrapper;

import org.reactivestreams.Publisher;

public interface DataScrapperFunction {
    Publisher<DataScrapperExecutionContext> process(DataScrapperExecutionContext context);
}
