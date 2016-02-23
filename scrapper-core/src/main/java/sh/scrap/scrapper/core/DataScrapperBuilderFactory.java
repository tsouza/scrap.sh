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
package sh.scrap.scrapper.core;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import sh.scrap.scrapper.DataScrapperBuilder;
import sh.scrap.scrapper.functions.JsonFunctionFactory;
import sh.scrap.scrapper.parser.ScrapBaseListener;
import sh.scrap.scrapper.parser.ScrapLexer;
import sh.scrap.scrapper.parser.ScrapParser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class DataScrapperBuilderFactory {

    public static DataScrapperBuilderFactory fromScript(Reader script) throws IOException {
        return fromScript(new ANTLRInputStream(script));
    }

    public static DataScrapperBuilderFactory fromScript(String script) {
        return fromScript(new ANTLRInputStream(script));
    }

    private static DataScrapperBuilderFactory fromScript(ANTLRInputStream script) {
        ReactorDataScrapperBuilder builder = new ReactorDataScrapperBuilder();
        ScrapLexer lexer = new ScrapLexer(script);
        ScrapParser parser = new ScrapParser(new CommonTokenStream(lexer),
                builder::isValidFunctionName);
        parser.setErrorHandler(new BailErrorStrategy());
        ScrapParser.ScrapOutputContext output = parser.scrapOutput();
        return new DataScrapperBuilderFactory(builder, output);
    }

    private final ScrapParser.ScrapOutputContext output;
    private final DataScrapperBuilder builder;

    private DataScrapperBuilderFactory(DataScrapperBuilder builder, ScrapParser.ScrapOutputContext output) {
        this.output = output;
        this.builder = builder;
    }

    public DataScrapperBuilder createBuilder() {

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new BuildingListener(), output);

        return builder;
    }

    private class BuildingListener extends ScrapBaseListener {

        DataScrapperBuilder.Field currentField;
        String functionName;
        Object mainArgument;
        Map<String, Object> annotations = new HashMap<>();

        @Override
        public void enterFunctionName(ScrapParser.FunctionNameContext ctx) {
            functionName = ctx.getText();
        }

        @Override
        public void enterFieldName(ScrapParser.FieldNameContext ctx) {
            currentField = builder.field(ctx.getText());
        }

        @Override
        public void enterTypeCast(ScrapParser.TypeCastContext ctx) {
            String text = ctx.getText();
            if (text.endsWith("array")) {
                currentField = currentField.asArray();
                text = text.replace("array", "");
            }
            currentField = currentField
                    .castTo(DataScrapperBuilder.FieldType
                            .valueOf(text.toUpperCase()));
        }

        @Override
        public void enterMainArgument(ScrapParser.MainArgumentContext ctx) {
            mainArgument = parseArgument(ctx.getText());
        }

        @Override @SuppressWarnings("unchecked")
        public void enterAnnotationsList(ScrapParser.AnnotationsListContext ctx) {
            String json = ctx.jsonObject().getText();
            if (StringUtils.isNotEmpty(json))
                annotations = (Map<String, Object>) JsonFunctionFactory
                        .jsonProvider.parse(json);
        }

        @Override
        public void exitSingleExpression(ScrapParser.SingleExpressionContext ctx) {
            currentField = currentField.map(functionName, mainArgument, annotations);
        }

        @Override
        public void exitIterationExpression(ScrapParser.IterationExpressionContext ctx) {
            currentField = currentField.map(functionName, mainArgument, annotations)
                    .forEach();
        }

        @Override
        public void exitExpression(ScrapParser.ExpressionContext ctx) {
            mainArgument = null;
            annotations = null;
        }
    }

    private Object parseArgument(String text) {
        if (text.startsWith("'") || text.startsWith("\"")) {
            boolean startsWithSingleQuote = text.startsWith("'");
            text = text.substring(1, text.length() - 1);
            text = StringEscapeUtils.unescapeEcmaScript(text);
            if (startsWithSingleQuote && text.length() == 1)
                return text.charAt(0);
            else
                return text;

        } else switch (text) {
            case "null": return null;
            case "true":
            case "false":
                return Boolean.valueOf(text);
            default: try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return Double.parseDouble(text);
            }
        }
    }
}
