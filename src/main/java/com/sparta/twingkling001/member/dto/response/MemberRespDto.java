package com.sparta.twingkling001.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.twingkling001.member.entity.Role;
import com.sparta.twingkling001.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MemberRespDto {
    private long memberId;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
    private boolean deletedYn;


    public static MemberRespDto from(Member member){
        return MemberRespDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .deletedYn(member.getDeletedYn())
                .build();
    }
}
