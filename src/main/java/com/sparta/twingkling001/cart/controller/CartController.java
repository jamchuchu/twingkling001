package com.sparta.twingkling001.cart.controller;

import com.sparta.twingkling001.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/cart")
@RestController
public class CartController {
    private final CartService cartService;
}
