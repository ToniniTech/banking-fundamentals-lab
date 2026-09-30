package com.toninitech.banking.learninglabs.jpa;

import com.toninitech.banking.account.domain.BankAccount;
import com.toninitech.banking.account.infrastructure.persistence.AccountJpaEntity;
import com.toninitech.banking.shared.money.Money;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static com.toninitech.banking.account.infrastructure.persistence.AccountJpaEntityTestFactory.changeBalance;
import static com.toninitech.banking.account.infrastructure.persistence.AccountJpaEntityTestFactory.idOf;
import static com.toninitech.banking.account.infrastructure.persistence.AccountJpaEntityTestFactory.rawEntity;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("postgres")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
class PersistenceContextLabTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void twoFindsInsideOnePersistenceContextReturnTheSameJavaReference() {
        AccountJpaEntity stored = entity("10.00");
        entityManager.persistAndFlush(stored);
        UUID id = idOf(stored);
        entityManager.clear();

        AccountJpaEntity first = entityManager.find(AccountJpaEntity.class, id);
        AccountJpaEntity second = entityManager.find(AccountJpaEntity.class, id);

        assertSame(first, second);
    }

    @Test
    void flushPersistsChangesMadeToAManagedEntityWithoutCallingSaveAgain() {
        AccountJpaEntity managed = entity("10.00");
        entityManager.persistAndFlush(managed);
        UUID id = idOf(managed);

        changeBalance(managed, "25.00");
        entityManager.flush();
        entityManager.clear();
        AccountJpaEntity detached = managed;
        changeBalance(detached, "50.00");

        assertEquals(detached.toDomain().balance(), Money.of("50.00", "USD"));


        AccountJpaEntity reloaded = entityManager.find(AccountJpaEntity.class, id);
        assertEquals(reloaded.toDomain().balance(), Money.of("25.00", "USD"));

        changeBalance(reloaded, "100.00");
        entityManager.flush();

        assertEquals(Money.of("100.00", "USD"), reloaded.toDomain().balance());

    }

    @Test
    void changingADetachedEntityIsNotDetected() {
        AccountJpaEntity detached = entity("10.00");
        entityManager.persistAndFlush(detached);
        UUID id = idOf(detached);
        entityManager.detach(detached);

        changeBalance(detached, "25.00");
        entityManager.flush();
        entityManager.clear();

        AccountJpaEntity reloaded = entityManager.find(AccountJpaEntity.class, id);

        assertNotSame(detached, reloaded);
        assertEquals(Money.of("10.00", "USD"), reloaded.toDomain().balance());
    }

    @Test
    void changingADetachedEntityIsNotDetected2() {
        AccountJpaEntity account = entity("10.00");
        UUID id = idOf(account);
        entityManager.persistAndFlush(account);

        assertTrue(entityManager.getEntityManager().contains(account));
        assertEquals(account.toDomain().balance(), Money.of("10.00","USD"));

        entityManager.detach(account);
        AccountJpaEntity detached = account;
        changeBalance(detached,"23.00");
        entityManager.flush();

        assertEquals(detached.toDomain().balance(), Money.of("23.00", "USD"));

        AccountJpaEntity reloaded = entityManager.find(AccountJpaEntity.class, id);
        assertNotEquals(reloaded.toDomain().balance(), detached.toDomain().balance());

    }

    @Test
    void mergingCanCopyItsCurrentStateIntoAManagedEntity(){
        AccountJpaEntity account = entity("100.00");
        entityManager.persistAndFlush(account);
        entityManager.detach(account);
        changeBalance(account, "150.00");
        AccountJpaEntity managedAccount = entityManager.merge(account);

        // merged object is now managed
        assertTrue(entityManager.getEntityManager().contains(managedAccount));

        // Original object is still detached
        assertFalse(entityManager.getEntityManager().contains(account));
    }

    @Test
    void aDatabaseConstraintCanRemainInvisibleUntilFlush() {
        AccountJpaEntity invalid = rawEntity("-1.00");

        entityManager.persist(invalid);

        assertThrows(PersistenceException.class, entityManager::flush);
    }

    @Test
    void entityLoadedWithFindBecomesManagedByThePersistenceContext() {
        // Arrange: guardamos una entidad
        AccountJpaEntity original = entity("10.00");
        entityManager.persistAndFlush(original);
        UUID id = idOf(original);

        // La expulsamos del persistence context.
        entityManager.clear();

        assertFalse(entityManager.getEntityManager().contains(original));

        // Act: volvemos a recuperarla desde PostgreSQL
        AccountJpaEntity loaded = entityManager.find(AccountJpaEntity.class, id);

        assertTrue(entityManager.getEntityManager().contains(loaded));

        entityManager.clear();

        assertFalse(entityManager.getEntityManager().contains(original));

    }

    private static AccountJpaEntity entity(String balance){
        return AccountJpaEntity.fromDomain(BankAccount.open(
                UUID.randomUUID(), Money.of(balance, "USD")
        ));
    }

}
