package sh.scrap.scrapper;

import java.util.Map;

public interface DataScrapperFunctionFactory<T> {

    DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                T mainArgument, Map<String, Object> annotations);

    default boolean isValidName(String name) {
        return true;
    }
}
