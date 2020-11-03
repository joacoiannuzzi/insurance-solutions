package com.insurance.solutions.app.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final String USERS = "/users/**";
    public static final String HEALTHCHECK = "/server/_healthcheck";
    public static final String CRUD_BOOKS = "/books/**";


    public static final String USER = "/users/**";
    public static final String CLIENT = "/clients/**";
    public static final String VEHICLE = "/vehicles/**";
    public static final String DRIVING_PROFILE = "/driving-profiles/**";
    public static final String MONITORING_SYSTEM = "/monitoring-systems/**";
    public static final String INSURANCE_COMPANY = "/insurance-companies/**";


    // Swagger configuration
    public static final String SWAGGER = "/swagger-ui.html";
    public static final String WEBJARS = "/webjars/**";
    public static final String V2API = "/v2/api-docs";
    public static final String UICONFIG = "/configuration/ui";
    public static final String SWAGGERRESOURCES = "/swagger-resources/**";
    public static final String SECURITYCONFIG = "/configuration/security";
}
