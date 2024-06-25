package com.sparta.twingkling001.product.entity;

import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.product.constant.SaleState;
import com.sparta.twingkling001.product.dto.request.ProductReqDto;
import com.sparta.twingkling001.product.dto.response.ProductRespDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private Long categoryId;
    private Long memberId;
    private String productName;
    private Long price;
    private LocalDateTime createdAt;
    private Boolean deleteYn;
    @Enumerated(EnumType.STRING)
    private SaleState saleState;

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setDeleteYn(Boolean deleteYn) {
        this.deleteYn = deleteYn;
    }

    public void setSaleState(SaleState saleState) {
        this.saleState = saleState;
    }

    public static Product from(ProductReqDto reqDto) {
        return Product.builder()
                .productId(reqDto.getProductId())
                .categoryId(reqDto.getCategoryId())
                .memberId(reqDto.getMemberId())
                .productName(reqDto.getProductName())
                .price(reqDto.getPrice())
                .createdAt(reqDto.getCreatedAt())
                .deleteYn(reqDto.isDeletedYn())
                .saleState(reqDto.getSaleState())
                .build();
    }

}