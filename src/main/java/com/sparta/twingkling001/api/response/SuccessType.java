package com.sparta.twingkling001.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessType {
    SUCCESS_CREATE(200, "Operation successful"),
    SEND_EMAIL(200, "이메일을 보냈습니다"),
    CREATED(201, "Resource created");


    private final int httpStatusCode;
    private final String message;
}