package com.edgar.core.repository.transaction;

import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.dml.DefaultMapper;
import com.mysema.query.sql.dml.SQLInsertClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2014/8/31.
 */
public class InsertTransaction<T> extends TransactionTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertTransaction.class);

    private T domain;

    private final boolean withNullBindings;

    public InsertTransaction(Builder<T> builder) {
        super(builder);
        this.domain = builder.getDomain();
        this.withNullBindings = builder.isWithNullBindings();
    }

    @Override
    public Long execute() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.execute(new ConnectionCallback<Long>() {
            @Override
            public Long doInConnection(Connection connection) throws SQLException, DataAccessException {
                final SQLInsertClause insertClause = new SQLInsertClause(connection, configuration, pathBase);
                if (withNullBindings) {
                    insertClause.populate(domain, DefaultMapper.WITH_NULL_BINDINGS);
                } else {
                    insertClause.populate(domain);
                }
                SQLBindings sqlBindings = insertClause.getSQL().get(0);
                LOGGER.debug("insert {} \nSQL[{}] \nparams:{}", getPathBase()
                        .getTableName(), sqlBindings.getSQL(), sqlBindings.getBindings());
                return insertClause.execute();
            }
        });
    }

    public static class Builder<T> extends TransactionBuilderTemplate {
        private T domain;
        private boolean withNullBindings = false;

        public Builder domain(T domain) {
            this.domain = domain;
            return this;
        }

        public Builder withNullBindings(boolean withNullBindings) {
            this.withNullBindings = withNullBindings;
            return this;
        }

        @Override
        public Transaction build() {
            return new InsertTransaction(this);
        }

        public T getDomain() {
            return domain;
        }

        public boolean isWithNullBindings() {
            return withNullBindings;
        }
    }
}
