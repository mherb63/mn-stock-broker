package com.mherb.mnaut.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Greeting {

    private final String myText = "Hello World";
    private final BigDecimal id = BigDecimal.ONE;
    private final Instant timeUTC = Instant.now();
}
