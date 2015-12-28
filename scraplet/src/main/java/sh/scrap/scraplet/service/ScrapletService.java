package sh.scrap.scraplet.service;

import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;
import sh.scrap.scrapper.DataScrapper;
import sh.scrap.scrapper.DataScrapperBuilder;
import sh.scrap.scrapper.util.ObjectUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ScrapletService implements Service, ScrapletInvoker {

    private final DataScrapperBuilder builder;

    private DataScrapper scrapper;

    public ScrapletService(DataScrapperBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Result invoke(Map<String, Object> metadata, Object data) throws Exception {
        if (metadata == null)
            metadata = Collections.emptyMap();

        AtomicInteger processedBytes = new AtomicInteger();

        Map<String, Object> output = scrapper
                .scrap(processedBytes, metadata, data)
                .await();

        output.putAll(metadata);

        return new Result(ObjectUtils.sizeOf(data), processedBytes.get(), output);
    }

    @Override
    public void cancel(ServiceContext ctx) {

    }

    @Override
    public void init(ServiceContext ctx) throws Exception {
        scrapper = builder.build();
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {}
}
