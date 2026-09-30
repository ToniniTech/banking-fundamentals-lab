package com.toninitech.banking.account.infrastructure.persistence;

import com.toninitech.banking.account.application.AccountRepository;
import com.toninitech.banking.account.domain.AccountStatus;
import com.toninitech.banking.account.domain.BankAccount;
import com.toninitech.banking.shared.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("postgres")
@Import(JpaAccountRepositoryAdapter.class)

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
class PostgresAccountRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private JpaAccountRepositoryAdapter repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void savesAndRehydratesTheDomainEntityFromPostgres() {
        UUID accountId = UUID.randomUUID();
        BankAccount original = BankAccount.open(accountId, Money.of("100.00", "USD"));

        repository.save(original);
        entityManager.flush();
        entityManager.clear();

        BankAccount rehydrated = repository.findById(accountId).orElseThrow();


        assertNotSame(original, rehydrated);
        assertEquals(accountId, rehydrated.id());
        assertEquals(Money.of("100.00", "USD"), rehydrated.balance());
        assertEquals(AccountStatus.ACTIVE, rehydrated.status());
    }

    @Test
    void aJpaEntityIsManagedUntilThePersistenceContextIsCleared() {
        AccountJpaEntity entity = AccountJpaEntity.fromDomain(BankAccount.open(
                UUID.randomUUID(), Money.of("100.00", "USD")));

        assertFalse(entityManager.getEntityManager().contains(entity));

        entityManager.persist(entity);
        assertTrue(entityManager.getEntityManager().contains(entity));

        entityManager.flush();
        assertTrue(entityManager.getEntityManager().contains(entity));

        entityManager.clear();
        assertFalse(entityManager.getEntityManager().contains(entity));
    }
}
