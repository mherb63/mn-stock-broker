package com.mherb.mnaut;

import com.mherb.mnaut.domain.InMemoryStore;
import com.mherb.mnaut.domain.Quote;
import com.mherb.mnaut.domain.Symbol;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.rxjava2.http.client.RxHttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static io.micronaut.http.HttpRequest.GET;

@MicronautTest
@Slf4j
class QuotesControllerTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    InMemoryStore store;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void testGetQuote() {
        store.update(Quote.builder()
                .symbol(new Symbol("APPL"))
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build());

        final Quote quote = client.toBlocking().retrieve(GET("/quotes/APPL"), Quote.class);
        log.debug("Quote: " + quote);
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }

}
