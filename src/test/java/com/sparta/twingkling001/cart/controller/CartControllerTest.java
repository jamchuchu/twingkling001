package com.sparta.twingkling001.cart.controller;


import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sparta.twingkling001.cart.dto.response.CartRespDto;
import com.sparta.twingkling001.cart.entity.Cart;
import com.sparta.twingkling001.cart.entity.CartDetail;
import com.sparta.twingkling001.cart.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = CartController.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartService cartService;


    @Test
    @WithMockUser
    @DisplayName("회원가입 시 빈카트 생성")
    void addEmptyCart() throws Exception{
        // given
        Long memberId = 1L;
        CartRespDto cart = new CartRespDto(1L, 1L, false, new ArrayList<>());
        when(cartService.addEmptyCart(memberId)).thenReturn(cart);
        // when
        ResultActions result = mockMvc.perform(post("/api/cart/{memberId}", memberId)
                .with(csrf())  // CSRF 토큰 추가
                .contentType(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.cartId").value(1))
                .andExpect(jsonPath("$.data.memberId").value(1))
                .andExpect(jsonPath("$.data.deletedYn").value(false));
    }

    @Test
    @WithMockUser
    @DisplayName("카트 조회 테스트")
    void getCartTest() throws Exception {
        // given
        Long memberId = 1L;
        CartRespDto cart = new CartRespDto(1L, 1L, false, new ArrayList<>());
        when(cartService.getCart(memberId)).thenReturn(cart);

        // when
        ResultActions result = mockMvc.perform(get("/api/cart/{memberId}", memberId)
                .with(csrf())  // CSRF 토큰 추가
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.cartId").value(1))
                .andExpect(jsonPath("$.data.memberId").value(1))
                .andExpect(jsonPath("$.data.deletedYn").value(false));
    }

    @Test
    @WithMockUser
    @DisplayName("회원 탈퇴 시 카트 삭제 테스트")
    void deleteCartTest() throws Exception {
        // given
        Long memberId = 1L;
        doNothing().when(cartService).deleteCart(memberId);

        // when
        ResultActions result = mockMvc.perform(delete("/api/cart/{memberId}", memberId)
                .with(csrf())  // CSRF 토큰 추가
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    @Test
    @WithMockUser
    @DisplayName("카트 상세 정보 조회 테스트")
    void getCartDetailsTest() throws Exception {
        // given
        Long cartId = 1L;
        List<CartDetail> details = new ArrayList<>();
        details.add(new CartDetail());
        when(cartService.getCartDetails(cartId)).thenReturn(details);

        // when
        ResultActions result = mockMvc.perform(get("/api/cart/detail/{cartId}", cartId)
                .with(csrf())  // CSRF 토큰 추가
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @WithMockUser
    @DisplayName("카트 내 상품 수량 조절 테스트")
    void updateProductQuantityTest() throws Exception {
        // given
        Long cartDetailId = 1L;
        Long quantity = 3L;
        doNothing().when(cartService).updateProductQuantity(cartDetailId, quantity);

        // when
        ResultActions result = mockMvc.perform(patch("/api/cart/detail/{cartDetailId}", cartDetailId)
                .param("quantity", String.valueOf(quantity))
                .with(csrf())  // CSRF 토큰 추가
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201));
    }

    @Test
    @WithMockUser
    @DisplayName("카트 상세 삭제 테스트")
    void deleteCartDetailTest() throws Exception {
        // given
        Long cartDetailId = 1L;
        doNothing().when(cartService).deleteCartDetail(cartDetailId);

        // when
        ResultActions result = mockMvc.perform(delete("/api/cart/detail/{cartDetailId}", cartDetailId)
                .with(csrf())  // CSRF 토큰 추가
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }


}
