package com.edgar.core.auth;

import com.edgar.core.command.Command;

/**
 * Created by Administrator on 2014/8/20.
 */
public class LogoutCommand implements Command {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
