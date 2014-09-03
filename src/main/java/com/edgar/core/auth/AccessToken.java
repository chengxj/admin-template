package com.edgar.core.auth;

public class AccessToken {


    private String accessToken;

    /**
     * 用于刷新Access Token 的 Refresh Token
     */
    private String refreshToken;

    /**
     * 基于http调用API时计算参数签名用的签名密钥
     */
    private String secretKey;

    /**
     * Access Token的有效期，以秒为单位
     */
    private long expiresIn;

    /**
     * 用户名
     */
    private String username;

    /**
     * 创建时间
     */
    private long createdTime;

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
