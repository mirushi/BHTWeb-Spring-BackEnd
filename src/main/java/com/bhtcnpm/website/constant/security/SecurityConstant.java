package com.bhtcnpm.website.constant.security;

public class SecurityConstant {
    public static final int MAXIMUM_REFRESH_TOKEN_PER_USER = 20; //Number of refresh token that exist in DB for single user.
    //Allow to be accessed without any security.
    public static final String[] PUBLIC_URLS = {"/user/login", "/user/register", "/user/resetpassword/**", "/resources/**"};
    public static final String[] DEV_PUBLIC_URLS = {"/h2-console/**", "/swagger-ui/**", "/v3/**"};
}
