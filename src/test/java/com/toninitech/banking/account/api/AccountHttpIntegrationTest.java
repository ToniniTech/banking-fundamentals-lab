package com.toninitech.banking.account.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("in-memory")
class AccountHttpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void accountFlowsThroughControllerServiceRepositoryAndDomain() throws Exception {
        MvcResult openResult = mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "openingBalance": 100.00,
                                  "currency": "USD"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode opened = objectMapper.readTree(openResult.getResponse().getContentAsString());
        String accountId = opened.get("id").asText();

        mockMvc.perform(post("/api/accounts/{accountId}/debits", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 35.50,
                                  "currency": "USD"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(64.50));

        mockMvc.perform(get("/api/accounts/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(64.50));
    }
}
