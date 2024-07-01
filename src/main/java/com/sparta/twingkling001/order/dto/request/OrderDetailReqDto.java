package com.sparta.twingkling001.order.dto.request;

import com.sparta.twingkling001.order.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDetailReqDto {
    private Long orderDetailId;
    private Order order;
    private Long productDetailId;
    private Long quantity;
    private Boolean deletedYn;
    private Long price;
}
