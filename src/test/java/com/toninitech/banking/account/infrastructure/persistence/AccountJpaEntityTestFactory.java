package com.toninitech.banking.account.infrastructure.persistence;

import com.toninitech.banking.account.domain.AccountStatus;

import java.math.BigDecimal;
import java.util.UUID;

public final class AccountJpaEntityTestFactory {

    private AccountJpaEntityTestFactory() {
    }

    public static AccountJpaEntity rawEntity(String balance) {
        return new AccountJpaEntity(
                UUID.randomUUID(),
                new BigDecimal(balance),
                "USD",
                AccountStatus.ACTIVE);
    }

    public static UUID idOf(AccountJpaEntity entity) {
        return entity.id();
    }

    public static void changeBalance(AccountJpaEntity entity, String balance) {
        entity.changeBalanceForLab(new BigDecimal(balance));
    }
}
