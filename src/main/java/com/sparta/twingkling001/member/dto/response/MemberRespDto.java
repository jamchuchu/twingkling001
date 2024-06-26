package com.sparta.twingkling001.member.dto.response;

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
    private LocalDateTime createdAt;
    private boolean deletedYn;
    private String email;
    private String password;
    private Role role;

    public static MemberRespDto from(Member member){
        return MemberRespDto.builder()
                .memberId(member.getMemberId())
                .createdAt(member.getCreatedAt())
                .deletedYn(member.getDeletedYn())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
