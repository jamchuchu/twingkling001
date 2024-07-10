package com.sparta.twingkling001.like.repository;

import com.sparta.twingkling001.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findLikeByProductIdAndMemberId(Long productId, Long memberId);
}
