package sh.scrap.scrapper;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringEscapeUtils;
import sh.scrap.scrapper.impl.ReactorDataScrapperBuilder;
import sh.scrap.scrapper.parser.ScrapBaseListener;
import sh.scrap.scrapper.parser.ScrapLexer;
import sh.scrap.scrapper.parser.ScrapParser;

import java.util.ArrayList;
import java.util.List;

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
        List<Object> arguments = new ArrayList<>();

        @Override
        public void enterArgumentList(@NotNull ScrapParser.ArgumentListContext ctx) {
            arguments.clear();
        }

        @Override
        public void enterFunctionName(@NotNull ScrapParser.FunctionNameContext ctx) {
            functionName = ctx.getText();
        }

        @Override
        public void enterFieldName(@NotNull ScrapParser.FieldNameContext ctx) {
            currentField = builder.field(ctx.getText());
        }

        @Override
        public void enterArgument(@NotNull ScrapParser.ArgumentContext ctx) {
            String text = ctx.getText();
            if (text.startsWith("'") || text.startsWith("\"")) {
                text = text.substring(1, text.length() - 1);
                text = StringEscapeUtils.unescapeEcmaScript(text);
                arguments.add(text);

            } else if ("null".equals(text))
                arguments.add(null);

            else if (text.matches("^(true|false)$"))
                arguments.add(Boolean.valueOf(text));

            else try {
                arguments.add(Integer.parseInt(text));

            } catch (NumberFormatException e) {
                arguments.add(Double.parseDouble(text));
            }
        }

        @Override
        public void exitIterationFieldExpression(@NotNull ScrapParser.IterationFieldExpressionContext ctx) {
            currentField = currentField.forEach().
                    map(functionName, arguments.toArray());
        }

        @Override
        public void exitSingleFieldExpression(@NotNull ScrapParser.SingleFieldExpressionContext ctx) {
            currentField = currentField.map(functionName, arguments.toArray());
        }
    }
}
