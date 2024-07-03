package com.sparta.twingkling001.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CartDetailReqDto {
    private Long cartId;
    private Long productDetailId;
    private Long quantity;
    private Boolean presentSaleYn;
}
