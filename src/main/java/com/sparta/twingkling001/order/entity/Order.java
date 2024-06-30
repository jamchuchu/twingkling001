package com.sparta.twingkling001.order.entity;

import com.sparta.twingkling001.order.constant.OrderState;
import com.sparta.twingkling001.order.dto.request.OrderReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    private Long memberId;
    private Long receiveAddressId;
    private String receiveName;
    private String receivePhoneNumber;
    private LocalDateTime paymentDate;
    private LocalDateTime deliverDate;
    private LocalDateTime createAt;
    private Boolean deletedYn;
    @Enumerated(EnumType.STRING)
    private OrderState orderState;
    private Long totalPrice;
    private Long deliverFee;
    private Long payId;

    public void setReceiveAddressId(Long receiveAddressId) {
        this.receiveAddressId = receiveAddressId;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public void setReceivePhoneNumber(String receivePhoneNumber) {
        this.receivePhoneNumber = receivePhoneNumber;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setDeliverDate(LocalDateTime deliverDate) {
        this.deliverDate = deliverDate;
    }

    public void setDeletedYn(Boolean deletedYn) {
        this.deletedYn = deletedYn;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public static Order from(OrderReqDto reqDto){
        return Order.builder()
                .orderId(reqDto.getOrderId())
                .memberId(reqDto.getMemberId())
                .receiveAddressId(reqDto.getReceiveAddressId())
                .receiveName(reqDto.getReceiveName())
                .receivePhoneNumber(reqDto.getReceivePhoneNumber())
                .paymentDate(reqDto.getPaymentDate())
                .deliverDate(reqDto.getDeliverDate())
                .createAt(reqDto.getCreateAt())
                .deletedYn(reqDto.getDeletedYn())
                .orderState(reqDto.getOrderState())
                .totalPrice(reqDto.getTotalPrice())
                .deliverFee(reqDto.getDeliverFee())
                .payId(reqDto.getPayId())
                .build();
    }
}
