package com.sparta.twingkling001.cart.controller;

import com.sparta.twingkling001.api.exception.cart.CartAlreadyExistsException;
import com.sparta.twingkling001.api.exception.general.AlreadyDeletedException;
import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.cart.dto.request.CartDetailReqDto;
import com.sparta.twingkling001.cart.dto.response.CartDetailRespDto;
import com.sparta.twingkling001.cart.dto.response.CartRespDto;
import com.sparta.twingkling001.cart.entity.CartDetail;
import com.sparta.twingkling001.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/cart")
@RestController
public class CartController {
    private final CartService cartService;


    //회원가입 시 빈카트 생성
    @PostMapping("/{memberId}")
    public ResponseEntity<ApiResponse<CartRespDto>> addEmptyCart(@PathVariable Long memberId) throws CartAlreadyExistsException {
        CartRespDto response = cartService.addEmptyCart(memberId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
    }

    //카트 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<CartRespDto>> getCart(@PathVariable Long memberId) throws DataNotFoundException {
        CartRespDto response = cartService.getCart(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //회원 탈퇴 시 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<?>> deleteCart(@PathVariable Long memberId) throws DataNotFoundException, AlreadyDeletedException {
        cartService.deleteCart(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //카트에 물건 추가
    @PostMapping("/detail/{memberId}")
    public ResponseEntity<ApiResponse<CartDetailRespDto>> createCartDetail(@PathVariable Long memberId, @RequestBody CartDetailReqDto reqDto) throws DataNotFoundException {
        CartDetailRespDto response = cartService.addCartDetail(reqDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
    }

    @GetMapping("/detail/{memberId}")
    public ResponseEntity<ApiResponse<List<CartDetail>>> getCartDetails(@PathVariable Long memberId){
        List<CartDetail> response = cartService.getCartDetails(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }


    //카트에 물건 양 조절
    @PatchMapping("/detail/{cartDetailId}")
    public ResponseEntity<ApiResponse<?>> updateProductQuantity(@PathVariable Long cartDetailId, @RequestParam Long quantity){
        cartService.updateProductQuantity(cartDetailId, quantity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE));
    }

    //물건 삭제시 해당 물건 판매 전부 false /  물건 재판매시 해당 물건 판매 전부 true
    @PatchMapping("/detail/{productId}/is-sale")
    public ResponseEntity<ApiResponse<?>> updatePresentSaleYn(@PathVariable Long productId, @RequestParam boolean isSale) {
        cartService.updatePresentSaleYn(productId, isSale);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }


    @DeleteMapping("/detail/{cartDetailId}")
    public ResponseEntity<ApiResponse<?>> deleteCartDetail(@PathVariable Long cartDetailId) {
        cartService.deleteCartDetail(cartDetailId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }




}
