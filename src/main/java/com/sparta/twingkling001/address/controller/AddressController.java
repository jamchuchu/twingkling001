package com.sparta.twingkling001.address.controller;

import com.sparta.twingkling001.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/address")
@RestController
public class AddressController {
    private final AddressService addressService;
}
