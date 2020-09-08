package com.dustinredmond.sharepoint;

public final class Token {

    private final String rtFa;
    private final String fedAuth;
    private final String domain;

    Token(String rtFa, String fedAuth, String domain) {
        this.rtFa = rtFa;
        this.fedAuth = fedAuth;
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

    static Token of(String rtFa, String fedAuth, String domain) {
        return new Token(rtFa, fedAuth, domain);
    }

}
