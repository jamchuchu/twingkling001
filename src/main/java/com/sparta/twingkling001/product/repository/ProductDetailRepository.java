package com.sparta.twingkling001.product.repository;

import com.sparta.twingkling001.product.constant.DetailType;
import com.sparta.twingkling001.product.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    interface SaleQuantityMapping {
        Long getProductDetailId();
        Long getSaleQuantity();
    }
    SaleQuantityMapping findSaleQuantityByProductDetailId(Long productDetailId);
    boolean existsProductDetailByProductDetailId(Long productDetailId);
    ProductDetail findProductDetailByProductDetailId(Long productDetailId);
    List<ProductDetail> findProductDetailsByProductId(Long productId);
    List<ProductDetail> findProductDetailsByProductIdAndDetailType(long productId, DetailType detailType);
    void deleteAllByProductId(Long productId);
    void deleteByProductDetailId(Long productDetailId);
}
