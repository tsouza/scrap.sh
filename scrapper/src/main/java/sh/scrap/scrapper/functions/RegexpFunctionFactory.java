package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;
import sh.scrap.scrapper.annotation.Requires;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("regexp") @Requires("to-string")
public class RegexpFunctionFactory implements DataScrapperFunctionFactory {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        Pattern pattern = Pattern.compile((String) args[0],
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        String joiner = args.length > 1 ? (String) args[1] : "";
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                String data = (String) context.data();
                Matcher matcher = pattern.matcher(data);
                List<String> result = new ArrayList<>();
                while (matcher.find()) {
                    if (matcher.groupCount() > 0) {
                        StringBuilder joined = new StringBuilder();
                        for (int i = 1; i <= matcher.groupCount(); i++)
                            joined.append(i == 1 ? "" : joiner)
                                    .append(matcher.group(i));
                        result.add(joined.toString());
                    }
                    else
                        result.add(matcher.group(0));
                }

                subscriber.onNext(context.withData(result));
                subscriber.onComplete();
            }
            @Override public void cancel() {}
        });
    }

}
