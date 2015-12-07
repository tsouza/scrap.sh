package sh.scrap.scrapper.functions;

import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ToIterableFunctionTest extends FunctionsTestBase {

    @Test
    public void testToIterable_singleObject() {
        String test = "test";
        testFunction(new ToIterableFunctionFactory(),
                (Iterable<String> data) ->
                        assertThat(data.iterator().next(), equalTo(test)),
                test);
    }

    @Test
    public void testToIterable_list() {
        List<String> test = Arrays.asList("test");
        testFunction(new ToIterableFunctionFactory(),
                (Iterable<String> data) ->
                        assertThat(data, equalTo(test)),
                test);
    }

    @Test
    public void testToIterable_xml() {
        NodeList test = nodeList();
        testFunction(new ToIterableFunctionFactory(),
                (Iterable<Node> data) ->
                        assertThat(data.iterator().next(), equalTo(test.item(0))),
                test);
    }

    @Test
    public void testToIterable_array() {
        String[] test = new String[] { "test" };
        testFunction(new ToIterableFunctionFactory(),
                (Iterable<String> data) ->
                        assertThat(data.iterator().next(), equalTo(test[0])),
                test);
    }

}
