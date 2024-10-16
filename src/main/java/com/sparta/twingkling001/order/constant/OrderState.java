package com.sparta.twingkling001.order.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderState {
    TRY_ORDER("주문중"),
    ORDER_COMPLETED("주문완료"),
    PAY_COMPLETED("결제완료"),
    PREPARING_FOR_SHIPMENT("배송준비"),
    SHIPPED("배송중"),
    DELIVERY_COMPLETED("배송완료"),

    CANSEL("취소완료"),

    FAILED("주문 실패");

    private final String orderState;
}
