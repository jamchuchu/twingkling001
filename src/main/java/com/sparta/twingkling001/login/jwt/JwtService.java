package com.sparta.twingkling001.login.jwt;

import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.entity.Role;
import com.sparta.twingkling001.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Member signupForUser(String email, String password) {
        Member newMember = Member.from(Role.USER, email, password);
        return memberRepository.save(newMember);
    }

    public Member signupForSeller(String email, String password) {
        Member newMember = Member.from(Role.SELLER, email, password);
        return memberRepository.save(newMember);
    }


    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtUtil.createToken(email, member.getRole());
    }
}
