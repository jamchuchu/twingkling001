package com.sparta.twingkling001.order.dto.request;

import com.sparta.twingkling001.order.constant.OrderState;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderReqDto {
    private Long orderId;
    private Long memberId;
    private Long receiveAddressId;
    private String receiveName;
    private String receivePhoneNumber;
    private LocalDateTime paymentDate;
    private LocalDateTime deliverDate;
    private LocalDateTime createAt;
    private Boolean deletedYn;
    @Enumerated
    private OrderState orderState;
    private Long totalPrice;
    private Long deliverFee;
    private Long payId;

    private List<OrderDetailReqDto> orderDetailsList;
}
