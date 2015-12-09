package sh.scrap.scrapper;

import javax.script.ScriptException;
import java.util.Map;

public interface DataScrapperBuilder {

    Field field(String fieldName);

    DataScrapperBuilder addLibrarySource(String source)
            throws ScriptException;

    DataScrapper build();

    interface Field {
        Field castTo(FieldType type);
        Field map(String functionName);
        Field map(String functionName, Object mainArgument);
        Field map(String functionName, Object mainArgument, Map<String, Object> annotations);
        Field forEach();
        DataScrapper build();
    }

    enum FieldType {
        STRING,
        NUMBER,
        BOOLEAN
    }
}
