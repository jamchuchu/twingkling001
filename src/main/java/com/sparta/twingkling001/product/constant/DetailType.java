package com.sparta.twingkling001.product.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DetailType {
    COLOR("색상"),
    SIZE("크기");

    private String detailType;
}
