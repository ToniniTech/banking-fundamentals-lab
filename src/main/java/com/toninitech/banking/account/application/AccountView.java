package com.toninitech.banking.account.application;

import com.toninitech.banking.account.domain.AccountStatus;
import com.toninitech.banking.account.domain.BankAccount;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountView(
        UUID id,
        BigDecimal balance,
        String currency,
        AccountStatus status) {

    public static AccountView from(BankAccount account) {
        return new AccountView(
                account.id(),
                account.balance().amount(),
                account.balance().currency().getCurrencyCode(),
                account.status());
    }
}

