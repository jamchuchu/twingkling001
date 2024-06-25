package com.sparta.twingkling001.product.dto.response;

import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.product.constant.DetailType;
import com.sparta.twingkling001.product.constant.SaleState;
import com.sparta.twingkling001.product.dto.request.ProductDetailReqDto;
import com.sparta.twingkling001.product.entity.ProductDetail;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductDetailRespDto {
    private Long productDetailId;
    private Long productId;
    private DetailType detailType;
    private String detailTypeName;
    private Long saleQuantity;
    private Long detailPrice;
    private SaleState saleState;

    public static ProductDetailRespDto from(ProductDetail detail) {
        return ProductDetailRespDto.builder()
                .productId(detail.getProductId())
                .detailType(detail.getDetailType())
                .detailTypeName(detail.getDetailTypeName())
                .saleQuantity(detail.getSaleQuantity())
                .detailPrice(detail.getDetailPrice())
                .saleState(detail.getSaleState())
                .build();
    }


}
