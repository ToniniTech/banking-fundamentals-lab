package com.toninitech.banking.shared.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Valor monetario inmutable.
 *
 * <p>La cantidad se normaliza a la escala oficial de la moneda. Se usa
 * {@link RoundingMode#UNNECESSARY} para rechazar silencios de redondeo: el
 * llamador debe decidir explicitamente como redondear antes de crear dinero.</p>
 */
public record Money(BigDecimal amount, Currency currency) implements Comparable<Money> {

    public Money {
        Objects.requireNonNull(amount, "amount is required");
        Objects.requireNonNull(currency, "currency is required");

        int fractionDigits = currency.getDefaultFractionDigits();
        if (fractionDigits < 0) {
            throw new IllegalArgumentException("Currency must define fraction digits: " + currency);
        }

        amount = amount.setScale(fractionDigits, RoundingMode.UNNECESSARY);
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }
    }

    public static Money of(String amount, String currencyCode) {
        Objects.requireNonNull(amount, "amount is required");
        Objects.requireNonNull(currencyCode, "currencyCode is required");
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other){
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);

    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        if (amount.compareTo(other.amount) < 0) {
            throw new IllegalArgumentException("Resulting money amount cannot be negative");
        }
        return new Money(amount.subtract(other.amount), currency);
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public boolean isLessThan(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) < 0;
    }

    @Override
    public int compareTo(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount);
    }

    private void requireSameCurrency(Money other){
        Objects.requireNonNull(other, "Currency is required");
        if (!currency.equals(other.currency)){
            throw new IllegalArgumentException("Currency mismatch: " + currency.getCurrencyCode()
                    + " and " + other.currency.getCurrencyCode());


        }
    }


    }








