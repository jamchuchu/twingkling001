package com.sparta.twingkling001.config;

import com.sparta.twingkling001.login.token.JwtAuthenticationFilter;
import com.sparta.twingkling001.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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

                .authorizeHttpRequests((authorizationRequests) ->
                        authorizationRequests
                                .anyRequest().permitAll()
                                .requestMatchers("/", "/login/**", "/api/login/**").permitAll()
                                .requestMatchers("/users").hasRole(Role.USER.getAuthority())
//                                .requestMatchers("/sellers").hasRole(Role.SELLER.getAuthority())
                                .requestMatchers(new RegexRequestMatcher("^/api/.*/permit/.*$", null)).permitAll()
//                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
