package com.sparta.twingkling001.member.controller;

import com.sparta.twingkling001.address.dto.request.AddressReqDto;
import com.sparta.twingkling001.address.dto.response.AddressRespDto;
import com.sparta.twingkling001.address.dto.response.PublicRespDto;
import com.sparta.twingkling001.address.entity.Address;
import com.sparta.twingkling001.address.service.AddressService;
import com.sparta.twingkling001.api.exception.ErrorType;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.login.mailSignup.MailService;
import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import com.sparta.twingkling001.member.dto.request.MemberDetailReqDto;
import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.member.dto.response.MemberAddressRespDto;
import com.sparta.twingkling001.member.dto.response.MemberDetailRespDto;
import com.sparta.twingkling001.member.dto.response.MemberRespDto;
import com.sparta.twingkling001.member.service.MemberAddressService;
import com.sparta.twingkling001.member.service.MemberService;
import com.sparta.twingkling001.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {
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
