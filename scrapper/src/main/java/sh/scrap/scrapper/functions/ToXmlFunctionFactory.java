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
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

@Name("to-xml")
public class ToXmlFunctionFactory implements DataScrapperFunctionFactory {

    private final Parser xmlParser;
    private final Parser htmlParser;

    public ToXmlFunctionFactory() throws ParserConfigurationException {
        DocumentBuilder xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        xmlParser = xmlBuilder::parse;

        HtmlDocumentBuilder htmlBuilder = new HtmlDocumentBuilder();
        htmlParser = htmlBuilder::parse;

        htmlBuilder.setHeuristics(Heuristics.ALL);
    }

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
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

    protected Node toXML(Object data) throws IOException, SAXException {
        String body = data.toString().trim();

        Parser parser = body.startsWith("<?xml") ?
                xmlParser : htmlParser;

        return parser.parse(new InputSource(new StringReader(body)))
                .getDocumentElement();
    }

    interface Parser {
        Document parse(InputSource source) throws IOException, SAXException;
    }
}
