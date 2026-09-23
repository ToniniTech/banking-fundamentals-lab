package com.toninitech.banking.learninglabs.spring;

import com.toninitech.banking.account.application.AccountApplicationService;
import com.toninitech.banking.account.application.AccountRepository;
import com.toninitech.banking.account.infrastructure.InMemoryAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
class SpringBeanIdentityLabTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void defaultBeansAreSingletonsAndCanBeRequestedByTheirInterface() {
        AccountApplicationService first = context.getBean(AccountApplicationService.class);
        AccountApplicationService second = context.getBean(AccountApplicationService.class);
        AccountRepository repository = context.getBean(AccountRepository.class);

        assertSame(first, second);
        assertInstanceOf(InMemoryAccountRepository.class, repository);
    }
}

