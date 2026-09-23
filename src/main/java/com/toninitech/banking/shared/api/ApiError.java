package com.toninitech.banking.shared.api;

import java.util.Map;

public record ApiError(
        int status,
        String code,
        String message,
        Map<String, String> fieldErrors) {

    public ApiError(int status, String code, String message) {
        this(status, code, message, Map.of());
    }
}

