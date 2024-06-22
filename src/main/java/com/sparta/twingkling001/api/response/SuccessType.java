package com.sparta.twingkling001.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessType {
    SUCCESS(200, "Operation successful"),
    CREATED(201, "Resource created");

    private final int httpStatusCode;
    private final String message;
}