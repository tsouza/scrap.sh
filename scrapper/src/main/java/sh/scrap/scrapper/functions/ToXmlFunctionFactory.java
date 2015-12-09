package sh.scrap.scrapper.functions;

import nu.validator.htmlparser.common.Heuristics;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import org.reactivestreams.Subscription;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

@Name("to-xml")
public class ToXmlFunctionFactory implements DataScrapperFunctionFactory<Void> {

    private static final Parser xmlParser;
    private static final Parser htmlParser;

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       Void mainArgument, Map<String, Object> annotations) {
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                try {
                    Object data = context.data();

                    if (!(data instanceof Node))
                        data = toXML(data);

                    subscriber.onNext(context.withData(data));

                } catch (IOException | SAXException e) {
                    subscriber.onError(e);
                    return;
                }
                subscriber.onComplete();
            }

            @Override public void cancel() {}
        });
    }

    static Node toXML(Object data) throws IOException, SAXException {
        String body = data.toString().trim();

        Parser parser = body.startsWith("<?xml") ?
                xmlParser : htmlParser;

        return parser.parse(new InputSource(new StringReader(body)))
                .getDocumentElement();
    }

    interface Parser {
        Document parse(InputSource source) throws IOException, SAXException;
    }

    static {
        try {
            DocumentBuilder xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmlParser = xmlBuilder::parse;

            HtmlDocumentBuilder htmlBuilder = new HtmlDocumentBuilder();
            htmlParser = htmlBuilder::parse;

            htmlBuilder.setHeuristics(Heuristics.ALL);

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }

    }
}
