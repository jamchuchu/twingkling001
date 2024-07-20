package com.sparta.twingkling001;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.login.LoginReqDto;
import com.sparta.twingkling001.login.jwt.Constant;
import com.sparta.twingkling001.login.jwt.JwtService;
import com.sparta.twingkling001.login.jwt.JwtToken;

import com.sparta.twingkling001.member.entity.Role;
import com.sparta.twingkling001.member.service.MemberService;
import com.sparta.twingkling001.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {
    private final MemberService memberService;
    private final RedisService redisService;
    private final JwtService jwtService;



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
    public ResponseEntity<ApiResponse<?>> login(HttpServletRequest request, @RequestBody LoginReqDto reqDto) {
        String userAgent = request.getHeader(Constant.USER_AGENT);
        JwtToken jwtToken = jwtService.login(userAgent, reqDto.getEmail(), reqDto.getPassword());

        String refreshKey = reqDto.getEmail() + ":" + userAgent;

//        if (refreshKey == null) {
//        // 재 로그인 권유}
        try {
            redisService.setValues(refreshKey, jwtToken.getRefreshToken(), Duration.ofDays(Constant.REFRESH_TOKEN_EXPIRATION_DAY));
        }catch (Exception e) {
            throw new IllegalStateException("refresh 토큰 삽입 실패");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, jwtToken));
    }


    @GetMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> createAccessTokenFromRefreshToken(HttpServletRequest req, HttpServletResponse res) {
        String accessToken = jwtService.createAccessTokenFromRefreshToken(req, res);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, accessToken));

    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest req, HttpServletResponse res){
        String accessToken = jwtService.getJwtFromHeader(req);

        String email = jwtService.getEmailFromToken(accessToken);
        String userAgent = jwtService.getClaimFromToken(accessToken, Constant.USER_AGENT);
        String refreshKey = email + ":" + userAgent;

        redisService.deleteValues(refreshKey);

        res.setHeader(Constant.AUTHORIZATION_HEADER, null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //추가
    @PostMapping("/logout/all")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<?>> allLogout(HttpServletRequest req, HttpServletResponse res){
        String accessToken = jwtService.getJwtFromHeader(req);

        String email = jwtService.getEmailFromToken(accessToken);
        String refreshKey = email + "*";

        redisService.deleteValues(refreshKey);

        res.setHeader(Constant.AUTHORIZATION_HEADER, null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    @ResponseBody
    @PostMapping("/test")
    @PreAuthorize("hasAuthority('USER')")
        public String test() {
        return "success";
    }

}
