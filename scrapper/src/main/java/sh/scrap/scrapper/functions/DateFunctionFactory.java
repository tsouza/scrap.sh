package sh.scrap.scrapper.functions;

import org.apache.commons.lang3.LocaleUtils;
import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;
import sh.scrap.scrapper.annotation.Requires;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;

@Name("date") @Requires("to-string")
public class DateFunctionFactory implements DataScrapperFunctionFactory<String> {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       String pattern, Map<String, Object> annotations) {
        return context -> subscription -> {
            Locale locale = parseLocale(annotations.getOrDefault("locale", EN_US));
            ZoneId zoneId = ZoneId.of((String) annotations.getOrDefault("timezone", "UTC"));

            Locale finalLocale = locale;
            subscription.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    String data = context.data().toString();
                    ZonedDateTime parsed;
                    context.objectProcessed(data);
                    try {
                        switch (name) {
                            case "parse":
                                parsed = parse(data, pattern, finalLocale, zoneId);
                                context.objectProcessed(parsed);
                                subscription.onNext(context.withData(format(parsed, ISO8601, EN_US, ZoneId.of("UTC"))));
                                subscription.onComplete();
                                return;
                            case "format":
                                parsed = parse(data, ISO8601, EN_US, ZoneId.of("UTC"));
                                context.objectProcessed(parsed);
                                subscription.onNext(context.withData(format(parsed, pattern, finalLocale, zoneId)));
                                subscription.onComplete();
                                return;
                        }
                    } catch (DateTimeParseException e) {
                        subscription.onError(e);
                        subscription.onComplete();
                        return;
                    }
                }

                @Override
                public void cancel() {}
            });
        };
    }

    private ZonedDateTime parse(String data, String pattern, Locale locale, ZoneId zoneId) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern, locale)
                .withZone(zoneId);
        return ZonedDateTime.parse(data, format);
    }

    private String format(ZonedDateTime data, String pattern, Locale locale, ZoneId zoneId) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern, locale)
                .withZone(zoneId);
        return data.format(format);
    }

    @Override
    public boolean isValidName(String name) {
        return "parse".equals(name) ||
                "format".equals(name);
    }

    private Locale parseLocale(Object value) {
        if (value instanceof Locale)
            return (Locale) value;
        return LocaleUtils.toLocale((String) value);
    }

    private static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final Locale EN_US = new Locale("en", "US");

}
