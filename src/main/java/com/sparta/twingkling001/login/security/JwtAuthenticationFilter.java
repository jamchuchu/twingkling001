package com.sparta.twingkling001.login.security;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.twingkling001.login.LoginReqDto;
import com.sparta.twingkling001.login.jwt.JwtService;
import com.sparta.twingkling001.login.jwt.JwtToken;
import com.sparta.twingkling001.login.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
//        setFilterProcessesUrl("/api/v1/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginReqDto loginReqDto = new ObjectMapper().readValue(request.getInputStream(), LoginReqDto.class);

            log.info("username: " + loginReqDto.getEmail());
            log.info("password: " + loginReqDto.getPassword());

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginReqDto.getEmail(),
                            loginReqDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error("입출력 오류: " + e.getMessage());
            throw new RuntimeException("Error reading request body", e);
        }
    }

    //인증 성공
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        JwtToken token = jwtService.createTokens(request.getHeader("User-Agent"), username);

        request.setAttribute("refreshToken", token.getRefreshToken());
        request.setAttribute(AUTHORIZATION_HEADER, token.getAccessToken());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}