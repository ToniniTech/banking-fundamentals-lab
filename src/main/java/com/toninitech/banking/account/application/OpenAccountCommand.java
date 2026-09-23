package com.toninitech.banking.account.application;

import java.math.BigDecimal;
import java.util.Objects;

public record OpenAccountCommand(BigDecimal openingBalance, String currencyCode) {

    public OpenAccountCommand {
        Objects.requireNonNull(openingBalance, "openingBalance is required");
        Objects.requireNonNull(currencyCode, "currencyCode is required");
    }
}

