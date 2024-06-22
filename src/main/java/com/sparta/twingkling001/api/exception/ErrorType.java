package com.sparta.twingkling001.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INVALID_PERMISSION(org.springframework.http.HttpStatus.UNAUTHORIZED, "Permission is invalid");

    private final HttpStatus httpStatus;
    private final String message;
}
