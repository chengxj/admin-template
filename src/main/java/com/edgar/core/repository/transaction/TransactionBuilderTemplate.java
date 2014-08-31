package com.edgar.core.repository.transaction;

import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPathBase;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2014/8/29.
 */
public abstract class TransactionBuilderTemplate implements TransactionBuilder {
    private Configuration configuration;

    private RelationalPathBase<?> pathBase;

    private DataSource dataSource;

    private QueryExample example;

    public TransactionBuilderTemplate configuration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public TransactionBuilderTemplate pathBase(RelationalPathBase<?> pathBase) {
        this.pathBase = pathBase;
        return this;
    }

    public TransactionBuilderTemplate dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public TransactionBuilderTemplate example(QueryExample example) {
        this.example = example;
        return this;
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
