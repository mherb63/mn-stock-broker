package com.mherb.mnaut.configuration;

import io.micronaut.context.annotation.ConfigurationInject;
import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("hello.config.greeting")
@Data
public class GreetingConfig {
    private final String de;
    private final String en;

    @ConfigurationInject
    public GreetingConfig(@NotBlank String de, @NotBlank String en) {
        this.de = de;
        this.en = en;
    }
}
