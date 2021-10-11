package com.mherb.mnaut;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OpenAPIDefinition(
    info = @Info(
        title = "mn-stock-broker",
        version = "0.1",
        description = "Udemy Course on Micronaut",
        license = @License(name = "MIT")
    )
)
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        log.info("Starting application");
        Micronaut.run(Application.class, args);
    }
}
