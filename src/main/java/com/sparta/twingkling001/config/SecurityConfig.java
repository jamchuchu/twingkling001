package com.sparta.twingkling001.config;

import com.sparta.twingkling001.login.securityLogin.Role;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.List;

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
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin((formLogin) -> {
                    formLogin
                            .loginProcessingUrl("/api/login/success")
                            .defaultSuccessUrl("/api/login/success")
                            .failureUrl("/api/login/permit/fail");
                })
                .logout((logoutConfig) -> {
                    logoutConfig
                            .logoutSuccessUrl("/api/login/permit/logout");
                })

                .authorizeHttpRequests((authoriationRequests) ->
                        authoriationRequests
//                                .anyRequest().permitAll()
                                .requestMatchers("/", "/login/**", "/api/login/**").permitAll()
                                .requestMatchers("/users").hasRole(Role.USER.getAuthority())
                                .requestMatchers("/sellers").hasRole(Role.SELLER.getAuthority())
                                .requestMatchers(new RegexRequestMatcher("^/api/.*/permit/.*$", null)).permitAll() // 정규 표현식을 사용한 requestMatchers
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        ;
        return http.build();
    }
}
