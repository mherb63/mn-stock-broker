package com.mherb.mnaut.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Symbol", description = "Abbreviation for publicly traded stock")
public class Symbol {
    @Schema(description = "stock symbol value", minLength = 1, maxLength = 4)
    private String value;
}
