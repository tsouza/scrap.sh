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
