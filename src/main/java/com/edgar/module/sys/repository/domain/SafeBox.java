package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * SafeBox is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class SafeBox {

    private String boxCode;

    private Integer boxId;

    private Integer companyId;

    private Integer customerId;

    private Boolean enabled;

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}

