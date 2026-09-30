package com.toninitech.banking.learninglabs.transactions;

import com.toninitech.banking.account.application.AccountNotFoundException;
import com.toninitech.banking.account.application.AccountRepository;
import com.toninitech.banking.account.domain.BankAccount;
import com.toninitech.banking.shared.money.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * A deliberately narrow lab: checked exceptions do not trigger rollback by
 * default, while rollbackFor makes that decision explicit.
 */
@Service
public class CheckedExceptionRollbackLabService {

    private final AccountRepository accountRepository;

    public CheckedExceptionRollbackLabService(AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository is required");
    }

    @Transactional
    public void debitThenThrowCheckedException(UUID accountId, Money amount)
            throws TransferReviewRequiredException {
        debitAndSave(accountId, amount);
        throw new TransferReviewRequiredException("Manual review required after debit");
    }

    @Transactional(rollbackFor = TransferReviewRequiredException.class)
    public void debitThenRollbackForCheckedException(UUID accountId, Money amount)
            throws TransferReviewRequiredException {
        debitAndSave(accountId, amount);
        throw new TransferReviewRequiredException("Manual review required after debit");
    }

    private void debitAndSave(UUID accountId, Money amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.debit(amount);
        accountRepository.save(account);
    }
}
