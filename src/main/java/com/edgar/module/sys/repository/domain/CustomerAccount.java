package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * CustomerAccount is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class CustomerAccount {

    private Integer accountId;

    private Boolean isDel;

    private String password;

    private String username;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

