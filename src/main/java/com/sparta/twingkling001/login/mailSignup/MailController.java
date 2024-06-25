package com.sparta.twingkling001.login.mailSignup;

import com.sparta.twingkling001.api.exception.ErrorType;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.member.service.MemberService;
import com.sparta.twingkling001.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth/mail/naver")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final RedisService redisService;
    private final MemberService memberService;

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
}
