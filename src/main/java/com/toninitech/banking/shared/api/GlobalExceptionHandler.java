package com.toninitech.banking.shared.api;

import com.toninitech.banking.account.application.AccountNotFoundException;
import com.toninitech.banking.account.domain.AccountUnavailableException;
import com.toninitech.banking.account.domain.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    ResponseEntity<ApiError> handleNotFound(AccountNotFoundException exception) {
        return response(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    ResponseEntity<ApiError> handleInsufficientFunds(InsufficientFundsException exception) {
        return response(HttpStatus.UNPROCESSABLE_ENTITY, "INSUFFICIENT_FUNDS", exception.getMessage());
    }

    @ExceptionHandler(AccountUnavailableException.class)
    ResponseEntity<ApiError> handleUnavailable(AccountUnavailableException exception) {
        return response(HttpStatus.CONFLICT, "ACCOUNT_UNAVAILABLE", exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, ArithmeticException.class})
    ResponseEntity<ApiError> handleInvalidOperation(RuntimeException exception) {
        return response(HttpStatus.BAD_REQUEST, "INVALID_OPERATION", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fields.putIfAbsent(error.getField(), error.getDefaultMessage()));
        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_REQUEST",
                "Request validation failed",
                Map.copyOf(fields));
        return ResponseEntity.badRequest().body(body);
    }

    private static ResponseEntity<ApiError> response(
            HttpStatus status,
            String code,
            String message) {
        return ResponseEntity.status(status)
                .body(new ApiError(status.value(), code, message));
    }
}

