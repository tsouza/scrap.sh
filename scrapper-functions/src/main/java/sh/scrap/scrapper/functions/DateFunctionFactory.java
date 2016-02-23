/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Thiago Souza <thiago@scrap.sh>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
