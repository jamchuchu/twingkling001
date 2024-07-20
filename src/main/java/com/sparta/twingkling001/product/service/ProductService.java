package com.sparta.twingkling001.product.service;


import com.querydsl.jpa.impl.JPAQueryFactory;
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
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;


    public ProductRespDto addProduct(ProductReqDto reqDto) {
        Product product = productRepository.save(Product.from(reqDto));
        List<ProductDetail> details = reqDto.getDetails().stream()
                .map(ProductDetail::from)
                .map(productDetailRepository::save).toList();
        return ProductRespDto.from(product, details);
    }

    @Cacheable(value = "product", key = "#productId", cacheManager = "cacheManager")
    public ProductRespDto getProductByProductId(Long productId) {
        Product product = productRepository.findProductByProductId(productId);
        List<ProductDetail> details = productDetailRepository.findProductDetailsByProductId(productId);
        return ProductRespDto.from(product, details);
    }

    public List<ProductRespDto> getProductsByMemberId(Long memberId) {
        List<ProductRespDto> products = productRepository.findProductsByMemberId(memberId).stream()
                .map(ProductRespDto::from).toList();
        products.forEach(product -> {
            Long productId = product.getProductId();
            List<ProductDetail> details = productDetailRepository.findProductDetailsByProductId(productId);
            product.setDetails(details);
        });
        return products;
    }

    public List<ProductRespDto> getProductsByMemberIdAndState(Long memberId, SaleState saleState) {
        List<ProductRespDto> products = productRepository.findProductsByMemberIdAndSaleState(memberId, saleState).stream()
                .map(ProductRespDto::from).toList();
        products.forEach(product -> {
            Long productId = product.getProductId();
            List<ProductDetail> details = productDetailRepository.findProductDetailsByProductId(productId);
            product.setDetails(details);
        });
        return products;
    }

    public List<ProductRespDto> getProductByProductName(String productName, Pageable pageable) {
        List<ProductRespDto> products = productRepository.findProductsByProductNameContaining(productName, pageable)
                .stream().map(ProductRespDto::from).toList();
        products.forEach(product ->{
            product.setDetails(productDetailRepository
                    .findProductDetailsByProductId(product.getProductId()));
        });
        return products;
    }

    public List<ProductDetail> getProductDetailByDetailType(long productId, DetailType detailType) {
        return productDetailRepository.findProductDetailsByProductIdAndDetailType(productId, detailType);
    }

    //기본정보만 변경
    public void updateProduct(Long productId, ProductReqDto reqDto) {
        Product product = entityManager.find(Product.class, productId);
        if(product != null){
            product.setCategoryId(reqDto.getCategoryId());
            product.setProductName(reqDto.getProductName());
            product.setPrice(reqDto.getPrice());
            product.setSaleState(reqDto.getSaleState());
        }
    }

    public void updateSaleState(Long productId, SaleState newSaleState) {
        Product product = entityManager.find(Product.class, productId);
        if (product != null) {
            product.setSaleState(newSaleState);
        }
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteProductByProductId(productId);
    }

    public ProductDetailRespDto addProductDetail(ProductDetailReqDto reqDto) {
        ProductDetail productDetail = productDetailRepository.save(ProductDetail.from(reqDto));
        return ProductDetailRespDto.from(productDetail);
    }

    public List<ProductDetailRespDto> getProductDetailByProductId(Long productId) {
        List<ProductDetail> details = productDetailRepository.findProductDetailsByProductId(productId);
        return details.stream().map(ProductDetailRespDto::from).toList();
    }


    @Transactional
    public void updateProductDetails(ProductDetailReqDto reqDto) throws DataNotFoundException {
        ProductDetail detail = entityManager.find(ProductDetail.class, reqDto.getProductDetailId());
        if(detail == null){
            throw new DataNotFoundException();
        }
        detail.setDetailTypeName(reqDto.getDetailTypeName());
        detail.setSaleQuantity(reqDto.getSaleQuantity());
        detail.setDetailPrice(reqDto.getDetailPrice());
        detail.setSaleState(reqDto.getSaleState());
    }


    public void plusProductQuantity(Long ProductDetailId) throws DataNotFoundException {
        ProductDetail detail = entityManager.find(ProductDetail.class, ProductDetailId);
        if(detail == null){
            throw new DataNotFoundException();
        }
        detail.setSaleQuantity(detail.getSaleQuantity()+1);
    }

    @Transactional
    public void minusProductQuantity(Long productDetailId, Long purchaseNum) throws Exception {
        ProductDetail detail = entityManager.find(ProductDetail.class, productDetailId, LockModeType.PESSIMISTIC_WRITE);
        if (detail == null) {
            throw new DataNotFoundException();
        }
        if (detail.getSaleQuantity() < purchaseNum) {
            throw new NoStockException();
        }
        detail.setSaleQuantity(detail.getSaleQuantity() - purchaseNum);
        // entityManager.merge(detail);  // 필요한 경우 명시적으로 merge
    }


    public void deleteProductDetails(Long productId) {
        productDetailRepository.deleteAllByProductId(productId);
    }

    public void deleteProductDetail(long productDetailId) {
        productDetailRepository.deleteByProductDetailId(productDetailId);
    }


}
