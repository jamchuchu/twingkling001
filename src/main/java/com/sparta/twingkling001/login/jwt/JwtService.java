package com.sparta.twingkling001.login.jwt;

import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.entity.Role;
import com.sparta.twingkling001.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    @Value("${jwt.expiration}")
    private long TOKEN_TIME;

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
        claims.put("email", email);

        String accessToken = BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별자값(ID)
                        .setClaims(claims)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 10분
                        .setIssuedAt(date) // 발급일
                        .signWith(key) // 암호화 알고리즘
                        .compact();

        claims = new HashMap<>();
        claims.put("email", email);
        claims.put("userAgent", userAgent);
        String refreshToken =
                Jwts.builder()
                        .setSubject(email)
                        .setClaims(claims)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME*10)) // 만료 1 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key) // 암호화 알고리즘
                        .compact();


        return JwtToken.from(accessToken, refreshToken);
    }

    public String createAccessToken(String email){
        Date date = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        String accessToken = BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별자값(ID)
                        .setClaims(claims)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key) // 암호화 알고리즘
                        .compact();
        return accessToken;
    }


    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
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
