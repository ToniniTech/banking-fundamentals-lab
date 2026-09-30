package com.toninitech.banking.account.domain;

import com.toninitech.banking.shared.money.Money;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de dominio identificada por un UUID estable.
 *
 * <p>El saldo puede cambiar, pero no participa en equals/hashCode. De esa forma
 * una operacion legitima no cambia el bucket de la cuenta cuando esta dentro de
 * una coleccion basada en hashing.</p>
 */
public final class BankAccount {

    private final UUID id;
    private Money balance;
    private AccountStatus status;

    public BankAccount(UUID id, Money openingBalance, AccountStatus status) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.balance = Objects.requireNonNull(openingBalance, "balance is requireed");
        this.status = Objects.requireNonNull(status, "status is required");
    }

    public static BankAccount open(UUID id, Money openingBalance){
        return new BankAccount(id, openingBalance, AccountStatus.ACTIVE);
    }

    public void debit(Money amount){
        requirePositive(amount);
        requireActive();
        if (balance.isLessThan(amount)){
            throw new InsufficientFundsException(id, balance, amount);
        }
        this.balance = balance.subtract(amount);
    }

    public void credit(Money amount) {
        requireActive();
        requirePositive(amount);
        balance = balance.add(amount);
    }

    public void block(){
        status = AccountStatus.BLOCKED;
    }

    public UUID id() {
        return id;
    }

    public Money balance() {
        return balance;
    }

    public AccountStatus status() {
        return status;
    }

    private void requireActive() {
        if (status != AccountStatus.ACTIVE){
            throw new AccountUnavailableException(id, status);
        }
    }

    private static void requirePositive(Money amount){
        Objects.requireNonNull(amount, "amount required");
        if (!amount.isPositive()){
            throw new IllegalArgumentException();
        }
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BankAccount account)) {
            return false;
        }
        return id.equals(account.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "BankAccount[id=%s, balance=%s %s, status=%s]"
                .formatted(id, balance.amount(), balance.currency().getCurrencyCode(), status);
    }
}

