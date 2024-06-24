package com.sparta.twingkling001.login.securityLogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.twingkling001.member.dto.response.MemberRespDto;
import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityLoginService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.getMemberByEmail(email);
        if(member == null){
            return null;
        }
        MemberRespDto memberRespDto = MemberRespDto.builder()
                .memberId(member.getMemberId())
                .createdAt(member.getCreatedAt())
                .deletedYn(member.getDeletedYn())
                .email(email)
                .password(member.getPassword())
                .role(Role.fromRoleId(Math.toIntExact(member.getRoleId())))
                .build();

        return new UserDetail(memberRespDto);
    }


}
