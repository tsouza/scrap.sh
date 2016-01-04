package sh.scrap.scraplet.store;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import sh.scrap.scrapper.DataScrapperBuilder;
import sh.scrap.scrapper.DataScrapperBuilderFactory;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class DataScrapperCacheStore extends CacheStoreAdapter<DataScrapperCacheKey, DataScrapperBuilder>
        implements Serializable {

    private transient Table scrapletTable;

    public DataScrapperCacheStore() {
        init();
    }

    private void init() {
        AmazonDynamoDB dynamoDB = new AmazonDynamoDBClient();
        this.scrapletTable = new Table(dynamoDB, "scraplet");
   }

    @Override
    public DataScrapperBuilder load(DataScrapperCacheKey key) throws CacheLoaderException {
        try {
            Item item = scrapletTable.getItem("ApiKey", key.apiKey,
                    "Name", key.name);
            if (item == null)
                return null;

            String script = item.getString("Script");

            return DataScrapperBuilderFactory
                    .fromScript(script)
                    .createBuilder();
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    @Override
    public void write(Cache.Entry<? extends DataScrapperCacheKey, ? extends DataScrapperBuilder> entry) throws CacheWriterException {
        throw new CacheWriterException("NOT_SUPPORTED");
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        throw new CacheWriterException("NOT_SUPPORTED");
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {

        stream.defaultReadObject();

        init();

    }
}
