package com.edgar.module.sys.repository.domain;

import javax.annotation.Generated;

/**
 * Test2Table is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class Test2Table {

    private java.sql.Timestamp createdTime;

    private String dictName;

    private String parentCode;

    private Integer sorted;

    private String testCode2;

    private String testId;

    private java.sql.Timestamp updatedTime;

    public java.sql.Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(java.sql.Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getSorted() {
        return sorted;
    }

    public void setSorted(Integer sorted) {
        this.sorted = sorted;
    }

    public String getTestCode2() {
        return testCode2;
    }

    public void setTestCode2(String testCode2) {
        this.testCode2 = testCode2;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public java.sql.Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(java.sql.Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

}

