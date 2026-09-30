package com.toninitech.banking.account.application;

import com.toninitech.banking.shared.money.Money;

import java.util.Objects;
import java.util.UUID;

/**
 * A small, framework-free command for the Unit 4 transfer use case.
 */
public record TransferCommand(UUID sourceAccountId, UUID targetAccountId, Money amount) {

    public TransferCommand {
        Objects.requireNonNull(sourceAccountId, "sourceAccountId is required");
        Objects.requireNonNull(targetAccountId, "targetAccountId is required");
        Objects.requireNonNull(amount, "amount is required");
        if (sourceAccountId.equals(targetAccountId)) {
            throw new IllegalArgumentException("Source and target accounts must be different");
        }
    }
}
