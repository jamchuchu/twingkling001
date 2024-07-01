package com.sparta.twingkling001.cart.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CartDetailReqDto {
    private Long cartId;
    private Long productId;
    private Long quantity;
    private Boolean presentSaleYn;
}
