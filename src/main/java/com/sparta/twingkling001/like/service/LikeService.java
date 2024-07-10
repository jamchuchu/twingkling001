package com.sparta.twingkling001.like.service;

import com.sparta.twingkling001.like.dto.response.LikeRespDto;
import com.sparta.twingkling001.like.entity.Like;
import com.sparta.twingkling001.like.repository.LikeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public LikeRespDto updateLikes(Long productId, Long memberId) {
        Like like = getLike(productId, memberId);
        if(like == null){
            like = likeRepository.save(Like.from(productId, memberId));
        }else {
            if (!like.getDeletedYn()) {
                like.setDeletedYn(true);
            }else{
                like.setDeletedYn(false);
            }
        }
        return LikeRespDto.from(like);
    }

    public Like getLike(Long productId, Long memberId) {
        return likeRepository.findLikeByProductIdAndMemberId(productId, memberId);
    }
}
