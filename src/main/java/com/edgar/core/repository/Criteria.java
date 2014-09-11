package com.edgar.core.repository;


import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * 查询条件的类.
 *
 * @author Edgar
 * @version 1.0
 */
public class Criteria implements Comparable<Criteria> {

    /**
     * 查询字段，可以是实体类的属性
     */
    private String field;

    /**
     * 查询运算符
     */
    private SqlOperator op;

    /**
     * 查询参数
     */
    private Object value;

    /**
     * 查询参数，用于between的第二个参数
     */
    private Object secondValue;

    public Criteria(String field, SqlOperator op, Object value) {
        super();
        this.field = field;
        this.op = op;
        this.value = value;
    }

    public Criteria(String field, SqlOperator op) {
        super();
        this.field = field;
        this.op = op;
    }

    public Criteria(String field, SqlOperator op, Object value, Object secondValue) {
        this(field, op, value);
        this.secondValue = secondValue;
    }

    @Override
    public int compareTo(Criteria o) {
        return ComparisonChain.start().compare(this.field, o.getField()).compare(this.op, o.getOp(), Ordering.natural().nullsLast()).result();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Criteria criteria = (Criteria) o;
        return Objects.equal(this.field, criteria.getField())
                && Objects.equal(this.op, criteria.getOp())
                && Objects.equal(this.value, criteria.getValue())
                && Objects.equal(this.secondValue, criteria.getSecondValue());

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(field, op, value, secondValue);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SqlOperator getOp() {
        return op;
    }

    public void setOp(SqlOperator op) {
        this.op = op;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(Object secondValue) {
        this.secondValue = secondValue;
    }
}
