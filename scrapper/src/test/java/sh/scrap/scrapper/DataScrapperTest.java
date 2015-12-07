package sh.scrap.scrapper;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class DataScrapperTest {

    @Test
    public void testScrapper_simpleJson() throws InterruptedException{
        DataScrapper scrapper = new DataScrapperBuilder()
                .map("fieldTest", "json", "$.test")
                .build();

        Map<String, Object> result = scrapper
                .scrap(Collections.emptyMap(), "{ \"test\": 1 }")
                .await();

        assertThat(result.get("fieldTest"), equalTo(1));
    }

    @Test
    public void testScrapper_simpleJson_forEach() throws InterruptedException{
        DataScrapper scrapper = new DataScrapperBuilder()
                .map("fieldTest", "json", "$.test")
                .forEach("fieldTest")
                .map("fieldTest", "json", "$.a")
                .build();

        Map<String, Object> result = scrapper
                .scrap(Collections.emptyMap(), "{ \"test\": [ { \"a\": 10 }, { \"a\": 20 }, { \"a\": 30 } ] }")
                .await();

        System.out.println(result);
        //assertThat(result.get("fieldTest"), equalTo(1));
    }
}
