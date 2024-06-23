package com.sparta.twingkling001.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INVALID_TOKEN(org.springframework.http.HttpStatus.UNAUTHORIZED, "토큰 불일치"),
    INVALID_EMAIL_FORM(org.springframework.http.HttpStatus.UNAUTHORIZED, "이메일 형식이 올바르지 않습니다"),
    INVALID_EMAIL_DUPLE(org.springframework.http.HttpStatus.UNAUTHORIZED, "이메일이 중복 되었습니다"),
    LOGIN_FAIL(org.springframework.http.HttpStatus.UNAUTHORIZED, "로그인 실패");

    private final HttpStatus httpStatus;
    private final String message;
}
