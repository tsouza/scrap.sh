package sh.scrap.scrapper;

public interface DataScrapperFunctionLibrary {

    Object invoke(String functionName, Object data, Object... args) throws Exception;
}
