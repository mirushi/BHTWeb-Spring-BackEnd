package com.bhtcnpm.website.constant.security;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000; //5 days expressed in milliseconds.
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String BHTCNPM_ORG = "Ban hoc tap Cong nghe Phan mem";
    public static final String BHTCNPM_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    //Allow to be accessed without any security.
    public static final String[] PUBLIC_URLS = {"/user/login", "/user/register", "/user/resetpassword/**", "/resources/**"};
    public static final String[] DEV_PUBLIC_URLS = {"/h2-console/**", "/swagger-ui/**", "/v3/**"};
}
