package com.sparta.twingkling001.like.service;

import com.sparta.twingkling001.like.dto.response.LikeRespDto;
import com.sparta.twingkling001.like.entity.Like;
import com.sparta.twingkling001.like.repository.LikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    @Mock
    private LikeRepository likeRepository;
    @InjectMocks
    LikeService likeService;

    @Test
    @DisplayName("")
    void updateLikesFirstLike() throws Exception{
        // given
        Long productId = 1L;
        Long memberId = 1L;
        Like like = new Like(1L, 1L, 1L, false);

        //when
        when(likeService.getLike(productId, memberId)).thenReturn(null);
        when(likeRepository.save(any(Like.class))).thenReturn(like);
        LikeRespDto expected = LikeRespDto.from(like);
        LikeRespDto response = likeService.updateLikes(productId, memberId);

        // then
        assertEquals(expected.getLikeId(), response.getLikeId());
        assertEquals(expected.getMemberId(), response.getMemberId());
        assertEquals(expected.getProductId(), response.getProductId());
        assertEquals(expected.getDeletedYn(), response.getDeletedYn());

    }

    @Test
    void updateLikesFalseToTrue() {
        //given
        Long productId = 1L;
        Long memberId = 1L;
        Like like = new Like(1L, 1L, 1L, false);

        //when
        when(likeService.getLike(productId, memberId)).thenReturn(like);
        likeService.updateLikes(productId, memberId);

        //then
        assertEquals(true, like.getDeletedYn());
    }

    @Test
    void updateLikesTrueToFalse() {
        //given
        Long productId = 1L;
        Long memberId = 1L;
        Like like = new Like(1L, 1L, 1L, true);

        //when
        when(likeService.getLike(productId, memberId)).thenReturn(like);
        likeService.updateLikes(productId, memberId);

        //then
        assertEquals(false, like.getDeletedYn());
    }

    @Test
    void getLike() {
        //given
        Long productId = 1L;
        Long memberId = 1L;
        Like like = new Like(1L, 1L, 1L, true);

        //when
        when(likeRepository.findLikeByProductIdAndMemberId(productId, memberId)).thenReturn(like);
        Like expected = like;
        Like response = likeService.getLike(memberId, productId);

        //then
        assertEquals(expected.getLikeId() , response.getLikeId());
        assertEquals(expected.getMemberId() , response.getMemberId());
        assertEquals(expected.getProductId() , response.getProductId());
        assertEquals(expected.getDeletedYn() , response.getDeletedYn());

    }
}