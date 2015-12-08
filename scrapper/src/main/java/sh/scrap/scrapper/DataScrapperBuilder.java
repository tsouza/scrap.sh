package sh.scrap.scrapper;

import javax.script.ScriptException;

public interface DataScrapperBuilder {

    Field field(String fieldName);

    DataScrapperBuilder addLibrarySource(String source)
            throws ScriptException;

    DataScrapper build();

    interface Field {
        Field map(String functionName, Object... args);
        Field forEach();
        DataScrapper build();
    }
}
