package com.mherb.mnaut;

import com.mherb.mnaut.controller.WatchListReactiveController;
import com.mherb.mnaut.domain.InMemoryAccountStore;
import com.mherb.mnaut.domain.Symbol;
import com.mherb.mnaut.domain.WatchList;
import com.mherb.mnaut.security.JwtWatchListClient;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@Slf4j
class WatchListReactiveControllerTest {

    private static final UUID TEST_ACCOUNT_ID = WatchListReactiveController.ACCOUNT_ID;

    @Inject
    @Client("/")
    JwtWatchListClient client;

    @Inject
    InMemoryAccountStore store;

    @Test
    void returnsEmptyWatchListForAccount() {
        final Single<WatchList> result = client.retrieveWatchList(getAuthorizationHeader()).singleOrError();
        assertTrue(result.blockingGet().getSymbols().isEmpty());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnsWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "NFLX")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        var result = client.retrieveWatchList(getAuthorizationHeader()).singleOrError().blockingGet();
        assertEquals(3, result.getSymbols().size());
        assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void returnsWatchListForAccountAsSingle() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "NFLX")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final WatchList result = client.retrieveWatchListAsSingle(getAuthorizationHeader()).blockingGet();
        assertEquals(3, result.getSymbols().size());
        assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    void canUpdateWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "NFLX")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        final HttpResponse<Object> added = client.updateWatchList(getAuthorizationHeader(), watchList);
        assertEquals(HttpStatus.OK, added.getStatus());
        assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID));
    }

    @Test
    void canDeleteWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "NFLX")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);
        assertFalse(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

        final HttpResponse<Object> deleted = client.deleteWatchList(getAuthorizationHeader(), WatchListReactiveController.ACCOUNT_ID);
        assertEquals(HttpStatus.OK, deleted.getStatus());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    private String getAuthorizationHeader() {
        return "Bearer " + givenMyUserLoggedIn().getAccessToken();
    }

    private BearerAccessRefreshToken givenMyUserLoggedIn() {
        return client.login(new UsernamePasswordCredentials("my-user", "secret"));
    }

}
