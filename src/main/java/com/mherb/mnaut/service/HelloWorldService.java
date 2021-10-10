package com.mherb.mnaut.service;

import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HelloWorldService {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldService.class);

    @Value("${hello.service.greeting}")
    private String greeting;

    @EventListener
    public void onStartup(StartupEvent startupEvent) {
        log.debug("Startup: {}", HelloWorldService.class.getSimpleName());
    }

    public String sayHi() {
        return greeting;
    }
}
