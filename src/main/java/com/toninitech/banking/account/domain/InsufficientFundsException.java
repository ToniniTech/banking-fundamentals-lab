package com.toninitech.banking.account.domain;

import com.toninitech.banking.shared.money.Money;


import java.util.UUID;

public final class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(UUID accountId, Money available, Money requested) {
        super("Insufficient funds for account %s: available=%s, requested=%s"
                .formatted(accountId, available.amount(), requested.amount()));
    }

}

