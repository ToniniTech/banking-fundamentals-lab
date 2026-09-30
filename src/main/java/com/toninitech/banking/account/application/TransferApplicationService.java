package com.toninitech.banking.account.application;

import com.toninitech.banking.account.domain.BankAccount;
import com.toninitech.banking.shared.money.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * The transaction boundary for a money transfer.
 *
 * <p>The domain still owns debit and credit rules. This service owns the
 * all-or-nothing boundary around both changes.</p>
 */
@Service
public class TransferApplicationService {

    private final AccountRepository accountRepository;

    public TransferApplicationService(AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository is required");
    }

    @Transactional
    public void transfer(TransferCommand command){
        Objects.requireNonNull(command, "command is required");

        BankAccount source = findRequired(command.sourceAccountId());
        BankAccount target = findRequired(command.targetAccountId());

        source.debit(command.amount());
        accountRepository.save(source);

        target.credit(command.amount());
        accountRepository.save(target);

    }

    private BankAccount findRequired(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
