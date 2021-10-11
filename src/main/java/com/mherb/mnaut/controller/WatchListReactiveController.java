package com.mherb.mnaut.controller;

import com.mherb.mnaut.domain.InMemoryAccountStore;
import com.mherb.mnaut.domain.WatchList;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Controller("/account/watchlist-reactive")
@Slf4j
public class WatchListReactiveController {

    public static final UUID ACCOUNT_ID = UUID.randomUUID();

    private final InMemoryAccountStore store;
    private final Scheduler scheduler;

    public WatchListReactiveController(InMemoryAccountStore store,
                                       @Named(TaskExecutors.IO)ExecutorService executorService) {
        this.store = store;
        this.scheduler = Schedulers.from(executorService);
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public WatchList get() {
        log.debug("getWatchList - " + Thread.currentThread().getName());
        return store.getWatchList(ACCOUNT_ID);
    }

    @Get(value = "/single",  produces = MediaType.APPLICATION_JSON)
    public Single<WatchList> getAsSingle() {
        return Single.fromCallable(() -> {
            log.debug("getWatchList single - " + Thread.currentThread().getName());
            return store.getWatchList(ACCOUNT_ID);
        }).subscribeOn(scheduler);
    }

    @Put(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(value = "/{accountId}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public void delete(@PathVariable UUID accountId) {
        store.deleteWatchList(accountId);
    }
}
