package com.toninitech.banking.account.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record OpenAccountRequest(
        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal openingBalance,

        @NotBlank
        @Pattern(regexp = "[A-Z]{3}")
        String currency) {
}

