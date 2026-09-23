package com.toninitech.banking.learninglabs;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalConstructionLabTest {

    @Test
    void constructingFromBinaryDoublePreservesItsApproximation() {
        BigDecimal fromDoubleConstructor = new BigDecimal(0.1);
        BigDecimal fromText = new BigDecimal("0.1");
        BigDecimal fromValueOf = BigDecimal.valueOf(0.1);

        assertNotEquals(fromText, fromDoubleConstructor);
        assertEquals(fromText, fromValueOf);
        assertTrue(fromDoubleConstructor.toPlainString().length() > fromText.toPlainString().length());
    }
}

