package com.sparta.twingkling001.order.dto.response;

import com.sparta.twingkling001.order.entity.Order;
import com.sparta.twingkling001.order.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDetailRespDto {
    private Long orderDetailId;
    private Order order;
    private Long productId;
    private Long quantity;
    private Boolean deletedYn;
    private Long price;

    public static OrderDetailRespDto from(OrderDetail orderDetail){
        return OrderDetailRespDto.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .order(orderDetail.getOrder())
                .productId(orderDetail.getProductId())
                .quantity(orderDetail.getQuantity())
                .deletedYn(orderDetail.getDeletedYn())
                .price(orderDetail.getPrice())
                .build();
    }
}
