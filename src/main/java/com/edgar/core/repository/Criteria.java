package com.edgar.core.repository;

import lombok.Data;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * 查询条件的类.
 * 
 * @author Edgar
 * @version 1.0
 * 
 */
@Data
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
                super();
                this.field = field;
                this.op = op;
                this.value = value;
                this.secondValue = secondValue;
        }

        @Override
        public int compareTo(Criteria o) {
                return new CompareToBuilder().append(field, o.getField()).append(op, o.getOp())
                                .append(value, o.getValue())
                                .append(secondValue, o.getSecondValue()).toComparison();

        }

}
