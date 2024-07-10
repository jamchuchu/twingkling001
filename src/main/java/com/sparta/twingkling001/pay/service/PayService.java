package com.sparta.twingkling001.pay.service;

import com.sparta.twingkling001.pay.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PayService {
    private final PayRepository payRepository;

}
