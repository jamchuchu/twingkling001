package com.sparta.twingkling001.login.security;

import com.sparta.twingkling001.member.entity.Role;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((authorizationRequests) ->
                                authorizationRequests
//                                .anyRequest().permitAll()
                                        .requestMatchers("/", "/login/**", "/api/login/**").permitAll()
//                                        .requestMatchers("/users").hasRole(Role.USER.getAuthority())
//                                        .requestMatchers("*/users").hasRole(Role.USER.getAuthority())
//                                .requestMatchers("/sellers").hasRole(Role.SELLER.getAuthority())
                                        .requestMatchers(new RegexRequestMatcher("^/api/.*/permit/.*$", null)).permitAll()
                                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
