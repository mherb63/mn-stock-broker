package com.mherb.mnaut;

import com.mherb.mnaut.controller.WatchListReactiveController;
import com.mherb.mnaut.domain.InMemoryAccountStore;
import com.mherb.mnaut.domain.Symbol;
import com.mherb.mnaut.domain.WatchList;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.rxjava2.http.client.RxHttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@Slf4j
class WatchListReactiveControllerTest {

    private static final UUID TEST_ACCOUNT_ID = WatchListReactiveController.ACCOUNT_ID;

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    InMemoryAccountStore store;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void testEmptyWatchListForAccount() {
        Single<WatchList> response = client.retrieve(GET("/account/watchlist-reactive"), WatchList.class).singleOrError();

        assertTrue(response.blockingGet().getSymbols().isEmpty());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
        log.debug("response: " + response);
    }

    @Test
    void testGetWatchListForAccount() {
        List<Symbol> symbols = Stream.of("AAPL", "AMZN", "NFLX").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        WatchList response = client.toBlocking().retrieve("/account/watchlist-reactive", WatchList.class);
        assertEquals(3, response.getSymbols().size());
        assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void testGetWatchListForAccountAsSingle() {
        List<Symbol> symbols = Stream.of("AAPL", "AMZN", "NFLX").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        WatchList response = client.toBlocking().retrieve("/account/watchlist-reactive/single", WatchList.class);
        assertEquals(3, response.getSymbols().size());
        assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void testUpdateWatchListForAccount() {
        List<Symbol> symbols = Stream.of("AAPL", "AMZN", "NFLX").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        HttpResponse<Object> response = client.toBlocking().exchange(PUT("/account/watchlist-reactive", watchList));
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID));
    }

    @Test
    void testDeleteWatchListForAccount() {
        List<Symbol> symbols = Stream.of("AAPL", "AMZN", "NFLX").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);
        assertFalse(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

        HttpResponse<Object> response = client.toBlocking().exchange(DELETE("/account/watchlist-reactive/" + TEST_ACCOUNT_ID));
        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }
}
