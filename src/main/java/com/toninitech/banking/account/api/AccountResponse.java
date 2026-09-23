package com.toninitech.banking.account.api;

import com.toninitech.banking.account.application.AccountView;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        BigDecimal balance,
        String currency,
        String status) {

    public static AccountResponse from(AccountView view) {
        return new AccountResponse(
                view.id(),
                view.balance(),
                view.currency(),
                view.status().name());
    }
}

