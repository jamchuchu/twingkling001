package com.sparta.twingkling001.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderReceiveReqDto {
    private Long receiveAddressId;
    private String receiveName;
    private String receivePhoneNumber;


}
