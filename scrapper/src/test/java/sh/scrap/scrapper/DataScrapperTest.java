package sh.scrap.scrapper;

import org.junit.Test;
import sh.scrap.scrapper.impl.ReactorDataScrapperBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class DataScrapperTest {

    @Test
    public void testScrapper_json_simple() throws InterruptedException {
        DataScrapper scrapper = new ReactorDataScrapperBuilder()
                .field("fieldTest")
                    .map("json", "$.test")
                .build();

        Map<String, Object> result = scrapper
                .scrap(Collections.emptyMap(), "{ \"test\": 1 }")
                .await();

        assertThat(result.get("fieldTest"), equalTo(1));
    }

    @Test
    public void testScrapper_json_forEach() throws InterruptedException{
        DataScrapper scrapper = new ReactorDataScrapperBuilder()
                .field("fieldTest")
                    .map("json", "$.test")
                    .forEach()
                        .map("json", "$.a")
                .build();

        Map<String, Object> result = scrapper
                .scrap(Collections.emptyMap(), "{ \"test\": [ { \"a\": 10 }, { \"a\": 20 }, { \"a\": 30 } ] }")
                .await();

        assertThat(result.get("fieldTest"), equalTo(Arrays.asList(10, 20, 30)));
    }
}
