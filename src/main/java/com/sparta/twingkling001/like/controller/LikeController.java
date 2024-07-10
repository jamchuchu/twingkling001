package com.sparta.twingkling001.like.controller;

import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.like.dto.response.LikeRespDto;
import com.sparta.twingkling001.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/likes")
@RestController
public class LikeController {
    private final LikeService likeService;

    // 좋아요 상태변경
    @PatchMapping("/{productId}/{memberId}")
    public ResponseEntity<ApiResponse<LikeRespDto>> removeLikes (@PathVariable Long productId, @PathVariable Long memberId){
        LikeRespDto response = likeService.updateLikes(productId, memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }


}
