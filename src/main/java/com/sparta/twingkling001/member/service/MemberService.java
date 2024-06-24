package com.sparta.twingkling001.member.service;


import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.member.dto.response.SimpleMemberRespDto;
import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public SimpleMemberRespDto addMember(MemberReqDtoByMail memberReqDtoByMail) {
        Member member = Member.builder()
                .roleId(1L)
                .email(memberReqDtoByMail.getEmail())
                .password(passwordEncoder.encode(memberReqDtoByMail.getPassword()))
                .createdAt(LocalDateTime.now())
                .deletedYn(false)
                .build();
        return new SimpleMemberRespDto(memberRepository.save(member).getMemberId());
    }

    //이메일 형식 체크
    public boolean checkEmailForm(String email)   {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
    //중복 체크
    public boolean checkDuple(String email)   {
        Long size = memberRepository.getMemberSizeByEmail(email);
        if(size != 0){
            System.out.println(false);
            return false;
        }
        return true;
    }
}
