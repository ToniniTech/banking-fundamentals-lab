package com.toninitech.banking.learninglabs;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnprotectedAccountInvariantLabTest {

    @Test
    void anAnemicAccountAllowsAnImpossibleNegativeBalance() {
        UnprotectedAccount account = new UnprotectedAccount(new BigDecimal("50.00"));

        account.debit(new BigDecimal("80.00"));

        assertEquals(new BigDecimal("-30.00"), account.balance());
    }

    /** Variante deliberadamente incorrecta, aislada en test. */
    private static final class UnprotectedAccount {
        private BigDecimal balance;

        private UnprotectedAccount(BigDecimal balance) {
            this.balance = balance;
        }

        private void debit(BigDecimal amount) {
            balance = balance.subtract(amount);
        }

        private BigDecimal balance() {
            return balance;
        }
    }
}

