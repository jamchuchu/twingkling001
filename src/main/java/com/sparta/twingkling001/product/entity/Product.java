package com.sparta.twingkling001.product.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "Field6")
    private Long field6;

    @Column(name = "sale_quantity")
    private Long saleQuantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "delete_yn")
    private Boolean deleteYn;

    @Column(name = "price")
    private Long price;

    @Column(name = "product_state")
    private String productState;

}