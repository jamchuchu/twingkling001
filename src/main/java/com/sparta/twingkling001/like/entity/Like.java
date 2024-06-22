package com.sparta.twingkling001.like.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")

public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "deleted_yn")
    private Boolean deletedYn;
}
