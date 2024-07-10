package com.sparta.twingkling001.cart.dto.response;

import com.sparta.twingkling001.cart.entity.CartDetail;
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
    private Long productDetailId;
    private Long quantity;
    private Boolean presentSaleYn;

    public static CartDetailRespDto from(CartDetail detail){
        return CartDetailRespDto.builder()
                .cartId(detail.getCartId())
                .productDetailId(detail.getProductDetailId())
                .quantity(detail.getQuantity())
                .presentSaleYn(detail.getPresentSaleYn())
                .build();
    }
}
