package sh.scrap.scrapper;

public interface DataScrapperBuilder {

    Field field(String fieldName);

    DataScrapper build();

    interface Field {
        Field map(String functionName, Object... args);
        Field forEach();
        DataScrapper build();
    }
}
