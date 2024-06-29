package com.sparta.twingkling001.login.jwt;

public class Constant {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN ="Refresh-Token";

    public static final String USER_AGENT = "User-Agent";
    public static final String Email = "Eamil";

    public static final String BEARER_PREFIX = "Bearer ";


    public static final long ACCESS_TOKEN_EXPIRATION_SECOND = 3600000;

    public static final long REFRESH_TOKEN_EXPIRATION_DAY = 30;
    public static final long REFRESH_TOKEN_EXPIRATION_SECOND = 30L * 24 * 60 * 60 * 1000;

}
