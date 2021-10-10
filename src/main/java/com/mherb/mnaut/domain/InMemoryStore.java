package com.mherb.mnaut.domain;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;

import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {
    private final List<Symbol> symbols = new ArrayList<>();
    private final Map<String, Quote> cachedQuotes = new HashMap<>();
    private static final ThreadLocalRandom localRandom = ThreadLocalRandom.current();

    @EventListener
    public void onStartup(StartupEvent startupEvent) {
        symbols.addAll(Stream.of("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList())
        );
        symbols.forEach(symbol ->
                cachedQuotes.put(symbol.getValue(), initRandomQuote(symbol))
        );
    }

    public Optional<Quote> fetchQuote(String symbol) {
        return Optional.ofNullable(cachedQuotes.get(symbol));
    }

    public void update(final Quote quote) {
        cachedQuotes.put(quote.getSymbol().getValue(), quote);
    }

    public List<Symbol> getAllSymbols() {
        return symbols;
    }

    public Quote initRandomQuote(final Symbol symbol) {
        return Quote.builder().symbol(symbol).bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(localRandom.nextDouble(1, 100));
    }

}
