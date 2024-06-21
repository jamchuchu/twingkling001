package com.sparta.twingkling001.like.controller;

import com.sparta.twingkling001.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/likes")
@RestController
public class LikeController {
    private final LikeService likeService;
}
