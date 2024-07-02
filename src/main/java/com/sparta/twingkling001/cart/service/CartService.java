package com.sparta.twingkling001.cart.service;

import com.sparta.twingkling001.cart.dto.request.CartDetailReqDto;
import com.sparta.twingkling001.cart.dto.response.CartDetailRespDto;
import com.sparta.twingkling001.cart.dto.response.CartRespDto;
import com.sparta.twingkling001.cart.entity.Cart;
import com.sparta.twingkling001.cart.entity.CartDetail;
import com.sparta.twingkling001.cart.repository.CartDetailRepository;
import com.sparta.twingkling001.cart.repository.CartRepository;
import com.sparta.twingkling001.product.controller.ProductController;
import com.sparta.twingkling001.product.entity.Product;
import com.sparta.twingkling001.product.entity.ProductDetail;
import com.sparta.twingkling001.product.repository.ProductDetailRepository;
import com.sparta.twingkling001.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final EntityManager entityManager;
    private final ProductDetailRepository productDetailRepository;


    public CartRespDto addEmptyCart(Long memberId) {
        Cart cart = Cart.builder()
                .memberId(memberId)
                .deletedYn(false)
                .build();
        return CartRespDto.from(cartRepository.save(cart));
    }

    @Transactional
    public void deleteCart(Long memberId) {
        Cart cart = cartRepository.findCartByMemberId(memberId);
        if(cart == null){
            throw new NullPointerException("해당 데이터가 없습니다");
        }
        if(cart.getDeletedYn()){
            throw new IllegalArgumentException("이미 삭제된 데이터 입니다");
        }
        cart.setDeletedYn(true);
    }

    @Transactional
    public CartDetailRespDto addCartDetail(CartDetailReqDto reqDto) {
        CartDetail cd = CartDetail.from(reqDto);
        CartDetail alreadyExist = cartDetailRepository.findCartDetailByProductDetailIdAndCartId(reqDto.getProductDetailId(), reqDto.getCartId());
        //이미 같은게 있으면 숫자 up
        if(alreadyExist != null){
            alreadyExist.setQuantity(alreadyExist.getQuantity()+reqDto.getQuantity());
            return CartDetailRespDto.from(alreadyExist);
        }
        ProductDetail productDetail = productDetailRepository.findProductDetailByProductDetailId(reqDto.getProductDetailId());
        //productId가 없으면 error
        if(productDetail == null){
            throw new NullPointerException("선택하신 상품이 없습니다");
        }
        return CartDetailRespDto.from(cartDetailRepository.save(cd));
    }

    public List<CartDetail> getCartDetails(Long cartId) {
        return cartDetailRepository.getCartDetailsByCartId(cartId);
    }

    public void updateProductQuantity(Long cartDetailId, Long quantity) {
        CartDetail cartDetail = entityManager.find(CartDetail.class, cartDetailId);
        cartDetail.setQuantity(quantity);
    }


    @Transactional
    public void deleteCartDetail(Long cartDetailId) {
        cartDetailRepository.deleteCartDetailByCartDetailId(cartDetailId);
    }

    //판매 상태 변경 시
    @Transactional
    public void updatePresentSaleYn(Long productId, boolean isSale) {
        List<CartDetail> cartDetails = cartDetailRepository.getCartDetailByProductDetailId(productId);
        cartDetails.forEach(detail->{
            if(isSale){
                detail.setPresentSaleYn(true);
                return;
            }
            detail.setPresentSaleYn(false);
        });
    }
}
