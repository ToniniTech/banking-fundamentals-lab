package com.toninitech.banking.account.infrastructure;

import com.toninitech.banking.account.application.AccountRepository;
import com.toninitech.banking.account.domain.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAccountRepository implements AccountRepository {

    private final ConcurrentMap<UUID, BankAccount> accounts = new ConcurrentHashMap<>();

    @Override
    public BankAccount save(BankAccount account) {
        Objects.requireNonNull(account, "account is required");
        accounts.put(account.id(), account);
        return account;
    }

    @Override
    public Optional<BankAccount> findById(UUID accountId) {
        Objects.requireNonNull(accountId, "accountId is required");
        return Optional.ofNullable(accounts.get(accountId));
    }
}

