package sh.scrap.scrapper;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import sh.scrap.scrapper.impl.ReactorDataScrapperBuilder;

import java.io.IOException;
import java.io.InputStream;
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

        assertThat(result.get("fieldTest"), equalTo("1"));
    }

    @Test
    public void testScrapper_json_forEach() throws InterruptedException {
        DataScrapper scrapper = new ReactorDataScrapperBuilder()
                .field("fieldTest")
                    .map("json", "$.test")
                    .forEach()
                        .map("json", "$.a")
                .build();

        Map<String, Object> result = scrapper
                .scrap(Collections.emptyMap(), "{ \"test\": [ { \"a\": 10 }, { \"a\": 20 }, { \"a\": 30 } ] }")
                .await();

        assertThat(result.get("fieldTest"), equalTo(Arrays.asList("10", "20", "30")));
    }

    @Test
    public void testScrapper_fullScript() throws InterruptedException, IOException {
        String scraplet = loadResource("/test.scraplet");
        String html = loadResource("/test.html");

        DataScrapper scrapper = DataScrapperBuilderFactory
                .fromScript(scraplet)
                .createBuilder()
                .build();

        Map<String, Object> result = scrapper
                .scrap(html)
                .await();

        assertThat(result.get("title"),
                equalTo("Porta Retrato Venus Vitrix Metal Multicolorido"));

        assertThat(result.get("sku"),
                equalTo("VE087HDU80GNF"));

        assertThat(result.get("categories"),
                equalTo(Arrays.asList("UNISSEX", "CASA", "CASA-FEMININA",
                        "DECORACAO", "OBJETOS-DECORATIVOS-DECORACAO",
                        "VENUS VICTRIX")));

        assertThat(result.get("price"),
                equalTo(114.99));

        assertThat(result.get("lastUpdate"),
                equalTo("08/12/2015"));
    }

    private String loadResource(String name) throws IOException {
        try (InputStream res = DataScrapperTest.class.getResourceAsStream(name)) {
            return IOUtils.toString(res);
        }
    }
}
