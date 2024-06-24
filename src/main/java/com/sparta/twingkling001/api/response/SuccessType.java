package com.sparta.twingkling001.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessType {
    SUCCESS_CREATE(200, "Operation successful"),
    SUCCESS(200, "Operation successful"),
    SEND_EMAIL(200, "이메일을 보냈습니다"),
    LOGIN_SUCCESS(200, "로그인 성공"),
    LOGOUT_SUCCESS(200, "로그아웃 성공");


    private final int httpStatusCode;
    private final String message;
}