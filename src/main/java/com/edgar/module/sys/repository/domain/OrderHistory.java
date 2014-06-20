package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * OrderHistory is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class OrderHistory {

    private String operatingTime;

    private String operator;

    private Integer operatorId;

    private Integer orderHistoryId;

    private Integer orderId;

    private String state;

    public String getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(String operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getOrderHistoryId() {
        return orderHistoryId;
    }

    public void setOrderHistoryId(Integer orderHistoryId) {
        this.orderHistoryId = orderHistoryId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}

