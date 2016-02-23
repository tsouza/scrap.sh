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
package sh.scrap.scrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import sh.scrap.scrapper.core.DataScrapperBuilderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

@SpringBootApplication
public class SampleRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SampleRunner.class);

    @Autowired DataScrapper scrapper;

    @Value("${input}")
    Resource input;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try (InputStream input = this.input.getInputStream()) {
            String data = IOUtils.toString(input);
            scrapper.scrap(data)
                    .onSuccess(result -> log.info(result == null ? null : result.toString()))
                    .onError(exception -> log.error(exception.getMessage(), exception));
        }
    }

    @Bean DataScrapper scrapper(@Value("${script}") Reader script) throws IOException {
        return DataScrapperBuilderFactory
                .fromScript(script)
                .createBuilder()
                .build();
    }
}
