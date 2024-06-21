package com.sparta.twingkling001.like.service;

import com.sparta.twingkling001.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikeService {
    private final LikeRepository likeRepository;
}
