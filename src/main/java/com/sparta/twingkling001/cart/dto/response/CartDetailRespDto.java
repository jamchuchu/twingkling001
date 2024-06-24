package com.sparta.twingkling001.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CartDetailRespDto {
    private Long cartDetailId;
    private Long cartId;
    private Long productId;
    private Long quantity;
    private Boolean presentSaleYn;
}
