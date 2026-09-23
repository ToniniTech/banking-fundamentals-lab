package com.toninitech.banking.shared.money;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLOutput;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;



public class Money1Test {

    @Test
    public void createInstancesDoNotAcceptNegative(){
        assertThrows(IllegalArgumentException.class, () ->
            Money.of("-100.00", "USD"),
            "La prueba falló: El sistema permitió crear una instancia con saldo negativo."
        );

    }

    @Test
    public void addReturnsNewInstancesWithoutModifyingTheOriginalOne(){
        Money account = Money.of("100.00", "USD");

        Money result = account.add(Money.of("100.00", "USD"));
        assertNotEquals(account, result);

    }

    @Test
    public void subtractReturnNewInstanceWithoutModifyingTheOriginalOne(){
        Money account = Money.of("100.00", "USD");

        Money result = account.subtract(Money.of("100.00", "USD"));
        assertNotEquals(account, result);

    }

    @Test
    public void substractMethodNeverAcceptsNegativeArguments(){
        Money account = Money.of("100.00", "USD");

        assertThrows(IllegalArgumentException.class, ()->
                 account.subtract(Money.of("-30.00", "USD")));

    }





    @Test
    public void substractNeverReturnAnegativeResult(){
        Money account = Money.of("100.00", "USD");
        Money account1 = Money.of("101.00", "USD");

        assertThrows(IllegalArgumentException.class, ()-> account.subtract(account1));

    }

    @Test
    public void rejectsOperationsWithDifferentCurrencies(){
        Money dollars = Money.of("100.00", "USD");
        Money euros = Money.of("100.00", "EUR");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> dollars.add(euros));

        assertTrue(exception.getMessage().contains("Currency mismatch"));



    }

    @Test
    public void normalizesAmountsWithNoDecimals(){
        Money account = Money.of("100", "USD");

        assertEquals(new BigDecimal("100.00"), account.amount());

    }

    @Test
    public void moneyInstancesDoNotAcceptNulls(){
        Money account = new Money(BigDecimal.valueOf(100), Currency.getInstance("USD"));
        System.out.println(account);
    }

}
