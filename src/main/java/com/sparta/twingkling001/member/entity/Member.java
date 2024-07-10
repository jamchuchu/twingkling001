package com.sparta.twingkling001.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private Boolean deletedYn;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDeletedYn(Boolean deletedYn) {
        this.deletedYn = deletedYn;
    }

    public static Member from(Role role, String email, String password){
        return Member.builder()
                .role(role)
                .email(email)
                .password(password)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
