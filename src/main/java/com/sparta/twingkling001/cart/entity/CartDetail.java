package com.sparta.twingkling001.cart.entity;

import com.sparta.twingkling001.cart.dto.request.CartDetailReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_detail")
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartDetailId;
    @Column(name = "cart_id", nullable = false)
    private Long cartId;
    private Long productDetailId;
    private Long quantity;
    private Boolean presentSaleYn;

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setPresentSaleYn(Boolean presentSaleYn) {
        this.presentSaleYn = presentSaleYn;
    }

    public static CartDetail from(CartDetailReqDto reqDto){
        return CartDetail.builder()
                .cartId(reqDto.getCartId())
                .productDetailId(reqDto.getProductDetailId())
                .quantity(reqDto.getQuantity())
                .presentSaleYn(reqDto.getPresentSaleYn())
                .build();
    }
}
