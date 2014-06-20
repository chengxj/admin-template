package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * Contact is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class Contact {

    private Integer contactId;

    private java.sql.Timestamp createdTime;

    private Integer customerId;

    private String description;

    private String fullName;

    private String mobileNo;

    private java.sql.Timestamp updatedTime;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public java.sql.Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(java.sql.Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public java.sql.Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(java.sql.Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

}

