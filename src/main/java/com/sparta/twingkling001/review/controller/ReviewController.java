package com.sparta.twingkling001.review.controller;

import com.sparta.twingkling001.review.service.reviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@RestController
public class ReviewController {
    private final reviewService reivewService;

}
