package com.sparta.twingkling001.login.jwt;

import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.repository.MemberRepository;
import com.sparta.twingkling001.redis.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @Value("${jwt.secret}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
//    public Member signupForUser(String email, String password) {
//        Member newMember = Member.from(Role.USER, email, password);
//        return memberRepository.save(newMember);
//    }
//
//    public Member signupForSeller(String email, String password) {
//        Member newMember = Member.from(Role.SELLER, email, password);
//        return memberRepository.save(newMember);
//    }


    // 토큰 생성
    public JwtToken createTokens(String userAgent, String email) {
        Date date = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.Email, email);

        String accessToken = Constant.BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별자값(ID)
                        .setClaims(claims)
                        .setExpiration(new Date(date.getTime() + Constant.ACCESS_TOKEN_EXPIRATION_SECOND)) // 만료 10분
                        .setIssuedAt(date) // 발급일
                        .signWith(key) // 암호화 알고리즘
                        .compact();

        claims = new HashMap<>();
        claims.put(Constant.Email, email);
        claims.put(Constant.USER_AGENT, userAgent);
        String refreshToken =
                Jwts.builder()
                        .setSubject(email)
                        .setClaims(claims)
                        .setExpiration(new Date(date.getTime() + Constant.REFRESH_TOKEN_EXPIRATION_SECOND)) // 만료 1 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key) // 암호화 알고리즘
                        .compact();


        return JwtToken.from(accessToken, refreshToken);
    }

    public String createAccessToken(String email){
        Date date = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.Email, email);
        String accessToken = Constant.BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별자값(ID)
                        .setClaims(claims)
                        .setExpiration(new Date(date.getTime() + Constant.REFRESH_TOKEN_EXPIRATION_SECOND)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key) // 암호화 알고리즘
                        .compact();
        return accessToken;
    }



    //인가 처리 SecurityContextHolder에서 빼서 처리
    public String createAccessTokenFromRefreshToken( HttpServletRequest req, HttpServletResponse res) {
        String refreshToken = req.getHeader(Constant.REFRESH_TOKEN);

        //들고온 refresh Token valid 처리
        validateToken(refreshToken);

        String email = getClaimFromToken(refreshToken, Constant.Email);
        String userAgent = getClaimFromToken(refreshToken, Constant.USER_AGENT);

        String refreshKey = email + ":" + userAgent;
        String storedRefreshToken = redisService.getValues(refreshKey);
        validateToken(storedRefreshToken);

        String newAccessToken = null;
        if (refreshToken.equals(storedRefreshToken)) {
            // Refresh 토큰이 유효한 경우, 새로운 Access 토큰 발급
            newAccessToken = createAccessToken(email);
            res.setHeader(Constant.AUTHORIZATION_HEADER, newAccessToken);
        } else {
            log.error("Invalid Refresh Token");
        }
        return newAccessToken;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }




    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constant.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constant.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }


    public JwtToken login(String userAgent, String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return createTokens(userAgent, email);
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getClaimFromToken(String token, String claimName){
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get(claimName, String.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }



}
