package sh.scrap.scrapper.functions;

import net.sf.saxon.lib.NamespaceConstant;
import net.sf.saxon.xpath.XPathEvaluator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import sh.scrap.scrapper.DataScrapperExecutionContext;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

@Name("xpath")
public class XPathFunctionFactory extends ToXmlFunctionFactory implements DataScrapperFunctionFactory {

    public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    static {
        System.setProperty("javax.xml.xpath.XPathFactory:" + NamespaceConstant.OBJECT_MODEL_SAXON,
                "net.sf.saxon.xpath.XPathFactoryImpl");
    }

    private final XPath xpath;

    public XPathFunctionFactory() throws XPathFactoryConfigurationException, ParserConfigurationException {
        XPathFactory factory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
        xpath = factory.newXPath();
        ((XPathEvaluator) xpath).getStaticContext().setDefaultElementNamespace(XHTML_NS);
    }

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscriber -> {
            try {
                XPathExpression xpath = this.xpath.compile((String) args[0]);
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        Object data = context.data();
                        if (data instanceof Node)
                            process(xpath, context, subscriber);

                        else try {
                            process(xpath, context.withData(toXML(data)), subscriber);
                        } catch (SAXException | IOException e) {
                            subscriber.onError(e);
                            subscriber.onComplete();
                        }


                    }
                    @Override public void cancel() {}
                });
            } catch (XPathExpressionException e) {
                subscriber.onError(e);
            }
        };
    }

    private void process(XPathExpression xpath, DataScrapperExecutionContext context, Subscriber<? super DataScrapperExecutionContext> subscriber) {
        try {
            Object result = xpath.evaluate(context.data(), XPathConstants.NODESET);
            subscriber.onNext(context.withData(result));
        } catch (XPathExpressionException e) {
            subscriber.onError(e);
            return;
        }
        subscriber.onComplete();
    }

}
