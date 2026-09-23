package com.toninitech.banking.account.application;

import java.math.BigDecimal;
import java.util.Objects;

public record MoneyOperationCommand(BigDecimal amount, String currencyCode) {

    public MoneyOperationCommand {
        Objects.requireNonNull(amount, "amount is required");
        Objects.requireNonNull(currencyCode, "currencyCode is required");
    }
}

