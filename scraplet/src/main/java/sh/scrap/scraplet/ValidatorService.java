package sh.scrap.scraplet;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.collect.ImmutableMap;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.HashMap;
import java.util.Map;

public class ValidatorService extends AbstractScrapletService {

    public Map<String, Object> handle(Map<String, String> input, Context context) throws Throwable {
        String apiKey = input.get("apiKey");
        String name = input.get("name");
        Map<String, Object> result = new HashMap<>();
        try {
            try {
                createScrapper(apiKey, name);
                return ImmutableMap.<String, Object>builder()
                        .put("result", "VALID")
                        .build();
            } catch (ParseCancellationException e) {
                throw e.getCause();
            }
        } catch (InputMismatchException e) {
            Token offendingToken = e.getOffendingToken();
            return ImmutableMap.<String, Object>builder()
                    .put("result", "INVALID")
                    .put("detail", ImmutableMap.<String, Object>builder()
                        .put("line", offendingToken.getLine())
                        .put("column", offendingToken.getCharPositionInLine())
                        .put("text", offendingToken.getText())
                        .build())
                    .build();
        }
    }
}
