package com.sparta.twingkling001.product.repository;

import com.sparta.twingkling001.product.constant.SaleState;
import com.sparta.twingkling001.product.dto.request.ProductReqDto;
import com.sparta.twingkling001.product.dto.response.ProductRespDto;
import com.sparta.twingkling001.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findProductByProductId(long productId);
    List<Product> findProductsByMemberId(long memberId);
    List<Product> findProductsByMemberIdAndSaleState(Long memberId, SaleState state);
    List<Product> findProductsByProductNameLike(String productName);
    List<Product> findProductsByProductNameContaining(String productName, Pageable pageable);
    List<Product> findProductsByProductNameStartingWith(String productName);
    void deleteProductByProductId(Long productId);

}
