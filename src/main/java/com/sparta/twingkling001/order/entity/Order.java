package com.sparta.twingkling001.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "address_detail_id")
    private Long addressDetailId;

    @Column(name = "receive_name")
    private String receiveName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "deliver_date")
    private LocalDateTime deliverDate;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "deleted_yn")
    private Boolean deletedYn;

    @Column(name = "order_state")
    private String orderState;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "deliver_fee")
    private Long deliverFee;

    @Column(name = "pay_id")
    private Long payId;

}
