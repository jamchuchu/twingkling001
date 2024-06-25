package com.sparta.twingkling001.product.repository;

import com.sparta.twingkling001.product.constant.DetailType;
import com.sparta.twingkling001.product.dto.request.ProductDetailReqDto;
import com.sparta.twingkling001.product.dto.response.ProductDetailRespDto;
import com.sparta.twingkling001.product.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findProductDetailsByProductId(Long productId);
    List<ProductDetail> findProductDetailsByProductIdAndDetailType(long productId, DetailType detailType);
    void deleteAllByProductId(Long productId);
    void deleteByProductDetailId(Long productDetailId);
}
