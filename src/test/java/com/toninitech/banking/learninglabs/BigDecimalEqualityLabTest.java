package com.toninitech.banking.learninglabs;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalEqualityLabTest {

    @Test
    void equalsConsidersScaleWhileCompareToComparesNumericValue() {
        BigDecimal oneDecimal = new BigDecimal("10.0");
        BigDecimal twoDecimals = new BigDecimal("10.00");
        HashSet<BigDecimal> values = new HashSet<>();
        values.add(oneDecimal);
        values.add(twoDecimals);

        assertNotEquals(oneDecimal, twoDecimals);
        assertEquals(0, oneDecimal.compareTo(twoDecimals));
        assertEquals(2, values.size());
    }
}

