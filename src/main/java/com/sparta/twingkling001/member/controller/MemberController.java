package com.sparta.twingkling001.member.controller;

import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import com.sparta.twingkling001.member.dto.request.MemberDetailReqDto;
import com.sparta.twingkling001.member.dto.response.MemberAddressRespDto;
import com.sparta.twingkling001.member.dto.response.MemberDetailRespDto;
import com.sparta.twingkling001.member.dto.response.MemberRespDto;
import com.sparta.twingkling001.member.service.MemberAddressService;
import com.sparta.twingkling001.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final MemberAddressService memberAddressService;

    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMemberByMemberId(@PathVariable long memberId) {
        MemberRespDto response = memberService.getMemberByMemberId(memberId);
        System.out.println(response.getCreatedAt());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //이메일로 member 찾기
    @GetMapping("/email")
    public ResponseEntity<ApiResponse<MemberRespDto>> getMemberByEmail(@RequestParam String email) {
        MemberRespDto response = memberService.getMemberByEmail(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //패스워드 변경
    @PatchMapping("/{memberId}/password")
    public ResponseEntity<ApiResponse<?>> updatePassword (@PathVariable long memberId, @RequestBody String password){
        memberService.updateMemberPassword(memberId, password);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<?>> deleteMember (@PathVariable long memberId){
        memberService.deleteMember(memberId);
        memberService.deleteMemberDetail(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }


    //개인 추가 정보 등록 (아이디 생성시 자동 생성)
    @PostMapping("/{memberId}/detail")
    public ResponseEntity<ApiResponse<Long>> addMemberDetail(@RequestBody MemberDetailReqDto reqDto){
        Long response = memberService.addMemberDetail(reqDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
    }

    @GetMapping("/{memberId}/detail")
    public ResponseEntity<ApiResponse<MemberDetailRespDto>> getMemberDetail(@PathVariable long memberId) {
        MemberDetailRespDto response = memberService.getMemberDetail(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    @PutMapping("/{memberId}/detail")
    public ResponseEntity<ApiResponse<?>> updateMemberDetail(@PathVariable Long memberId, @RequestBody MemberDetailReqDto reqDto) {
        memberService.updateMemberDetail(reqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }




    //주소 작성
    @PostMapping("/address")
    public ResponseEntity<ApiResponse<Long>> addMemberAddress(@RequestBody MemberAddressReqDto reqDto) {
        Long response = memberAddressService.addMemberAddress(reqDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE , response));
    }

    @GetMapping("/address/{memberId}")
    public ResponseEntity<ApiResponse<List<MemberAddressRespDto>>> getMemberAddresses(@PathVariable Long memberId) {
        List<MemberAddressRespDto> response = memberAddressService.getMemberAddresses(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //주소 1개 수정
    @PutMapping("/address/{memberAddressId}")
    public ResponseEntity<ApiResponse<?>> updateMemberAddress(@PathVariable Long memberAddressId,  MemberAddressReqDto reqDto) {
        memberAddressService.updateMemberAddress(memberAddressId, reqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //서브 -> 메인으로 변경
    @PatchMapping("/address/{memberId}/{memberAddressId}")
    public ResponseEntity<ApiResponse<?>> updateMemberAddressLevel(@PathVariable Long memberId, Long memberAddressId) {
        memberAddressService.updateMemberAddressLevel(memberId, memberAddressId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }


    @DeleteMapping("/address/{memberAddressId}")
    public ResponseEntity<ApiResponse<?>> deleteMemberAddress(@PathVariable Long memberAddressId) {
        memberAddressService.deleteMemberAddress(memberAddressId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }




}
