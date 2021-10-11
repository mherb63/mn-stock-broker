package com.mherb.mnaut;

import com.mherb.mnaut.domain.CustomError;
import com.mherb.mnaut.domain.InMemoryStore;
import com.mherb.mnaut.domain.Quote;
import com.mherb.mnaut.domain.Symbol;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.rxjava2.http.client.RxHttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static io.micronaut.http.HttpRequest.GET;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final Quote apple = generateQuote("APPL");
        store.update(apple);
        final Quote amazon = generateQuote("AMZN");
        store.update(amazon);

        final Quote appleResult = client.toBlocking().retrieve(GET("/quotes/APPL"), Quote.class);
        log.info("Result: {}", appleResult);
        assertThat(apple).usingRecursiveComparison().isEqualTo(appleResult);

        final Quote amazonResult = client.toBlocking().retrieve(GET("/quotes/AMZN"), Quote.class);
        log.info("Result: {}", amazonResult);
        assertThat(amazon).usingRecursiveComparison().isEqualTo(amazonResult);
    }

    @Test
    void testSymbolNotFound() {
        try {
            final Quote notFoundResult = client.toBlocking().retrieve(GET("/quotes/ABCD"), Quote.class);
        } catch (HttpClientResponseException e) {
            log.error("Response Body: " + e.getResponse().getBody(CustomError.class));
            assertEquals(HttpStatus.NOT_FOUND, e.getResponse().getStatus());
            final Optional<CustomError> customError = e.getResponse().getBody(CustomError.class);
            assertTrue(customError.isPresent());
            assertEquals(404, customError.get().getStatus());
        }
    }

    public Quote generateQuote(final String symbol) {
        return Quote.builder().symbol(new Symbol(symbol)).bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }

}
