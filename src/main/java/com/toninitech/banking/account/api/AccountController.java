package com.toninitech.banking.account.api;

import com.toninitech.banking.account.application.AccountApplicationService;
import com.toninitech.banking.account.application.AccountView;
import com.toninitech.banking.account.application.MoneyOperationCommand;
import com.toninitech.banking.account.application.OpenAccountCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountApplicationService accountService;

    public AccountController(AccountApplicationService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> openAccount(
            @Valid @RequestBody OpenAccountRequest request) {
        AccountView opened = accountService.openAccount(
                new OpenAccountCommand(request.openingBalance(), request.currency()));
        AccountResponse response = AccountResponse.from(opened);
        return ResponseEntity
                .created(URI.create("/api/accounts/" + response.id()))
                .body(response);
    }

    @GetMapping("/{accountId}")
    public AccountResponse findAccount(@PathVariable UUID accountId) {
        return AccountResponse.from(accountService.findAccount(accountId));
    }

    @PostMapping("/{accountId}/debits")
    public AccountResponse debit(
            @PathVariable UUID accountId,
            @Valid @RequestBody MoneyOperationRequest request) {
        return AccountResponse.from(accountService.debit(
                accountId,
                new MoneyOperationCommand(request.amount(), request.currency())));
    }

    @PostMapping("/{accountId}/credits")
    public AccountResponse credit(
            @PathVariable UUID accountId,
            @Valid @RequestBody MoneyOperationRequest request) {
        return AccountResponse.from(accountService.credit(
                accountId,
                new MoneyOperationCommand(request.amount(), request.currency())));
    }
}

