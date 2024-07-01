package com.sparta.twingkling001.product.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SaleState {
    PREPARING("준비중"),
    ON_SALE("판매중"),
    SOLD_OUT("매진"),
    STOP_SALE("판매 종료");

    private String saleState;
}
