package com.sparta.twingkling001.login.security;

import com.sparta.twingkling001.login.jwt.JwtService;
import com.sparta.twingkling001.login.jwt.JwtUtil;
import com.sparta.twingkling001.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisService redisService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, JwtService jwtService, UserDetailsServiceImpl userDetailsService, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtService.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {
            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtService.getUserInfoFromToken(tokenValue);
            String username = info.get("username", String.class);

            try {
                setAuthentication(username);
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
        filterChain.doFilter(req, res);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }


    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


    //인가 처리 SecurityContextHolder에서 빼서 처리
    private void handleExpiredAccessToken(HttpServletRequest req, HttpServletResponse res, ExpiredJwtException e) {
        String refreshToken = req.getHeader("refreshToken");
        String email = jwtService.getEmailFromToken(refreshToken);
        String userAgent = jwtService.getClaimFromToken(refreshToken, "userAgent");

        if (StringUtils.hasText(refreshToken)) {
            String refreshKey = "email:" + email + ":userAgent:" + userAgent;
            String storedRefreshToken = redisService.getValues(refreshKey);

            //refresh 토큰의 유효기간 처리

            if (refreshToken.equals(storedRefreshToken)) {
                // Refresh 토큰이 유효한 경우, 새로운 Access 토큰 발급
                String newAccessToken = jwtService.createAccessToken(email);
                res.setHeader("accessToken", newAccessToken);

            } else {
                log.error("Invalid Refresh Token");
            }
        } else {
            log.error("Refresh Token not provided");
        }
    }

    //refresh에서 꺼내서 확인


}
