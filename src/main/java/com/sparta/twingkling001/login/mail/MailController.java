package com.sparta.twingkling001.login.mail;

import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.member.service.MemberService;
import com.sparta.twingkling001.redis.RedisService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final MemberService memberService;
    private final RedisService redisService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signup(@RequestBody MemberReqDtoByMail memberReqDtoByMail) throws Exception {
        //email 형식 인증 추가


        String token = UUID.randomUUID().toString();
        memberReqDtoByMail.setToken(token);

        //db 저장
        redisService.setValues(memberReqDtoByMail.getEmail(), token);

        //메일 전송
        mailService.sendMail(token, memberReqDtoByMail.getEmail());

//         Long response = memberService.addMemberByMail(memberReqDtoByMail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.CREATED, 0L));
    }


    //토큰 검증 및 인증
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyEmail(HttpSession session, @RequestParam String email, String token){
        //토큰 일치시 DB 저장

        //토큰 검증
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.CREATED, mailService.checkToken(email, token)? "토큰일치": "토큰 불일치"));
//        }

    }
}
