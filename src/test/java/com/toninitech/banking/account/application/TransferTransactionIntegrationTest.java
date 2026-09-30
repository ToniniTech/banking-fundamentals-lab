package com.toninitech.banking.account.application;

import com.toninitech.banking.account.domain.BankAccount;
import com.toninitech.banking.learninglabs.transactions.CheckedExceptionRollbackLabService;
import com.toninitech.banking.learninglabs.transactions.SelfInvocationLabService;
import com.toninitech.banking.learninglabs.transactions.TransferReviewRequiredException;
import com.toninitech.banking.shared.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.awt.image.BandCombineOp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * These tests deliberately do not use @Transactional on the test methods.
 * Each assertion observes the commit or rollback performed by the service.
 */
@SpringBootTest
@ActiveProfiles("postgres")
@Testcontainers(disabledWithoutDocker = true)
class TransferTransactionIntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TransferApplicationService transferService;

    @Autowired
    private CheckedExceptionRollbackLabService checkedExceptionLab;

    @Autowired
    private SelfInvocationLabService selfInvocationLab;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void successfulTransferCommitsBothBalanceChanges() {
        BankAccount source = savedAccount("100.00");
        BankAccount target = savedAccount("20.00");

        transferService.transfer(new TransferCommand(
                source.id(),
                target.id(),
                Money.of("30.00", "USD")));

        BankAccount persistedSource = accountRepository.findById(source.id())
                        .orElseThrow();
        BankAccount persistedTarget = accountRepository.findById(target.id())
                        .orElseThrow();

        assertEquals(persistedSource.balance(), Money.of("70.00", "USD"));
        assertEquals(persistedTarget.balance(), Money.of("50.00", "USD"));

    }

    @Test
    void runtimeExceptionRollsBackTheEarlierDebit() {
        BankAccount source = savedAccount("100.00");
        BankAccount blockedTarget = savedAccount("20.00");
        blockedTarget.block();
        accountRepository.save(blockedTarget);

        assertThrows(RuntimeException.class, () -> transferService.transfer(new TransferCommand(
                source.id(),
                blockedTarget.id(),
                Money.of("30.00", "USD"))));

        assertBalance(source.id(), "100.00");
        assertBalance(blockedTarget.id(), "20.00");
    }

    @Test
    void checkedExceptionCommitsByDefault() {
        BankAccount account = BankAccount.open(UUID.randomUUID(),Money.of("100.00", "USD"));
        accountRepository.save(account);

        assertThrows(TransferReviewRequiredException.class, ()->
                checkedExceptionLab.debitThenThrowCheckedException(
                        account.id(), Money.of("75.00", "USD")));

        BankAccount persistedAccount = accountRepository.findById(account.id()).orElseThrow();
        assertEquals(persistedAccount.balance(), Money.of("25.00", "USD"));

    }
    @Test
    void rollbackForMakesTheCheckedExceptionRollBack() {
        BankAccount account = savedAccount("10.00");

        assertThrows(TransferReviewRequiredException.class, ()->
                checkedExceptionLab.debitThenRollbackForCheckedException(
                        account.id(), Money.of("5.00", "USD")
                ));

        BankAccount persistedAccount =
                accountRepository.findById(account.id()).orElseThrow();

        assertEquals(
                Money.of("10.00", "USD"),
                persistedAccount.balance()
        );
    }

    @Test
    void transactionalMethodCalledThroughTheProxyStartsATransaction() {
        assertTrue(selfInvocationLab.transactionIsActiveWhenCalledThroughProxy());
    }

    @Test
    void selfInvocationDoesNotCrossTheTransactionalProxy() {
        assertFalse(selfInvocationLab.callsTransactionalMethodInternally());
    }

    private BankAccount savedAccount(String balance) {
        return accountRepository.save(BankAccount.open(
                UUID.randomUUID(),
                Money.of(balance, "USD")));
    }

    private void assertBalance(UUID accountId, String expected) {
        BankAccount account = accountRepository.findById(accountId).orElseThrow();
        assertEquals(Money.of(expected, "USD"), account.balance());
    }
}
