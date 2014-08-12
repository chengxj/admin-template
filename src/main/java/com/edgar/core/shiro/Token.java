package com.edgar.core.shiro;

public class Token {


    private String accessToken;

    /**
     * 用于刷新Access Token 的 Refresh Token
     */
    private String refreshToken;

    /**
     * 基于http调用API时计算参数签名用的签名密钥
     */
    private String sessionSecret;

    /**
     * Access Token的有效期，以秒为单位
     */
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
