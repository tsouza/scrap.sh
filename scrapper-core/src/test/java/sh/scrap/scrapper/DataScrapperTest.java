/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Thiago Souza <thiago@scrap.sh>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package sh.scrap.scrapper;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import sh.scrap.scrapper.core.DataScrapperBuilderFactory;
import sh.scrap.scrapper.core.ReactorDataScrapperBuilder;

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
        String scraplet = loadResource("/samples/test.scraplet");
        String html = loadResource("/samples/test.html");

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

    private static String loadResource(String name) throws IOException {
        try (InputStream res = DataScrapperTest.class.getResourceAsStream(name)) {
            return IOUtils.toString(res);
        }
    }
}
