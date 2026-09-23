package com.toninitech.banking.learninglabs.spring;

import com.toninitech.banking.account.application.AccountApplicationService;
import com.toninitech.banking.account.application.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class MissingBeanLabTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(ServiceOnlyConfiguration.class);

    @Test
    void contextFailsWhenARequiredDependencyHasNoBean() {
        contextRunner.run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure())
                    .hasMessageContaining("AccountRepository");
        });
    }

    @Configuration(proxyBeanMethods = false)
    static class ServiceOnlyConfiguration {

        @Bean
        AccountApplicationService accountApplicationService(AccountRepository repository) {
            return new AccountApplicationService(repository);
        }
    }
}

