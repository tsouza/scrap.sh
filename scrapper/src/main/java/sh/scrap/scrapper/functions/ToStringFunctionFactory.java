package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Map;

@Name("to-string")
public class ToStringFunctionFactory implements DataScrapperFunctionFactory<Void> {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       Void mainArgument, Map<String, Object> annotations) {
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                try {
                    String data = ToStringFunctionFactory.toString(context.data());
                    subscriber.onNext(context.withData(data));
                    subscriber.onComplete();
                } catch (TransformerException e) {
                    subscriber.onError(e);
                    subscriber.onComplete();
                }
            }

            @Override public void cancel() {}
        });
    }

    static String toString(Object data) throws TransformerException {
        if (data instanceof Text)
            data = ((Text) data).getTextContent();

        else if (data instanceof Attr)
            data = ((Attr) data).getValue();

        else if (data instanceof Node)
            data = nodeToString((Node) data);

        else if (!(data instanceof String))
            data = data.toString();

        return (String) data;
    }

    private static String nodeToString(Node node) throws TransformerException {
        StringWriter sw = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty(OutputKeys.INDENT, "no");
        t.transform(new DOMSource(node), new StreamResult(sw));
        return sw.toString();
    }
}
