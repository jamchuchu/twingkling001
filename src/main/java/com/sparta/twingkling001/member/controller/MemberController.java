package com.sparta.twingkling001.member.controller;

import com.sparta.twingkling001.address.dto.request.AddressReqDto;
import com.sparta.twingkling001.address.dto.response.AddressRespDto;
import com.sparta.twingkling001.address.dto.response.PublicRespDto;
import com.sparta.twingkling001.address.entity.Address;
import com.sparta.twingkling001.address.service.AddressService;
import com.sparta.twingkling001.api.exception.ErrorType;
import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
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
    private final MemberService memberService;
    private final MailService mailService;
    private final RedisService redisService;
    private final MemberAddressService memberAddressService;
    private final AddressService addressService;

    @GetMapping("/principal")
    public ResponseEntity<ApiResponse<?>> getPrincipal() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, memberService.getPrincipal()));
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody MemberReqDtoByMail memberReqDtoByMail) throws Exception {
        //email 형식 인증 추가
        if(!memberService.checkEmailForm(memberReqDtoByMail.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(ErrorType.INVALID_EMAIL_FORM));
        }

        if(!memberService.checkDuple(memberReqDtoByMail.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(ErrorType.INVALID_EMAIL_DUPLE));
        }

        String token = UUID.randomUUID().toString();
        memberReqDtoByMail.setToken(token);

        //redis 저장
        redisService.setValues(memberReqDtoByMail.getEmail(), memberReqDtoByMail);

        //메일 전송
        mailService.sendMail(token, memberReqDtoByMail.getEmail());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SEND_EMAIL, SuccessType.SEND_EMAIL.getMessage()));
    }


    //토큰 검증 및 인증
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyEmail( @RequestParam String email, String token){
        //토큰 일치시 DB 저장
        if(mailService.checkToken(email, token)){
            Long response = memberService.addMember(redisService.getValues(email, MemberReqDtoByMail.class));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
        }else{
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(ErrorType.INVALID_TOKEN,  ErrorType.INVALID_TOKEN.getMessage()));

        }
    }



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
    public ResponseEntity<ApiResponse<?>> updateMemberDetail(@PathVariable Long memberId, @RequestBody MemberDetailReqDto reqDto) throws DataNotFoundException {
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
    @PutMapping("/address/{memberAddressId}/{newAddressId}")
    public ResponseEntity<ApiResponse<?>> updateMemberAddress(@PathVariable Long memberAddressId, @PathVariable Long newAddressId) {
        memberAddressService.updateMemberAddress(memberAddressId, newAddressId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //주소 사용날짜 수정
    @PatchMapping("/address/{memberAddressId}")
    public ResponseEntity<ApiResponse<?>> updateMemberAddressUsedAt(@PathVariable Long memberAddressId,  MemberAddressReqDto reqDto) {
        memberAddressService.updateMemberAddressUsedAt(memberAddressId, reqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //주소 main -> sub
    @PatchMapping("/address/{memberAddressId}/sub")
    public ResponseEntity<ApiResponse<?>> updateMemberAddressPrimary (@PathVariable Long memberAddressId){
        memberAddressService.updateMemberAddressPrimary(memberAddressId);
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
