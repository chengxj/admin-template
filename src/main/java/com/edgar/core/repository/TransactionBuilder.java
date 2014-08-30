package com.edgar.core.repository;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLTemplates;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2014/8/29.
 */
public abstract class TransactionBuilder {
    private Configuration configuration;

    private RelationalPathBase<?> pathBase;

    private DataSource dataSource;

    private QueryExample example;

    public TransactionBuilder configuration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public TransactionBuilder pathBase(RelationalPathBase<?> pathBase) {
        this.pathBase = pathBase;
        return this;
    }

    public TransactionBuilder dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public TransactionBuilder example(QueryExample example) {
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

    public abstract Transaction build();
}
