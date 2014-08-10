package com.edgar.core.repository;

public enum SqlOperator {

        /**
         * is null
         */
        IS_NULL,
        /**
         * is not null
         */
        IS_NOT_NULL,
        /**
         * =
         */
        EQUALS_TO,
        /**
         * <>
         */
        NOT_EQUALS_TO,
        /**
         * >
         */
        GREATER_THAN,
        /**
         * >=
         */
        GREATER_THAN_AND_EQUALS_TO,
        /**
         * 小于
         */
        LESS_THAN,
        /**
         * <=
         */
        LESS_THAN_AND_EQUALS_TO,
        /**
         * like
         */
        LIKE,
        /**
         * not like
         */
        NOT_LIKE,
        /**
         * in
         */
        IN,
        /**
         * not in
         */
        NOT_IN,
        /**
         * between
         */
        BETWEEN,
        /**
         * not between
         */
        NOT_BETWEEN
}
