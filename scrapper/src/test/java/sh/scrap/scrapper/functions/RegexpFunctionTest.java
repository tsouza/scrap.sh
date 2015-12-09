package sh.scrap.scrapper.functions;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class RegexpFunctionTest extends FunctionsTestBase {

    @Test
    public void testRegexp_ungrouped() {
        String text = "1 2 3";
        testFunction(new RegexpFunctionFactory(),
                data -> assertThat(data, equalTo(Arrays.asList("1", "2", "3"))),
                text, "\\d");

    }

    @Test
    public void testRegexp_grouped() {
        String text = "1 2 3 1 5 3";
        testFunction(new RegexpFunctionFactory(),
                data -> assertThat(data, equalTo(Arrays.asList("2", "5"))),
                text, "1 (\\d) 3");

    }

    @Test
    public void testRegexp_joined() {
        String text = "1 2 3 5 2 4";
        testFunction(new RegexpFunctionFactory(),
                data -> assertThat(data, equalTo(Arrays.asList("1:3", "5:4"))),
                text, "(\\d) 2 (\\d)", new HashMap<String, Object>() {{
                    put("separator", ":");
                }});

    }
}
