package sh.scrap.scraplet;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.collect.ImmutableMap;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import sh.scrap.scrapper.DataScrapper;
import sh.scrap.scrapper.DataScrapperBuilderFactory;

import java.util.HashMap;
import java.util.Map;

public class ValidatorService {

    public Map<String, Object> handle(Map<String, Object> input, Context context) throws Throwable {
        Map<String, Object> result = new HashMap<>();
        try {
            try {
                createScrapper((String) input.get("Script"));
                return ImmutableMap.<String, Object>builder()
                        .put("Result", "VALID")
                        .build();
            } catch (ParseCancellationException e) {
                throw e.getCause();
            }
        } catch (NoViableAltException | InputMismatchException e) {
            Token offendingToken = e.getOffendingToken();
            return ImmutableMap.<String, Object>builder()
                    .put("Result", "INVALID")
                    .put("Detail", ImmutableMap.<String, Object>builder()
                        .put("Line", offendingToken.getLine())
                        .put("Column", offendingToken.getCharPositionInLine())
                        .put("Text", offendingToken.getText())
                        .build())
                    .build();
        }
    }

    private DataScrapper createScrapper(String script) throws Exception {
        return DataScrapperBuilderFactory
                .fromScript(script)
                .createBuilder()
                .build();
    }
}
