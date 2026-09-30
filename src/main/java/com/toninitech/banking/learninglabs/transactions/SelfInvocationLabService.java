package com.toninitech.banking.learninglabs.transactions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * Shows that Spring applies @Transactional through a proxy, not by modifying
 * the body of the annotated method itself.
 */
@Slf4j
@Service
public class SelfInvocationLabService {

    public boolean callsTransactionalMethodInternally(){
        boolean active = transactionIsActiveWhenCalledThroughProxy();
        return active;
    }

    @Transactional
    public boolean transactionIsActiveWhenCalledThroughProxy() {
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("¿Hay transacción activa?: {}", active);
        return active;

    }
}
