package com.sparta.twingkling001.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class CartRespDto {
    private Long cartId;
    private Long memberId;
    private Boolean deletedYn;
}
