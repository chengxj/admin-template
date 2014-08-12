package com.edgar.core.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

/**
 * 无状态的TOKEN，用来校验rest API
 */
public class StatelessToken implements AuthenticationToken {
    private String username;
    private Map<String, ?> params;
    private String digest;

    public StatelessToken(String username, Map<String, ?> params, String digest) {
        this.username = username;
        this.params = params;
        this.digest = digest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, ?> getParams() {
        return params;
    }

    public void setParams(Map<String, ?> params) {
        this.params = params;
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
