package com.sparta.twingkling001.pay.controller;

import com.sparta.twingkling001.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/order")
@RestController
public class PayController {
    private final PayService payService;
}
