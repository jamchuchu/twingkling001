package com.sparta.twingkling001.like.entity;

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
@Table(name = "likes")

public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;
    private Long productId;
    private Long memberId;
    private Boolean deletedYn;

    public static Like from(Long productId, Long memberId){
        return Like.builder()
                .productId(productId)
                .memberId(memberId)
                .deletedYn(false)
                .build();
    }

    public void setDeletedYn(Boolean deletedYn) {
        this.deletedYn = deletedYn;
    }
}
