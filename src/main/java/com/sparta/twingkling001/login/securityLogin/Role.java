package com.sparta.twingkling001.login.securityLogin;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    USER("USER"),
    SELLER("SELLER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static Role fromRoleId(long roleId) {
        switch ((int) roleId) {
            case 1: return USER;
            case 2: return SELLER;
            default: return null;
        }
    }
}