package com.edgar.core.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

/**
 * 无状态的TOKEN，用来校验rest API
 */
public class StatelessToken implements AuthenticationToken {
    private String username;
    private String baseString;
    private String digest;

    public StatelessToken(String username, String baseString, String digest) {
        this.username = username;
        this.baseString = baseString;
        this.digest = digest;
    }

    public String getBaseString() {
        return baseString;
    }

    public void setBaseString(String baseString) {
        this.baseString = baseString;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return digest;
    }
}
