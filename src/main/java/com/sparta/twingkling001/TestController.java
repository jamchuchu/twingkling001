package com.sparta.twingkling001;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.login.jwt.JwtService;
import com.sparta.twingkling001.login.jwt.JwtToken;
import com.sparta.twingkling001.login.jwt.JwtUtil;

import com.sparta.twingkling001.member.service.MemberService;
import com.sparta.twingkling001.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {
    private final MemberService memberService;
    private final RedisService redisService;
    private final JwtService jwtService;

    // 토큰 만료시간
    @Value("${jwt.expiration}")
    private long TOKEN_TIME;

    @GetMapping("/redisTest")
    @ResponseBody
    public String testRedis() throws JsonProcessingException {
        redisService.setValues("num", 81);
        System.out.println(redisService.getValues("num"));
        redisService.setValues("num", 64);
        System.out.println(redisService.getValues("num"));

        return redisService.getValues("num");
    }

    @GetMapping("/helloWorld")
    @ResponseBody
    public String Test(){
        return "HELLO WORLD";
    }


    @GetMapping("/user")
    @ResponseBody
    public String testUser(){
        return "YOU ARE USER";
    }

//    @GetMapping("/sign-in")
//    @ResponseBody
//    public JwtToken signIn(HttpSession session, @RequestParam String email, String password) {
//        //토큰 만들기
//        JwtToken jwtToken = memberService.generateToken(email, password);
//        //session에 refresh 넣기
//        session.setAttribute("refreshToken" , jwtToken.getRefreshToken());
//        //redis에 refresh 넣기
//        redisService.setValues();
//        return jwtToken;
//    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(HttpServletRequest request, @RequestBody String email, String password) {

        String userAgent = request.getHeader("User-Agent").toString();

        JwtToken jwtToken = jwtService.login(userAgent, email, password);


        String refreshKey = "email:" + email + ":userAgent:" + userAgent;
        try {
            redisService.setValues(refreshKey, jwtToken.getRefreshToken());
        }catch (Exception e) {
            throw new IllegalStateException("refresh 토큰 삽입 실패");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, jwtToken));
    }


    @PostMapping("/test")
    public String test() {
        return "success";
    }

}
