package com.sparta.twingkling001.product.service;

import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.api.exception.product.NoStockException;
import com.sparta.twingkling001.product.constant.DetailType;
import com.sparta.twingkling001.product.constant.SaleState;
import com.sparta.twingkling001.product.dto.request.ProductDetailReqDto;
import com.sparta.twingkling001.product.dto.request.ProductReqDto;
import com.sparta.twingkling001.product.dto.response.ProductDetailRespDto;
import com.sparta.twingkling001.product.dto.response.ProductRespDto;
import com.sparta.twingkling001.product.entity.Product;
import com.sparta.twingkling001.product.entity.ProductDetail;
import com.sparta.twingkling001.product.repository.ProductDetailRepository;
import com.sparta.twingkling001.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductDetailRepository productDetailRepository;
    @Mock
    private EntityManager entityManager;

    private LocalDateTime now = LocalDateTime.now();

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 저장")
    void addProduct() {
        ProductReqDto reqDto = new ProductReqDto(1L, 1L, 1L, "테스트 상품", 10000L, now, false, SaleState.ON_SALE, new ArrayList<>());
        reqDto.getDetails().add(new ProductDetailReqDto(1L, 1L, DetailType.COLOR, "red", 100L, 10000L, SaleState.ON_SALE));

        Product product = Product.from(reqDto);
        List<ProductDetail> productDetails = reqDto.getDetails().stream().map(ProductDetail::from).toList();

        ProductRespDto expected = ProductRespDto.from(product, productDetails);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productDetailRepository.save(any(ProductDetail.class))).thenReturn(productDetails.get(0));

        ProductRespDto response = productService.addProduct(reqDto);

        assertEquals(expected.getClass(), response.getClass());
        assertEquals(expected.getProductId(), response.getProductId());
        assertEquals(expected.getCategoryId(), response.getCategoryId());
        assertEquals(expected.getMemberId(), response.getMemberId());
        assertEquals(expected.getProductName(), response.getProductName());
        assertEquals(expected.getPrice(), response.getPrice());
        assertEquals(expected.getCreatedAt(), response.getCreatedAt());
        assertEquals(expected.getDeleteYn(), response.getDeleteYn());
    }


    @Test
    @DisplayName("상품 번호로 상품 가져오기")
    void getProductByProductId() {
        Long productId = 1L;
        Product product = new Product(1L, 1L, 1L, "테스트 상품", 10000L, now, false, SaleState.ON_SALE);
        List<ProductDetail> productDetails = Arrays.asList(new ProductDetail(1L, 1L, DetailType.COLOR, "red", 100L, SaleState.ON_SALE, 10000L));

        when(productRepository.findProductByProductId(productId)).thenReturn(product);
        when(productDetailRepository.findProductDetailsByProductId(productId)).thenReturn(productDetails);

        ProductRespDto expected = ProductRespDto.from(product, productDetails);

        ProductRespDto response = productService.getProductByProductId(productId);

        assertEquals(expected.getClass(), response.getClass());
        assertEquals(expected.getProductId(), response.getProductId());
        assertEquals(expected.getCategoryId(), response.getCategoryId());
        assertEquals(expected.getMemberId(), response.getMemberId());
        assertEquals(expected.getProductName(), response.getProductName());
        assertEquals(expected.getPrice(), response.getPrice());
        assertEquals(expected.getCreatedAt(), response.getCreatedAt());
        assertEquals(expected.getDeleteYn(), response.getDeleteYn());
    }

    @Test
    @DisplayName("상품 판매자 번호로 상품들 가져오기")
    void getProductsByMemberId() {
        Long memberId = 1L;
        Long productId = 1L;
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, 1L, 1L, "테스트 상품1", 10000L, now, false, SaleState.ON_SALE));
        products.add(new Product(2L, 1L, 1L, "테스트 상품2", 10000L, now, false, SaleState.ON_SALE));

        when(productRepository.findProductsByMemberId(memberId)).thenReturn(products);
        when(productDetailRepository.findProductDetailsByProductId(productId)).thenReturn(new ArrayList<>());

        List<ProductRespDto> expected = new ArrayList<>();
        for (Product p : products) {
            expected.add(ProductRespDto.from(p));
        }

        List<ProductRespDto> response = productService.getProductsByMemberId(memberId);

        assertEquals(expected.get(0).getClass(), response.get(0).getClass());
        assertEquals(expected.get(0).getProductId(), response.get(0).getProductId());
        assertEquals(expected.get(0).getCategoryId(), response.get(0).getCategoryId());
        assertEquals(expected.get(0).getMemberId(), response.get(0).getMemberId());
        assertEquals(expected.get(0).getProductName(), response.get(0).getProductName());
        assertEquals(expected.get(0).getPrice(), response.get(0).getPrice());
        assertEquals(expected.get(0).getCreatedAt(), response.get(0).getCreatedAt());
        assertEquals(expected.get(0).getDeleteYn(), response.get(0).getDeleteYn());
    }

    @Test
    @DisplayName("상품 판매자 정보와 상태 정보로 상품들 가져오기")
    void getProductsByMemberIdAndState() {
        Long memberId = 1L;
        SaleState state = SaleState.ON_SALE;
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, 1L, 1L, "테스트 상품1", 10000L, now, false, SaleState.ON_SALE));
        products.add(new Product(2L, 1L, 1L, "테스트 상품2", 10000L, now, false, SaleState.ON_SALE));

        when(productRepository.findProductsByMemberIdAndSaleState(memberId, state)).thenReturn(products);
        when(productDetailRepository.findProductDetailsByProductId(any(Long.class))).thenReturn(new ArrayList<>());

        List<ProductRespDto> response = productService.getProductsByMemberIdAndState(memberId, state);

        List<ProductRespDto> expected = new ArrayList<>();
        for (Product p : products) {
            expected.add(ProductRespDto.from(p));
        }

        assertEquals(expected.get(0).getClass(), response.get(0).getClass());
        assertEquals(expected.get(0).getProductId(), response.get(0).getProductId());
        assertEquals(expected.get(0).getCategoryId(), response.get(0).getCategoryId());
        assertEquals(expected.get(0).getMemberId(), response.get(0).getMemberId());
        assertEquals(expected.get(0).getProductName(), response.get(0).getProductName());
        assertEquals(expected.get(0).getPrice(), response.get(0).getPrice());
        assertEquals(expected.get(0).getCreatedAt(), response.get(0).getCreatedAt());
        assertEquals(expected.get(0).getDeleteYn(), response.get(0).getDeleteYn());

    }

    @Test
    @DisplayName("상품 이름으로 상품들 가져오기")
    void getProductByProductName() {
        String productName = "테스트";
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, 1L, 1L, "테스트 상품1", 10000L, now, false, SaleState.ON_SALE));
        products.add(new Product(2L, 1L, 1L, "테스트 상품2", 10000L, now, false, SaleState.ON_SALE));
        products.add(new Product(3L, 1L, 1L, "신규 테스트 상품", 10000L, now, false, SaleState.ON_SALE));

        when(productRepository.findProductsByProductNameLike(productName)).thenReturn(products);
        when(productDetailRepository.findProductDetailsByProductId(any(Long.class))).thenReturn(new ArrayList<>());

        List<ProductRespDto> response = productService.getProductByProductName(productName, PageRequest.of(0,10));

        List<ProductRespDto> expected = new ArrayList<>();
        for (Product p : products) {
            expected.add(ProductRespDto.from(p));
        }
        assertEquals(expected.size(), response.size());
        assertEquals(expected.get(0).getProductName(), response.get(0).getProductName());
        assertEquals(expected.get(1).getProductName(), response.get(1).getProductName());
        assertEquals(expected.get(2).getProductName(), response.get(2).getProductName());
    }

    @Test
    @DisplayName("상품 별, 세부사항 타입 별로 세부사항 가져오기 ")
    void getProductDetailByDetailType() {
        Long productId = 1L;
        DetailType detailType = DetailType.COLOR;

        List<ProductDetail> expected = new ArrayList<>();
        expected.add(new ProductDetail(1L, 1L, DetailType.COLOR, "red", 100L, SaleState.ON_SALE, 10000L));
        expected.add(new ProductDetail(1L, 1L, DetailType.COLOR, "yellow", 100L, SaleState.ON_SALE, 10000L));
        expected.add(new ProductDetail(1L, 1L, DetailType.COLOR, "blue", 100L, SaleState.ON_SALE, 10000L));

        when(productDetailRepository.findProductDetailsByProductIdAndDetailType(productId, detailType)).thenReturn(expected);

        List<ProductDetail> response = productService.getProductDetailByDetailType(productId, detailType);

        assertEquals(expected.size(), response.size());
        assertEquals(expected.get(0).getDetailType(), response.get(0).getDetailType());
        assertEquals(expected.get(0).getDetailTypeName(), response.get(0).getDetailTypeName());
        assertEquals(expected.get(1).getDetailTypeName(), response.get(1).getDetailTypeName());
        assertEquals(expected.get(2).getDetailTypeName(), response.get(2).getDetailTypeName());
    }

    @Test
    @DisplayName("상품 정보 수정")
    void updateProduct() {
        // Given
        Long productId = 1L;
        ProductReqDto reqDto = new ProductReqDto(1L, 2L, 1L, "Updated Product", 20000L, LocalDateTime.now(), false, SaleState.ON_SALE, new ArrayList<>());
        Product existingProduct = new Product(productId, 1L, 1L, "Original Product", 10000L, LocalDateTime.now(), false, SaleState.ON_SALE);

        when(entityManager.find(Product.class, productId)).thenReturn(existingProduct);

        // When
        productService.updateProduct(productId, reqDto);

        // Then
        verify(entityManager).find(Product.class, productId);
        assertEquals(reqDto.getCategoryId(), existingProduct.getCategoryId());
        assertEquals(reqDto.getProductName(), existingProduct.getProductName());
        assertEquals(reqDto.getPrice(), existingProduct.getPrice());
        assertEquals(reqDto.getSaleState(), existingProduct.getSaleState());
    }

    @Test
    @DisplayName("상품 상태 수정")
    void updateSaleState() {
        // Given
        Long productId = 1L;
        SaleState newSaleState = SaleState.SOLD_OUT;
        Product existingProduct = new Product(productId, 1L, 1L, "Test Product", 10000L, LocalDateTime.now(), false, SaleState.ON_SALE);

        when(entityManager.find(Product.class, productId)).thenReturn(existingProduct);

        // When
        productService.updateSaleState(productId, newSaleState);

        // Then
        verify(entityManager).find(Product.class, productId);
        assertEquals(newSaleState, existingProduct.getSaleState());
    }
//
//    @Test
//    void deleteProduct() {
//    }

    @Test
    @DisplayName("상품 세부 사항 추가")
    void addProductDetail() {
        ProductDetailReqDto detailReqDto = new ProductDetailReqDto(1L, 100L, DetailType.COLOR, "Red", 50L, 15000L, SaleState.ON_SALE);
        ProductDetail productDetail = ProductDetail.from(detailReqDto);

        when(productDetailRepository.save(any(ProductDetail.class))).thenReturn(productDetail);

        ProductDetailRespDto expected = ProductDetailRespDto.from(productDetail);

        ProductDetailRespDto response = productService.addProductDetail(detailReqDto);

        assertEquals(expected.getClass(), response.getClass());
        assertEquals(expected.getProductDetailId(), response.getProductDetailId());
        assertEquals(expected.getProductId(), response.getProductId());
        assertEquals(expected.getDetailType(), response.getDetailType());
        assertEquals(expected.getDetailTypeName(), response.getDetailTypeName());
        assertEquals(expected.getSaleQuantity(), response.getSaleQuantity());
        assertEquals(expected.getDetailPrice(), response.getDetailPrice());
        assertEquals(expected.getSaleState(), response.getSaleState());
    }

    @Test
    @DisplayName("상품별 세부사항 들고오기")
    void getProductDetailByProductId() {
        Long productId = 1L;
        List<ProductDetail> details = new ArrayList<>();
        details.add(new ProductDetail(1L, 100L, DetailType.COLOR, "Red", 50L, SaleState.ON_SALE, 10000L));
        details.add(new ProductDetail(1L, 100L, DetailType.COLOR, "yellow", 50L, SaleState.ON_SALE, 10000L));
        details.add(new ProductDetail(1L, 100L, DetailType.COLOR, "blue", 50L, SaleState.ON_SALE, 10000L));

        when(productDetailRepository.findProductDetailsByProductId(productId)).thenReturn(details);

        List<ProductDetailRespDto> expected = new ArrayList<>();
        for (ProductDetail pd : details) {
            expected.add(ProductDetailRespDto.from(pd));
        }

        List<ProductDetailRespDto> response = productService.getProductDetailByProductId(productId);


        assertEquals(expected.size(), response.size());
        assertEquals(expected.get(0).getDetailTypeName(), response.get(0).getDetailTypeName());
        assertEquals(expected.get(1).getDetailTypeName(), response.get(1).getDetailTypeName());
        assertEquals(expected.get(2).getDetailTypeName(), response.get(2).getDetailTypeName());
    }

    @Test
    @DisplayName("상품별 세부사항 수정하기")
    void updateProductDetails() throws DataNotFoundException {
        // Given
        Long productDetailId = 1L;
        ProductDetailReqDto reqDto = new ProductDetailReqDto(
                productDetailId, 1L, DetailType.COLOR, "Blue", 100L, 15000L, SaleState.ON_SALE
        );
        ProductDetail existingDetail = new ProductDetail(
                productDetailId, 1L, DetailType.COLOR, "Red", 50L,  SaleState.ON_SALE, 10000L
        );

        when(entityManager.find(ProductDetail.class, productDetailId)).thenReturn(existingDetail);

        // When
        productService.updateProductDetails(reqDto);

        // Then
        verify(entityManager).find(ProductDetail.class, productDetailId);
        assertEquals(reqDto.getDetailTypeName(), existingDetail.getDetailTypeName());
        assertEquals(reqDto.getSaleQuantity(), existingDetail.getSaleQuantity());
        assertEquals(reqDto.getDetailPrice(), existingDetail.getDetailPrice());
        assertEquals(reqDto.getSaleState(), existingDetail.getSaleState());
    }

    @Test
    @DisplayName("상품별 세부사항 수정하기 -- 상품 번호 오류")
    void updateProductDetails_throwsDataNotFoundException() {
        // Given
        Long nonExistentProductDetailId = 999L;
        ProductDetailReqDto reqDto = new ProductDetailReqDto(
                nonExistentProductDetailId, 1L, DetailType.COLOR, "Blue", 100L, 15000L, SaleState.ON_SALE
        );

        when(entityManager.find(ProductDetail.class, nonExistentProductDetailId)).thenReturn(null);

        // When & Then
        assertThrows(DataNotFoundException.class, () -> {
            productService.updateProductDetails(reqDto);
        });

        verify(entityManager).find(ProductDetail.class, nonExistentProductDetailId);
    }

    @Test
    @DisplayName("판매 수량 증가")
    void plusProductQuantity() throws Exception {
        //given
        Long productDetailId = 1L;
        ProductDetail existingDetail = new ProductDetail(
                productDetailId, 1L, DetailType.COLOR, "Red", 50L, SaleState.ON_SALE, 10000L
        );
        Long initialQuantity = existingDetail.getSaleQuantity();

        when(entityManager.find(ProductDetail.class, productDetailId)).thenReturn(existingDetail);

        // When
        productService.plusProductQuantity(productDetailId);

        // Then
        verify(entityManager).find(ProductDetail.class, productDetailId);
        assertEquals(initialQuantity + 1, existingDetail.getSaleQuantity());
    }


    @Test
    @DisplayName("판매 수량 감소 -- 재고 있음")
    void minusProductQuantitySuccess() throws Exception {
        Long productDetailId = 1L;
        Long purchaseNum = 5L;


        ProductDetail productDetail = new ProductDetail(1L, 100L, DetailType.COLOR, "yellow", 100L, SaleState.ON_SALE, 10000L);
        when(entityManager.find(ProductDetail.class, productDetailId)).thenReturn(productDetail);

        // When
        productService.minusProductQuantity(productDetailId, purchaseNum );

        // Then
        verify(entityManager).find(ProductDetail.class, productDetailId);
        assertEquals(99L, productDetail.getSaleQuantity()); // 수량이 1 감소했는지 확인
    }


    @Test
    @DisplayName("판매 수량 감소 -- 재고 없음")
    void minusProductQuantityFail() {
        Long productDetailId = 1L;
        Long purchaseNum = 5L;
        ProductDetail productDetail = new ProductDetail(1L, 100L, DetailType.COLOR, "yellow", 0L, SaleState.ON_SALE, 10000L);
        when(entityManager.find(ProductDetail.class, productDetailId)).thenReturn(productDetail);

        // When & Then
        assertThrows(NoStockException.class, () -> {
            productService.minusProductQuantity(productDetailId, purchaseNum);
        });

        // Verify
        verify(entityManager).find(ProductDetail.class, productDetailId);
        assertEquals(0L, productDetail.getSaleQuantity()); // 수량이 변경되지 않았는지 확인    }
    }
//
//    @Test
//    void deleteProductDetails() {
//    }
//
//    @Test
//    void deleteProductDetail() {
//    }

}