package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * SysRoleResource is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class SysRoleResource {

    private Integer resourceId;

    private Integer roleId;

    private Integer roleResourceId;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getRoleResourceId() {
        return roleResourceId;
    }

    public void setRoleResourceId(Integer roleResourceId) {
        this.roleResourceId = roleResourceId;
    }

}

