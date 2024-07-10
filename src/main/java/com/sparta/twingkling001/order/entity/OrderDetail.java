package com.sparta.twingkling001.order.entity;

import com.sparta.twingkling001.order.dto.request.OrderDetailReqDto;
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
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Long productDetailId;
    private Long quantity;
    private Boolean deletedYn;
    private Long price;

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setDeletedYn(Boolean deletedYn) {
        this.deletedYn = deletedYn;
    }

    public static OrderDetail from(Order order, OrderDetailReqDto reqDto){
        return OrderDetail.builder()
                .order(order)
                .productDetailId(reqDto.getProductDetailId())
                .quantity(reqDto.getQuantity())
                .deletedYn(reqDto.getDeletedYn())
                .price(reqDto.getPrice())
                .build();
    }
}
