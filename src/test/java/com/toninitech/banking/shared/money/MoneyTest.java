package com.toninitech.banking.shared.money;

import com.toninitech.banking.account.domain.BankAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLOutput;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void createNewInstanceWithMoneyAreInmutable(){
        Money first = Money.of("100.00", "USD");
        Money second = Money.of("100.00", "USD");

        Money result = first.add(second);

        assertEquals(first, second);
        assertNotSame(first, result);


    }

    @Test
    void handleDifferentCurrenceWithCurrency(){
        Currency clp = Currency.getInstance("CLP");
        Currency usd = Currency.getInstance("USD");
        System.out.println(clp.getDisplayName());
        System.out.println(clp.getDisplayName());
        System.out.println(clp.getSymbol());
        System.out.println(clp.getDefaultFractionDigits());

        System.out.println(usd.getDisplayName());
        System.out.println(usd.getDisplayName());
        System.out.println(usd.getSymbol());
        System.out.println(usd.getDefaultFractionDigits());




    }

    @Test
    void addReturnsNewInstancesWithoutModyfingOriginalMomey() {
        Money originalAccount = Money.of("100", "USD");
        Money newInstanceAccount = originalAccount.add(Money.of("200.00", "USD"));

        assertEquals(Money.of("100", "USD"), originalAccount);
        assertEquals(Money.of("300", "USD"), newInstanceAccount);
        assertNotSame(originalAccount, newInstanceAccount);

    }


    @Test
    void normalizesAmountToCurrencyScale() {
        Money money = Money.of("10", "USD");

        assertEquals(new BigDecimal("10.00"), money.amount());
        assertEquals(Currency.getInstance("USD"), money.currency());
    }

    @Test
    void rejectsImplicitRounding() {
        assertThrows(ArithmeticException.class, () -> Money.of("10.001", "USD"));
    }

    @Test
    void supportsExplicitRoundingBeforeConstruction() {
        BigDecimal rounded = new BigDecimal("10.005").setScale(2, RoundingMode.HALF_EVEN);

        Money money = new Money(rounded, Currency.getInstance("USD"));

        assertEquals(new BigDecimal("10.00"), money.amount());
    }

    @Test
    void returnsNewValueWhenAdding() {
        Money original = Money.of("10.00", "USD");

        Money result = original.add(Money.of("2.50", "USD"));

        assertEquals(Money.of("10.00", "USD"), original);
        assertEquals(Money.of("12.50", "USD"), result);
        assertNotSame(original, result);
    }

    @Test
    void rejectsOperationsWithDifferentCurrencies() {
        Money dollars = Money.of("10.00", "USD");
        Money euros = Money.of("5.00", "EUR");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dollars.add(euros));

        assertTrue(exception.getMessage().contains("Currency mismatch"));
    }

    @Test
    void rejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("-0.01", "USD"));
    }


}

