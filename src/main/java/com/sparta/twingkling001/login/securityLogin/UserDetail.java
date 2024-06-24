package com.sparta.twingkling001.login.securityLogin;

import com.sparta.twingkling001.member.dto.response.MemberRespDto;
import com.sparta.twingkling001.member.entity.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserDetail implements UserDetails {
    private final MemberRespDto member;

    public UserDetail(MemberRespDto member) {
        this.member = member;
    }

    //아이디별 권한 하나씩만 부여
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(member.getRole());
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }


}
