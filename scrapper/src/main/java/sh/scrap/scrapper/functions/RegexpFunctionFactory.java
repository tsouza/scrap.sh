package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;
import sh.scrap.scrapper.annotation.Requires;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("regexp") @Requires("to-string")
public class RegexpFunctionFactory implements DataScrapperFunctionFactory<String> {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, String mainArgument, Map<String, Object> annotations) {
        Pattern pattern = Pattern.compile(mainArgument,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Object separator = annotations.getOrDefault("separator", "");
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                String data = (String) context.data();
                context.objectProcessed(data);
                Matcher matcher = pattern.matcher(data);
                List<String> result = new ArrayList<>();
                while (matcher.find()) {
                    if (matcher.groupCount() > 0) {
                        StringBuilder joined = new StringBuilder();
                        for (int i = 1; i <= matcher.groupCount(); i++)
                            joined.append(i == 1 ? "" : separator)
                                    .append(matcher.group(i));
                        String str = joined.toString();
                        context.objectProcessed(str);
                        result.add(str);
                    }
                    else {
                        String str = matcher.group(0);
                        context.objectProcessed(str);
                        result.add(str);
                    }
                }

                subscriber.onNext(context.withData(result));
                subscriber.onComplete();
            }
            @Override public void cancel() {}
        });
    }

}
