package com.sparta.twingkling001.member.dto.request;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class MemberReqDtoByMail {
    private String nickname;
    private String email;
    private String password;

    private String token;

    public void setToken(String token) {
        this.token = token;
    }
}
