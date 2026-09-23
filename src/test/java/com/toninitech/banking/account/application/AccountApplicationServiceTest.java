package com.toninitech.banking.account.application;

import com.toninitech.banking.account.infrastructure.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountApplicationServiceTest {

    private AccountApplicationService service;

    @BeforeEach
    void setUp() {
        service = new AccountApplicationService(new InMemoryAccountRepository());
    }

    @Test
    void opensAndFindsAnAccountWithoutStartingSpring() {
        AccountView opened = service.openAccount(
                new OpenAccountCommand(new BigDecimal("100.00"), "USD"));

        AccountView found = service.findAccount(opened.id());

        assertEquals(opened, found);
        assertEquals(new BigDecimal("100.00"), found.balance());
    }

    @Test
    void delegatesDebitRulesToTheDomain() {
        AccountView opened = service.openAccount(
                new OpenAccountCommand(new BigDecimal("100.00"), "USD"));

        AccountView debited = service.debit(
                opened.id(),
                new MoneyOperationCommand(new BigDecimal("35.50"), "USD"));

        assertEquals(new BigDecimal("64.50"), debited.balance());
    }

    @Test
    void reportsAnUnknownAccount() {
        UUID unknownId = UUID.fromString("90000000-0000-0000-0000-000000000001");

        assertThrows(AccountNotFoundException.class, () -> service.findAccount(unknownId));
    }
}

