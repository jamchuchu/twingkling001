package com.sparta.twingkling001.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.twingkling001.address.dto.request.AddressReqDto;
import com.sparta.twingkling001.address.dto.response.AddressRespDto;
import com.sparta.twingkling001.address.dto.response.PublicRespDto;
import com.sparta.twingkling001.address.service.AddressService;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/address")
@RestController
public class AddressController {
    private final AddressService addressService;

    @PostMapping(value="/total-public-addr")
    public ResponseEntity<ApiResponse<PublicRespDto>> getTotalAddrApi(@RequestParam String keyword, long currentPage, long countPerPage) throws Exception {
        PublicRespDto response = addressService.getPublicAddressAddress(keyword, currentPage, countPerPage);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
    }

    //주소 추가
    @PostMapping("/")
    public ResponseEntity<ApiResponse<?>> addAddress(@RequestBody AddressReqDto reqDto){
        AddressRespDto response = addressService.addAddress(reqDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
    }

    //주소 검색
    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<?>> getAddress(@PathVariable long addressId) {
        AddressRespDto response = addressService.getAddress(addressId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }


    //주소 삭제
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<?>> deleteAddress (@PathVariable long addressId){
        addressService.deleteAddress(addressId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }





}
