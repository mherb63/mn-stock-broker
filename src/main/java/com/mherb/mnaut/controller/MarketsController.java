package com.mherb.mnaut.controller;

import com.mherb.mnaut.domain.InMemoryStore;
import com.mherb.mnaut.domain.Symbol;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Controller("/markets")
public class MarketsController {
    private final InMemoryStore store;

    public MarketsController(InMemoryStore store) {
        this.store = store;
    }

    @Operation(summary = "Returns all available markets")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get
    public List<Symbol> all() {
        return store.getAllSymbols();
    }
}
