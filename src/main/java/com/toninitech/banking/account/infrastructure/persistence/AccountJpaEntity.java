package com.toninitech.banking.account.infrastructure.persistence;

import com.toninitech.banking.account.domain.AccountStatus;
import com.toninitech.banking.account.domain.BankAccount;
import com.toninitech.banking.shared.money.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bank_account")
public class AccountJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    protected AccountJpaEntity() {
        // Required by JPA. Application code should use fromDomain instead.
    }

    AccountJpaEntity(UUID id, BigDecimal balance, String currencyCode, AccountStatus status) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.balance = Objects.requireNonNull(balance, "balance is required");
        this.currencyCode = Objects.requireNonNull(currencyCode, "currencyCode is required");
        this.status = Objects.requireNonNull(status, "status is required");
    }

    public static AccountJpaEntity fromDomain(BankAccount account){
        Objects.requireNonNull(account, "can not be null");
        return new AccountJpaEntity(
                account.id(),
                account.balance().amount(),
                account.balance().currency().getCurrencyCode(),
                account.status()
        );
    }

    public BankAccount toDomain(){
        return new BankAccount(
                id,
                new Money(balance, Currency.getInstance(currencyCode)),
                status

        );
    }



    public void updateFrom(BankAccount account) {
        Objects.requireNonNull(account, "account is required");
        if (!id.equals(account.id())) {
            throw new IllegalArgumentException("Cannot change account identity");
        }
        balance = account.balance().amount();
        currencyCode = account.balance().currency().getCurrencyCode();
        status = account.status();
    }

    UUID id() {
        return id;
    }

    BigDecimal balance() {
        return balance;
    }

    String currencyCode() {
        return currencyCode;
    }

    AccountStatus status() {
        return status;
    }

    void changeBalanceForLab(BigDecimal newBalance) {
        balance = Objects.requireNonNull(newBalance, "newBalance is required");
    }

    @Override
    public String toString() {
        return "AccountJpaEntity{" +
                "id=" + id +
                ", balance=" + balance +
                ", currencyCode='" + currencyCode + '\'' +
                ", status=" + status +
                '}';
    }
}


//public static AccountJpaEntity fromDomain(BankAccount account) {
//    Objects.requireNonNull(account, "account is required");
//    return new AccountJpaEntity(
//            account.id(),
//            account.balance().amount(),
//            account.balance().currency().getCurrencyCode(),
//            account.status());
//}
//
//public BankAccount toDomain() {
//    return new BankAccount(
//            id,
//            new Money(balance, Currency.getInstance(currencyCode)),
//            status);
//}