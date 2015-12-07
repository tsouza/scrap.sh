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

@Name("to-string")
public class ToStringFunctionFactory implements DataScrapperFunctionFactory {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data = context.data();
                if (data instanceof Text)
                    data = ((Text) data).getTextContent();

                else if (data instanceof Attr)
                    data = ((Attr) data).getValue();

                else if (data instanceof Node) {
                    try {
                        data = nodeToString((Node) data);
                    } catch (Exception e) {
                        subscriber.onError(e);
                        subscriber.onComplete();
                        return;
                    }
                }

                else if (!(data instanceof String))
                    data = data.toString();

                subscriber.onNext(context.withData(data));
                subscriber.onComplete();
            }

            @Override public void cancel() {}
        });
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
