package com.toninitech.banking.learninglabs;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MutableHashSetKeyLabTest {

    @Test
    void mutatingHashRelevantStateMakesAnEntryPracticallyUnreachable() {
        MutableCustomerKey key = new MutableCustomerKey("customer-1");
        HashSet<MutableCustomerKey> keys = new HashSet<>();
        keys.add(key);

        key.changeValue("customer-2");

        assertFalse(keys.contains(key));
        assertEquals(1, keys.size());
        assertSame(key, keys.iterator().next());
    }

    private static final class MutableCustomerKey {
        private String value;

        private MutableCustomerKey(String value) {
            this.value = value;
        }

        private void changeValue(String newValue) {
            this.value = newValue;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof MutableCustomerKey key && Objects.equals(value, key.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}

