package com.sparta.twingkling001.product.entity;

import com.sparta.twingkling001.product.constant.DetailType;
import com.sparta.twingkling001.product.constant.SaleState;
import com.sparta.twingkling001.product.dto.request.ProductDetailReqDto;
import com.sparta.twingkling001.product.dto.request.ProductReqDto;
import com.sparta.twingkling001.product.dto.response.ProductDetailRespDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_detail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDetailId;
    private Long productId;
    @Enumerated(EnumType.STRING)
    private DetailType detailType;
    private String detailTypeName;
    private Long saleQuantity;
    @Enumerated(EnumType.STRING)
    private SaleState saleState;
    private Long detailPrice;


    public void setDetailTypeName(String detailTypeName) {
        this.detailTypeName = detailTypeName;
    }

    public void setSaleQuantity(Long saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public void setDetailPrice(Long detailPrice) {
        this.detailPrice = detailPrice;
    }

    public void setSaleState(SaleState saleState) {
        this.saleState = saleState;
    }


    public static ProductDetail from(ProductDetailReqDto reqDto) {
        return ProductDetail.builder()
                .productId(reqDto.getProductId())
                .detailType(reqDto.getDetailType())
                .detailTypeName(reqDto.getDetailTypeName())
                .saleQuantity(reqDto.getSaleQuantity())
                .detailPrice(reqDto.getDetailPrice())
                .saleState(reqDto.getSaleState())
                .build();
    }

}
