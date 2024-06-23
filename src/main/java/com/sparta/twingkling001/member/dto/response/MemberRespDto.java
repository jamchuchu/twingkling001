package com.sparta.twingkling001.member.dto.response;

import com.sparta.twingkling001.login.securityLogin.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberRespDto {
    private long memberId;
    private LocalDateTime createdAt;
    private boolean deletedYn;
    private String email;
    private String password;
    private Role role;
}
