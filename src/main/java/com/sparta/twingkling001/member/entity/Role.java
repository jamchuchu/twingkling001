package com.sparta.twingkling001.member.entity;

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

    public static <T> Role fromRoleName(String roleName) {
        if(roleName.equals("USER")) return USER;
        if(roleName.equals("SELLER")) return SELLER;
        else return null;
    }
}