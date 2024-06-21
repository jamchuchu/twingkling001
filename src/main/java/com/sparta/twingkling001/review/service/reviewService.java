package com.sparta.twingkling001.review.service;

import com.sparta.twingkling001.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class reviewService {
    private final ReviewRepository reviewRepository;
}
