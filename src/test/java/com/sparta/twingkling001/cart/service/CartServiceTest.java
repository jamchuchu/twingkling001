package com.sparta.twingkling001.cart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


import com.sparta.twingkling001.api.exception.cart.CartAlreadyExistsException;
import com.sparta.twingkling001.api.exception.general.AlreadyDeletedException;
import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.cart.controller.CartController;
import com.sparta.twingkling001.cart.dto.request.CartDetailReqDto;
import com.sparta.twingkling001.cart.dto.response.CartDetailRespDto;
import com.sparta.twingkling001.cart.dto.response.CartRespDto;
import com.sparta.twingkling001.cart.entity.Cart;
import com.sparta.twingkling001.cart.entity.CartDetail;
import com.sparta.twingkling001.cart.repository.CartRepository;
import com.sparta.twingkling001.cart.repository.CartDetailRepository;
import com.sparta.twingkling001.cart.service.CartService;
import com.sparta.twingkling001.member.repository.MemberRepository;
import com.sparta.twingkling001.product.entity.Product;
import com.sparta.twingkling001.product.entity.ProductDetail;
import com.sparta.twingkling001.product.repository.ProductDetailRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class CartServiceTests {
    @Mock
    private CartRespDto cartRespDto;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartDetailRepository cartDetailRepository;
    @Mock
    private ProductDetailRepository productDetailRepository;
    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(CartDetailRespDto.from(any(CartDetail.class))).thenReturn(null);
    }

    @Test
    @DisplayName("이미 카트가 생성 된 회원 에러 발생")
    void addEmptyCart_Exception() {
        Long memberId = 1L;
        when(cartRepository.existsCartByMemberId(memberId)).thenReturn(true);

        assertThrows(CartAlreadyExistsException.class, () -> {
            cartService.addEmptyCart(memberId);
        });

        verify(cartRepository, times(1)).existsCartByMemberId(memberId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("새 카트 생성완료")
    public void addEmptyCart_Success() throws CartAlreadyExistsException {
        Long memberId = 1L;
        Cart cart = new Cart(1L, 1L, false, new ArrayList<>());

        when(cartRepository.existsCartByMemberId(memberId)).thenReturn(false);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartRespDto actualResponse = cartService.addEmptyCart(memberId);

        assertEquals(CartRespDto.class, actualResponse.getClass());
        assertEquals(1L, actualResponse.getCartId());
        assertEquals(1L, actualResponse.getMemberId());
        assertEquals(false, actualResponse.getDeletedYn());


    }

    @Test
    @DisplayName("카트 조회")
    void getCart_Success() throws Exception {
        Long memberId = 1L;
        Cart cart = new Cart(1L, 1L, false, new ArrayList<>());
        when(cartRepository.findCartByMemberId(memberId)).thenReturn(cart);

        CartRespDto result = cartService.getCart(memberId);

        assertEquals(memberId, result.getMemberId());
    }

    @Test
    @DisplayName("카트 조회- 카트 없음")
    void getCart_Exception() throws Exception {
        Long memberId = 1L;
        when(cartRepository.findCartByMemberId(memberId)).thenReturn(null);
        assertThrows(DataNotFoundException.class, () -> {
            cartService.getCart(memberId);
        });
    }

    @Test
    @DisplayName("카트 삭제")
    void deleteCart_Success() throws Exception {
        Long memberId = 1L;
        Cart cart = new Cart(1L, 1L, false, new ArrayList<>());
        when(cartRepository.findCartByMemberId(memberId)).thenReturn(cart);

        cartService.deleteCart(memberId);

        assertTrue(cart.getDeletedYn());
    }

    @Test
    @DisplayName("카트 삭제 - 카트 없음")
    void deleteCart_NotFound() throws Exception {
        Long memberId = 1L;
        when(cartRepository.findCartByMemberId(memberId)).thenReturn(null);

        assertThrows(DataNotFoundException.class, () -> cartService.deleteCart(memberId));
    }

    @Test
    @DisplayName("카트 삭제 - 이미 삭제됨")
    void deleteCart_AlreadyDeleted() throws Exception {
        Long memberId = 1L;
        Cart cart = new Cart(1L, 1L, true, new ArrayList<>());
        when(cartRepository.findCartByMemberId(memberId)).thenReturn(cart);

        assertThrows(AlreadyDeletedException.class, () -> cartService.deleteCart(memberId));
    }

    @Test
    @DisplayName("카트 상세 추가 - 이미 존재")
    void addCartDetail_AlreadyExist() throws
            Exception {
        CartDetailReqDto reqDto = new CartDetailReqDto(1L, 1L, 5L, true);
        CartDetail existingDetail = new CartDetail(1L, 1L, 1L, 3L, true);
        when(cartDetailRepository.findCartDetailByProductDetailIdAndCartId(reqDto.getProductDetailId(), reqDto.getCartId())).thenReturn(existingDetail);

        CartDetailRespDto result = cartService.addCartDetail(reqDto);

        assertEquals(8, result.getQuantity());
    }





}
