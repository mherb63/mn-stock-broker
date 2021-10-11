package com.mherb.mnaut.controller;

import com.mherb.mnaut.configuration.GreetingConfig;
import com.mherb.mnaut.domain.Greeting;
import com.mherb.mnaut.service.HelloWorldService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("${hello.controller.path:/hello}")
@Secured(SecurityRule.IS_ANONYMOUS)
public class HelloWorldController {
    private final HelloWorldService service;
    private final GreetingConfig config;

    public HelloWorldController(HelloWorldService service, GreetingConfig config) {
        this.service = service;
        this.config = config;
    }

    @Get("/")
    public String index() {
        return service.sayHi();
    }

    @Get("/de")
    public String greetInGerman() {
        return config.getDe();
    }

    @Get("/en")
    public String greetInEnglish() {
        return config.getEn();
    }

    @Get("/json")
    public Greeting json() {
        return new Greeting();
    }
}
