package sh.scrap.scrapper.functions;

import org.w3c.dom.Node;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;

import javax.inject.Named;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Arrays;

@Named("to-string")
public class ToStringFunctionFactory implements DataScrapperFunctionFactory {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscriber -> {
            Object data = context.data();
            if (data instanceof Node) {
                try {
                    data = nodeToString((Node) data);
                } catch (Exception e) {
                    subscriber.onError(e);
                    subscriber.onComplete();
                    return;
                }
            }
            else if (data.getClass().isArray())
                data = Arrays.toString((Object[]) data);
            else if (!(data instanceof String))
                data = data.toString();

            subscriber.onNext(context.withData(data));
            subscriber.onComplete();
        };
    }

    private String nodeToString(Node node) throws TransformerException {
        StringWriter sw = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty(OutputKeys.INDENT, "no");
        t.transform(new DOMSource(node), new StreamResult(sw));
        return sw.toString();
    }
}
