package com.sparta.twingkling001.member.service;


import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.member.dto.response.SimpleMemberRespDto;
import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public SimpleMemberRespDto addMember(MemberReqDtoByMail memberReqDtoByMail) {
        Member member = Member.builder()
                .roleId(1L)
                .email(memberReqDtoByMail.getEmail())
                .password(memberReqDtoByMail.getPassword())
                .createdAt(LocalDateTime.now())
                .deletedYn(false)
                .build();
        return new SimpleMemberRespDto(memberRepository.save(member).getId());
    }
}
