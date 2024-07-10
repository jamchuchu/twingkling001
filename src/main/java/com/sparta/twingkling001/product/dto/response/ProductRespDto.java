package com.sparta.twingkling001.product.dto.response;

import com.sparta.twingkling001.product.constant.SaleState;
import com.sparta.twingkling001.product.dto.request.ProductReqDto;
import com.sparta.twingkling001.product.entity.Product;
import com.sparta.twingkling001.product.entity.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductRespDto {
    private Long productId;
    private Long categoryId;
    private Long memberId;
    private String productName;
    private Long price;
    private LocalDateTime createdAt;
    private Boolean deleteYn;
    private SaleState saleState;

    private List<ProductDetailRespDto> details = new ArrayList<>();

    public void setDetails(List<ProductDetail> details) {
        this.details = details.stream().map(ProductDetailRespDto::from).toList();
    }

    public static ProductRespDto from(Product product) {
        return ProductRespDto.builder()
                .productId(product.getProductId())
                .categoryId(product.getCategoryId())
                .memberId(product.getMemberId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .deleteYn(product.getDeleteYn())
                .saleState(product.getSaleState())
                .build();
    }


    public static ProductRespDto from(Product product, List<ProductDetail> detail) {
        ProductRespDto respDto = ProductRespDto.from(product);
        respDto.setDetails(detail);
        return respDto;
    }

}
