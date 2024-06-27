package com.sparta.twingkling001.login.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
