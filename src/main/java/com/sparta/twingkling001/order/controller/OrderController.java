package com.sparta.twingkling001.order.controller;

import com.sparta.twingkling001.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class OrderController {
    private final OrderService orderService;
}
