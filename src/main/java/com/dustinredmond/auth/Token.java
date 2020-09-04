package com.dustinredmond.auth;

public class Token {

    private final String rtFa;
    private final String fedAuth;
    private String domain;

    Token(String rtFa, String fedAuth) {
        this.rtFa = rtFa;
        this.fedAuth = fedAuth;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }
    public String getRtFa() {
        return this.rtFa;
    }
    public String getFedAuth() {
        return this.fedAuth;
    }

    static Token of(String left, String right) {
        return new Token(left, right);
    }

}
