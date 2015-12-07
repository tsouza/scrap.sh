package sh.scrap.scrapper;

public interface DataScrapperFunctionFactory {
    DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args);
}
