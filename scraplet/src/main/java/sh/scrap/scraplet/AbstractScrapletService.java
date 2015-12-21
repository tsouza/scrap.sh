package sh.scrap.scraplet;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import sh.scrap.scrapper.DataScrapper;
import sh.scrap.scrapper.DataScrapperBuilderFactory;

public abstract class AbstractScrapletService {

    private final Table scrapletTable;

    public AbstractScrapletService()  {
        AmazonDynamoDB client = new AmazonDynamoDBClient();
        DynamoDB db = new DynamoDB(client);
        scrapletTable = db.getTable("scraplet");
    }

    public DataScrapper createScrapper(String apiKey, String name) throws Exception {
        Item item = scrapletTable.getItem(new PrimaryKey("ApiKey", apiKey, "Name", name),
                "Script", null);
        String script = item.getString("Script");
        return DataScrapperBuilderFactory
                .fromScript(script)
                .createBuilder()
                .build();
    }
}
