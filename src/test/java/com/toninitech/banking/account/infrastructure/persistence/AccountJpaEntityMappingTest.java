package com.toninitech.banking.account.infrastructure.persistence;

import com.toninitech.banking.account.domain.AccountStatus;
import com.toninitech.banking.account.domain.BankAccount;
import com.toninitech.banking.shared.money.Money;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountJpaEntityMappingTest {

    @Test
    void mapsBetweenDomainAndPersistenceWithoutSharingTheSameObject() {
        UUID id = UUID.fromString("F0001234-0451-4000-B000-000000000000");

        BankAccount domain = BankAccount.open(id,
                Money.of("100.00", "USD"));

        AccountJpaEntity entity = AccountJpaEntity.fromDomain(domain);
        BankAccount rehydrated = entity.toDomain();

        assertNotSame(entity, rehydrated);
        assertEquals(entity.id(), rehydrated.id());
        assertEquals(entity.balance(), rehydrated.balance().amount());
        assertEquals(entity.status(), rehydrated.status());
        assertEquals(rehydrated, domain);
        assertNotSame(rehydrated, domain);
    }
}
