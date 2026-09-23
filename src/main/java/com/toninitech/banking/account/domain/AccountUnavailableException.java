package com.toninitech.banking.account.domain;

import java.util.UUID;

public final class AccountUnavailableException extends RuntimeException {

    public AccountUnavailableException(UUID accountId, AccountStatus status) {
        super("Account %s is not available: status=%s".formatted(accountId, status));
    }
}

