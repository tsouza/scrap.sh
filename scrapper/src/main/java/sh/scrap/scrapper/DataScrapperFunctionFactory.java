package sh.scrap.scrapper;

public interface DataScrapperFunctionFactory {

    DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args);

    default boolean isValidName(String name) {
        return true;
    }
}
