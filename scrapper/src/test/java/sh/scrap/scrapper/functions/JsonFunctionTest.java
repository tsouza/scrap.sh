package sh.scrap.scrapper.functions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class JsonFunctionTest extends FunctionsTestBase {

    @Test
    public void testJson_simple() {
        String json = "{ \"test\": 1 }";
        testFunction(new JsonFunctionFactory(),
                (Integer data) ->
                        assertThat(data, equalTo(1)),
                json, "$.test");

    }

    @Test
    public void testJson_unknownPath() {
        String json = "{ \"test\": 1 }";
        testFunction(new JsonFunctionFactory(),
                (Integer data) ->
                        assertThat(data, equalTo(null)),
                json, "$.test.test");

    }
}
