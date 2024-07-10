package com.sparta.twingkling001.product.repository;

import com.sparta.twingkling001.product.constant.DetailType;
import com.sparta.twingkling001.product.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    boolean existsProductDetailByProductDetailId(Long productDetailId);
    ProductDetail findProductDetailByProductDetailId(Long productDetailId);
    List<ProductDetail> findProductDetailsByProductId(Long productId);
    List<ProductDetail> findProductDetailsByProductIdAndDetailType(long productId, DetailType detailType);
    void deleteAllByProductId(Long productId);
    void deleteByProductDetailId(Long productDetailId);
}
