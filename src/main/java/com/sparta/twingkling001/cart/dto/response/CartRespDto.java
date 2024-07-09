package com.sparta.twingkling001.cart.dto.response;

import com.sparta.twingkling001.cart.entity.Cart;
import com.sparta.twingkling001.cart.entity.CartDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class CartRespDto {
    private Long cartId;
    private Long memberId;
    private Boolean deletedYn;

    private List<CartDetailRespDto> details;

    public static CartRespDto from(Cart cart){
        return CartRespDto.builder()
                .cartId(cart.getCartId())
                .memberId(cart.getMemberId())
                .deletedYn(cart.getDeletedYn())
                .details(cart.getDetails().stream()
                        .map(CartDetailRespDto::from)
                        .toList()
                )
                .build();
    }
}
