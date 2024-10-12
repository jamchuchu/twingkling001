package com.sparta.twingkling001.product.controller;

import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.product.constant.DetailType;
import com.sparta.twingkling001.product.constant.SaleState;
import com.sparta.twingkling001.product.dto.request.ProductDetailReqDto;
import com.sparta.twingkling001.product.dto.response.ProductDetailRespDto;
import com.sparta.twingkling001.product.dto.response.ProductRespDto;
import com.sparta.twingkling001.product.entity.ProductDetail;
import com.sparta.twingkling001.product.dto.request.ProductReqDto;
import com.sparta.twingkling001.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //주문 작성시 한번에 생성 detail 정보까지 한번에 생성
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> addProduct(@RequestBody ProductReqDto reqDto){
        productService.addProduct(reqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //물건 정보 디테일까지 들고오기
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductRespDto>> getProductByProductId(@PathVariable Long productId) {
        ProductRespDto response = productService.getProductByProductId(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //물건 이름으로 들고오기
    @GetMapping("/name")
    public ResponseEntity<ApiResponse<List<ProductRespDto>>> getProductByProductName(@RequestParam String productName, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<ProductRespDto> response = productService.getProductByProductName(productName, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //판매자별 판매 물건 들고오기
    @GetMapping("/seller/{memberId}")
    public ResponseEntity<ApiResponse<List<ProductRespDto>>> getProductByMemberId(@PathVariable Long memberId) {
        List<ProductRespDto> response = productService.getProductsByMemberId(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //판매자별 물건 상태별로 들고오기
    @GetMapping("/seller/{memberId}/state")
    public ResponseEntity<ApiResponse<?>> getProductByMemberIdAndSaleState(@PathVariable Long memberId, @RequestParam SaleState saleState) {
        List<ProductRespDto> response = productService.getProductsByMemberIdAndState(memberId, saleState);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }


    //판매 물건 정보 디테일 까지 변경
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> updateProduct (@PathVariable Long productId, @RequestBody ProductReqDto reqDto){
        productService.updateProduct(productId, reqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }


    //판매 상태 변경
    @PatchMapping("/{productId}/state")
    public ResponseEntity<ApiResponse<?>> updateSaleState (@PathVariable Long productId, @RequestParam SaleState state){
        productService.updateSaleState(productId, state);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //전체 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        productService.deleteProductDetails(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }


    //판매 추가 정보 detailtype별로 들고오기
    @GetMapping("/{productId}/details/{detailType}")
    public ResponseEntity<ApiResponse<?>> getProductDetailByDetailType(@PathVariable long productId, DetailType detailType) {
        List<ProductDetail> response = productService.getProductDetailByDetailType(productId, detailType);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //판매 추가정보 개별 추가
    @PostMapping("/{productId}/detail")
    public ResponseEntity<ApiResponse<ProductDetailRespDto>> addProductDetail (@PathVariable Long productId, @RequestBody ProductDetailReqDto reqDto){
        ProductDetailRespDto response = productService.addProductDetail(reqDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
    }

    //판매 추가 정보만 들고오기
    @GetMapping("/{productId}/details")
    public ResponseEntity<ApiResponse<List<ProductDetailRespDto>>> getProductDetailByProductId(@PathVariable("productId") Long productId) {
        List<ProductDetailRespDto> response = productService.getProductDetailByProductId(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }



    //판매 추가 정보 개별 수정
    @PutMapping("/{productId}/detail")
    public ResponseEntity<ApiResponse<?>> updateProductDetails (@PathVariable Long productId, @RequestBody ProductDetailReqDto reqDto) throws DataNotFoundException {
        productService.updateProductDetails(reqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //판매 추가 정보 전체 삭제
    @DeleteMapping("/{productId}/details")
    public ResponseEntity<ApiResponse<?>> deleteProductDetails (@PathVariable Long productId ){
        productService.deleteProductDetails(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //판매 추가 정보 개별 삭제
    @DeleteMapping("/{productId}/detail/{productDetailId}")
    public ResponseEntity<ApiResponse<?>> deleteProduct (@PathVariable Long productId, long productDetailId){
        productService.deleteProductDetail(productDetailId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }




}
