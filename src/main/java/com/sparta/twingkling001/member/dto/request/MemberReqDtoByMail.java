package com.sparta.twingkling001.member.dto.request;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberReqDtoByMail {
    private String nickname;
    private String email;
    private String password;
    private HttpSession session;

    private String token;

    public void setToken(String token) {
        this.token = token;
    }
}
