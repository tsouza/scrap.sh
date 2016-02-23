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
