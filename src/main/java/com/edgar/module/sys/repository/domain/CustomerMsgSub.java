package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * CustomerMsgSub is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class CustomerMsgSub {

    private Integer cusMsgSubId;

    private String messageType;

    private String subscriber;

    public Integer getCusMsgSubId() {
        return cusMsgSubId;
    }

    public void setCusMsgSubId(Integer cusMsgSubId) {
        this.cusMsgSubId = cusMsgSubId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

}

