package sh.scrap.scrapper;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import sh.scrap.scrapper.functions.JsonFunctionFactory;
import sh.scrap.scrapper.impl.ReactorDataScrapperBuilder;
import sh.scrap.scrapper.parser.ScrapBaseListener;
import sh.scrap.scrapper.parser.ScrapLexer;
import sh.scrap.scrapper.parser.ScrapParser;

import java.util.HashMap;
import java.util.Map;

public class DataScrapperBuilderFactory {

    public static DataScrapperBuilderFactory fromScript(String script) {
        ReactorDataScrapperBuilder builder = new ReactorDataScrapperBuilder();
        ScrapLexer lexer = new ScrapLexer(new ANTLRInputStream(script));
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

        boolean isArray = false;

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
                isArray = true;
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

        @Override
        public void exitFieldExpression(ScrapParser.FieldExpressionContext ctx) {
            if (isArray)
                currentField.forEach();

            isArray = false;
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

        } else if ("null".equals(text))
            return null;

        else if (text.matches("^(true|false)$"))
            return Boolean.valueOf(text);

        else try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return Double.parseDouble(text);
        }
    }
}
