package com.toninitech.banking.account.domain;

import com.toninitech.banking.shared.money.Money;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");

    @Test
    void debitsAvailableFunds() {
        BankAccount account = BankAccount.open(ACCOUNT_ID, Money.of("100.00", "USD"));

        account.debit(Money.of("50.00", "USD"));

        assertEquals(Money.of("50", "USD"), account.balance());

    }

        @Test
    void creditsFunds() {
        BankAccount account = BankAccount.open(ACCOUNT_ID, Money.of("100.00", "USD"));

        account.credit(Money.of("45.00", "USD"));

        assertEquals(Money.of("145.00", "USD"), account.balance());
    }

    @Test
    void insufficientFundsDoNotChangeBalance() {
        BankAccount account = BankAccount.open(ACCOUNT_ID, Money.of("100.00", "USD"));

        assertThrows(
                InsufficientFundsException.class,
                () -> account.debit(Money.of("100.01", "USD")));

        assertEquals(Money.of("100.00", "USD"), account.balance());
    }

    @Test
    void zeroAmountIsNotAValidOperation() {
        BankAccount account = BankAccount.open(ACCOUNT_ID, Money.of("100.00", "USD"));

        assertThrows(
                IllegalArgumentException.class,
                () -> account.credit(Money.of("0.00", "USD")));
    }

    @Test
    void blockedAccountCannotBeModified() {
        BankAccount account = BankAccount.open(ACCOUNT_ID, Money.of("100.00", "USD"));
        account.block();

        assertThrows(AccountUnavailableException.class, () ->
                account.debit(Money.of("120.00", "USD")));

    }

    @Test
    void equalityUsesStableIdentityRatherThanMutableState() {
        BankAccount firstRepresentation = BankAccount.open(ACCOUNT_ID, Money.of("100.00", "USD"));
        BankAccount secondRepresentation = BankAccount.open(ACCOUNT_ID, Money.of("500.00", "USD"));
        HashSet<BankAccount> accounts = new HashSet<>();
        accounts.add(firstRepresentation);

        firstRepresentation.credit(Money.of("25.00", "USD"));

        assertEquals(firstRepresentation, secondRepresentation);
        assertEquals(firstRepresentation.hashCode(), secondRepresentation.hashCode());
        assertTrue(accounts.contains(firstRepresentation));
    }
}

