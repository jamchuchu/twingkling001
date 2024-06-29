package com.sparta.twingkling001.order.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderState {
    ORDER_COMPLETED("결제완료"),
    PREPARING_FOR_SHIPMENT("배송준비"),
    SHIPPED("배송중"),
    DELIVERY_COMPLETED("배송완료");

    private final String orderState;
}
