package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * UserMessageSub is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class UserMessageSub {

    private String messageType;

    private String subscriber;

    private Integer userId;

    private Integer userMsgSubId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserMsgSubId() {
        return userMsgSubId;
    }

    public void setUserMsgSubId(Integer userMsgSubId) {
        this.userMsgSubId = userMsgSubId;
    }

}

