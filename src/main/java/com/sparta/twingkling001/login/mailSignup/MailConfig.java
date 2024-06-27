package com.sparta.twingkling001.login.mailSignup;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
@ToString
@Slf4j
public class MailConfig {
    private String username;
    private String password;
    private int port;
    private Properties properties;
    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//
        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername(username); // 네이버 아이디
        javaMailSender.setPassword(password); // 네이버 비밀번호


        javaMailSender.setPort(port); // 메일 인증서버 포트
        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }
}
