package com.sparta.twingkling001.order.dto.response;

import com.sparta.twingkling001.order.constant.OrderState;
import com.sparta.twingkling001.order.dto.request.OrderReqDto;
import com.sparta.twingkling001.order.entity.Order;
import com.sparta.twingkling001.order.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderRespDto {
    private Long orderId;
    private Long memberId;
    private Long receiveAddressId;
    private String receiveName;
    private String receivePhoneNumber;
    private LocalDateTime paymentDate;
    private LocalDateTime deliverDate;
    private LocalDateTime createAt;
    private Boolean deletedYn;
    private OrderState orderState;
    private Long totalPrice;
    private Long deliverFee;
    private Long payId;

    private List<OrderDetailRespDto> orderDetails;

    public void setOrderDetails(List<OrderDetailRespDto> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public static OrderRespDto from(Order order){
        return OrderRespDto.builder()
                .orderId(order.getOrderId())
                .memberId(order.getMemberId())
                .receiveAddressId(order.getReceiveAddressId())
                .receiveName(order.getReceiveName())
                .receivePhoneNumber(order.getReceivePhoneNumber())
                .paymentDate(order.getPaymentDate())
                .deliverDate(order.getDeliverDate())
                .createAt(order.getCreateAt())
                .deletedYn(order.getDeletedYn())
                .orderState(order.getOrderState())
                .totalPrice(order.getTotalPrice())
                .deliverFee(order.getDeliverFee())
                .payId(order.getPayId())
                .build();
    }
}
