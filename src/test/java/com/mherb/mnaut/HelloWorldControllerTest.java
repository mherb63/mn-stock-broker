package com.mherb.mnaut;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.rxjava2.http.client.RxHttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class HelloWorldControllerTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    RxHttpClient client;


    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testHelloResponse() {
        final String response = client.toBlocking().retrieve("/hello");
        assertEquals("Hello from HelloWorldService", response);
    }

    @Test
    void testGermanGreeting() {
        final String response = client.toBlocking().retrieve("/hello/de");
        assertEquals("Hallo", response);
    }

    @Test
    void testEnglishGreeting() {
        final String response = client.toBlocking().retrieve("/hello/en");
        assertEquals("Hello", response);
    }

//    @Test
//    void testGreetingAsJson() {
//        final ObjectNode response = client.toBlocking().retrieve("/hello/json", ObjectNode.class);
//        assertEquals("someJson", response.toString());
//
//    }
}
