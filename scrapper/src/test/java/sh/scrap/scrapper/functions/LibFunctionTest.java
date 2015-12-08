package sh.scrap.scrapper.functions;

import org.junit.Test;
import sh.scrap.scrapper.DataScrapperFunctionFactory;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class LibFunctionTest extends FunctionsTestBase {

    @Test
    public void testLib_simple() throws Exception {

        addLibrarySource("function test(input, arg) { " +
                "return java.util.Arrays.asList((input + ' ' + arg).split(' ')); " +
                "}");

        String text = "1 2 3";
        testFunction(new LibFunctionFactory(),
                data -> assertThat(data, equalTo(Arrays.asList("1", "2", "3", "4"))),
                text, "4");
    }


    @Override
    protected String getName(DataScrapperFunctionFactory factory) {
        return "test";
    }
}
