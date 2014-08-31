package com.edgar.core.repository.transaction;

import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPathBase;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2014/8/29.
 */
public abstract class TransactionTemplate implements Transaction {

    protected Configuration configuration;

    protected RelationalPathBase<?> pathBase;

    protected DataSource dataSource;

    protected QueryExample example;

    protected TransactionTemplate(TransactionBuilderTemplate builder) {
        this.configuration = builder.getConfiguration();
        this.pathBase = builder.getPathBase();
        this.dataSource = builder.getDataSource();
        this.example = builder.getExample();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public RelationalPathBase<?> getPathBase() {
        return pathBase;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public QueryExample getExample() {
        return example;
    }

}
