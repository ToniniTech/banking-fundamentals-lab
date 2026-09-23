package com.toninitech.banking.account.api;

import com.toninitech.banking.account.application.AccountApplicationService;
import com.toninitech.banking.account.application.AccountNotFoundException;
import com.toninitech.banking.account.application.AccountView;
import com.toninitech.banking.account.application.OpenAccountCommand;
import com.toninitech.banking.account.domain.AccountStatus;
import com.toninitech.banking.shared.api.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(GlobalExceptionHandler.class)
class AccountControllerTest {

    private static final UUID ACCOUNT_ID =
            UUID.fromString("10000000-0000-0000-0000-000000000001");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountApplicationService accountService;

    @Test
    void opensAnAccountThroughHttp() throws Exception {
        when(accountService.openAccount(any(OpenAccountCommand.class)))
                .thenReturn(new AccountView(
                        ACCOUNT_ID,
                        new BigDecimal("100.00"),
                        "USD",
                        AccountStatus.ACTIVE));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "openingBalance": 100.00,
                                  "currency": "USD"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/accounts/" + ACCOUNT_ID))
                .andExpect(jsonPath("$.id").value(ACCOUNT_ID.toString()))
                .andExpect(jsonPath("$.balance").value(100.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void rejectsInvalidInputBeforeCallingTheService() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "openingBalance": -1.00,
                                  "currency": "usd"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.fieldErrors.openingBalance").exists())
                .andExpect(jsonPath("$.fieldErrors.currency").exists());

        verifyNoInteractions(accountService);
    }

    @Test
    void translatesDomainIndependentNotFoundErrorToHttp404() throws Exception {
        when(accountService.findAccount(ACCOUNT_ID))
                .thenThrow(new AccountNotFoundException(ACCOUNT_ID));

        mockMvc.perform(get("/api/accounts/{accountId}", ACCOUNT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ACCOUNT_NOT_FOUND"));
    }
}

