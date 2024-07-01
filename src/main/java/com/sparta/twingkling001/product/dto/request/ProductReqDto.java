package com.sparta.twingkling001.product.dto.request;

import com.sparta.twingkling001.product.constant.SaleState;
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
public class ProductReqDto {

    private Long productId;
    private Long categoryId;
    private Long memberId;
    private String productName;
    private Long price;
    private LocalDateTime createdAt;
    private boolean deletedYn;
    private SaleState saleState;

    private List<ProductDetailReqDto> details = new ArrayList<>();

}
