package com.sparta.twingkling001.cart.dto.response;

import com.sparta.twingkling001.cart.entity.Cart;
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

    public static CartRespDto from(Cart cart){
        return CartRespDto.builder()
                .cartId(cart.getCartId())
                .memberId(cart.getMemberId())
                .deletedYn(cart.getDeletedYn())
                .build();
    }
}
