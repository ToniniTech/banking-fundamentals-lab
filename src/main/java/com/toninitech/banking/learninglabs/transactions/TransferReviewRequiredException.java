package com.toninitech.banking.learninglabs.transactions;

/**
 * Deliberately checked exception used only to demonstrate Spring's default
 * rollback rule in Unit 4.
 */
public class TransferReviewRequiredException extends Exception {

    public TransferReviewRequiredException(String message) {
        super(message);
    }
}
