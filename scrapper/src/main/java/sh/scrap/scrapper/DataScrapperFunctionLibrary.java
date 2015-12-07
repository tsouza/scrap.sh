package sh.scrap.scrapper;

public interface DataScrapperFunctionLibrary {

    Object invoke(String functionName, Object data) throws Exception;
}
