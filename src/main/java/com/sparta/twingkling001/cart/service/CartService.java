package com.sparta.twingkling001.cart.service;

import com.sparta.twingkling001.cart.dto.request.CartDetailReqDto;
import com.sparta.twingkling001.cart.dto.response.CartDetailRespDto;
import com.sparta.twingkling001.cart.dto.response.CartRespDto;
import com.sparta.twingkling001.cart.entity.Cart;
import com.sparta.twingkling001.cart.entity.CartDetail;
import com.sparta.twingkling001.cart.repository.CartDetailRepository;
import com.sparta.twingkling001.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    public CartRespDto addEmptyCart(Long memberId) {
        Cart cart = Cart.builder()
                .memberId(memberId)
                .deletedYn(false)
                .build();
        cart = cartRepository.save(cart);
        return CartRespDto.builder()
                .cartId(cart.getCartId())
                .memberId(cart.getMemberId())
                .deletedYn(cart.getDeletedYn())
                .build();
    }

    public void deleteCart(Long memberId) {
        cartRepository.deleteByMemberId(memberId);
    }

    public CartDetailRespDto addCartDetail(Long memberId, CartDetailReqDto reqDto) {
        CartDetail cd = CartDetail.builder()
                .cartId(reqDto.getCartId())
                .productId(reqDto.getProductId())
                .quantity(reqDto.getQuantity())
                .presentSaleYn(true)
                .build();
        cd = cartDetailRepository.save(cd);
        return CartDetailRespDto.builder()
                .cartDetailId(cd.getCartDetailId())
                .cartId(cd.getCartId())
                .productId(cd.getProductId())
                .quantity(cd.getQuantity())
                .presentSaleYn(cd.getPresentSaleYn())
                .build();
    }

    public List<CartDetail> getCartDetails(Long cartId) {
        return cartDetailRepository.getCartDetailsByCartId(cartId);
    }

    public void updateProductQuantity(Long cartDetailId, Long quantity) {
        cartDetailRepository.updateProductQuantity(cartDetailId, quantity);
    }

    public void deleteCartDetail(Long cartDetailId) {
        cartDetailRepository.deleteCartDetailByCartDetailId(cartDetailId);
    }

    public void updatePresentSaleYn(Long productId, boolean isSale) {
        if(isSale){
            cartDetailRepository.updateProductSateTrue(productId);
            return;
        }
            cartDetailRepository.updateProductSateFalse(productId);
    }
}
