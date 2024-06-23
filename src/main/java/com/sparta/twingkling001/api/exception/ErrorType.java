package com.sparta.twingkling001.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
//    INVALID_PERMISSION(org.springframework.http.HttpStatus.UNAUTHORIZED, "Permission is invalid");
    INVALID_TOKEN(org.springframework.http.HttpStatus.UNAUTHORIZED, "토큰 불일치");
    private final HttpStatus httpStatus;
    private final String message;
}
