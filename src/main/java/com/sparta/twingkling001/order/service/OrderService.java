package com.sparta.twingkling001.order.service;

import com.sparta.twingkling001.order.repository.OrderDetailRepository;
import com.sparta.twingkling001.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository  orderRepository;
    private final OrderDetailRepository orderDetailRepository;
}
