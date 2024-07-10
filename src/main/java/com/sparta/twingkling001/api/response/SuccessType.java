package com.sparta.twingkling001.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessType {
    //일반
    SUCCESS_CREATE(201, "데이터가 추가 되었습니다"),
    SUCCESS(200 ,"작업이 성공적으로 수행 되었습니다"),


    SEND_EMAIL(200, "이메일을 보냈습니다"),
    LOGIN_SUCCESS(200, "로그인 성공"),
    LOGOUT_SUCCESS(200, "로그아웃 성공");


    private final int httpStatusCode;
    private final String message;
}