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

    /**
     * Deprecated, use SharePointTokenFactory.getToken() instead
     * @param username SharePoint username (email address) e.g. person@example.com
     * @param password SharePoint user's password
     * @param domain The subdomain of the SharePoint site e.g. myCompany
     * @return A token suitable for creating an instance of code SharePointAPI
     */
    @Deprecated(since = "1.0.1")
    public static Token create(String username, String password, String domain) {
        return TokenFactory.getToken(username, password, domain);
    }

}
