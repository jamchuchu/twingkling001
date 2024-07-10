package com.sparta.twingkling001.product.dto.request;

import com.sparta.twingkling001.product.constant.DetailType;
import com.sparta.twingkling001.product.constant.SaleState;
import com.sparta.twingkling001.product.dto.response.ProductDetailRespDto;
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
public class ProductDetailReqDto {
    private Long productDetailId;

    private Long productId;

    @Enumerated(EnumType.STRING)
    private DetailType detailType;

    private String detailTypeName;

    private Long saleQuantity;

    private Long detailPrice;

    @Enumerated(EnumType.STRING)
    private SaleState saleState;

}
