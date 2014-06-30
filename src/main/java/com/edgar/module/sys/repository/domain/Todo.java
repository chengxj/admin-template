package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * Todo is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class Todo {

    private Integer todoId;

    private String totoTitle;

    private Integer userId;

    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public String getTotoTitle() {
        return totoTitle;
    }

    public void setTotoTitle(String totoTitle) {
        this.totoTitle = totoTitle;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

