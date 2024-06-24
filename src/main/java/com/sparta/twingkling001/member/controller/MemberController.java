package com.sparta.twingkling001.member.controller;

import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.member.dto.request.MemberDetailReqDto;
import com.sparta.twingkling001.member.dto.response.SimpleMemberDetailRespDto;
import com.sparta.twingkling001.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    //개인 추가 정보 등록 (아이디 생성시 자동 생성)
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<SimpleMemberDetailRespDto>> addMemberDetail(@RequestBody MemberDetailReqDto reqDto){
        SimpleMemberDetailRespDto response = memberService.addMemberDetail(reqDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
    }

    @PutMapping("/detail")
    public ResponseEntity<ApiResponse<SimpleMemberDetailRespDto>> updateMemberDetail(@RequestBody MemberDetailReqDto reqDto) {
        SimpleMemberDetailRespDto response = memberService.updateMemberDetail(reqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    @DeleteMapping("/detail/{memberId}")
    public ResponseEntity<ApiResponse<?>> addMemberDetail(@PathVariable long memberId) {
        memberService.deleteMemberDetail(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }


}
