package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * Company is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class Company {

    private String companyCode;

    private Integer companyId;

    private String companyName;

    private java.sql.Timestamp createdTime;

    private Boolean isDel;

    private java.sql.Timestamp updatedTime;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public java.sql.Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(java.sql.Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    public java.sql.Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(java.sql.Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

}

