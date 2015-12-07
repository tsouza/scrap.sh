package sh.scrap.scrapper.functions;

import org.junit.Test;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ForEachFunctionTest extends FunctionsTestBase {

    private int forEachCounter;

    @Test
    public void testForEach_singleObject() {
        forEachCounter = 0;
        Object object = 1;
        testFunction(new ForEachFunctionFactory(),
                data -> forEachCounter++,
                object);

        assertThat(forEachCounter, equalTo(1));
    }

    @Test
    public void testForEach_list() {
        forEachCounter = 0;
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        testFunction(new ForEachFunctionFactory(),
                data -> forEachCounter++,
                list);

        assertThat(forEachCounter, equalTo(list.size()));
    }

    @Test
    public void testForEach_xml() {
        forEachCounter = 0;
        NodeList list = nodeList();
        testFunction(new ForEachFunctionFactory(),
                data -> forEachCounter++,
                list);

        assertThat(forEachCounter, equalTo(list.getLength()));
    }

    @Test
    public void testForEach_array() {
        forEachCounter = 0;
        Integer[] array = new Integer[] {1, 2, 3, 4, 5};
        testFunction(new ForEachFunctionFactory(),
                data -> forEachCounter++,
                array);

        assertThat(forEachCounter, equalTo(array.length));
    }
}
