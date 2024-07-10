package com.sparta.twingkling001.like.dto.response;

import com.sparta.twingkling001.like.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LikeRespDto {
    private Long likeId;
    private Long productId;
    private Long memberId;
    private Boolean deletedYn;

    public static LikeRespDto from(Like like){
       return LikeRespDto.builder()
               .likeId(like.getLikeId())
               .productId(like.getProductId())
               .memberId(like.getMemberId())
               .deletedYn(like.getDeletedYn())
               .build();
    }
}
