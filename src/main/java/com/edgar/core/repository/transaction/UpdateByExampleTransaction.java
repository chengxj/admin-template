package com.edgar.core.repository.transaction;

import com.edgar.core.repository.handler.WhereHandler;
import com.mysema.query.sql.*;
import com.mysema.query.sql.dml.DefaultMapper;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.expr.BooleanExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 即使修改了实体的主键，SQLUpdateClause.populate(domain)方法也不会更新主键，如果实体的属性值为null，则不会更新。
 * withNullBindings：false会忽略domain中的null值
 */
public class UpdateByExampleTransaction<T> extends TransactionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateByExampleTransaction.class);

    private T domain;

    private final boolean withNullBindings;

    public UpdateByExampleTransaction(Builder<T> builder) {
        super(builder);
        this.domain = builder.getDomain();
        this.withNullBindings = builder.isWithNullBindings();
    }

    public Long execute() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.execute(new ConnectionCallback<Long>() {
            @Override
            public Long doInConnection(Connection connection) throws SQLException, DataAccessException {
                final SQLUpdateClause updateClause = new SQLUpdateClause(connection, configuration, pathBase);
                where(updateClause);
                set(updateClause);
                for (SQLBindings sqlBindings : updateClause.getSQL()) {
                    LOGGER.debug("update {} \nSQL[{}] \nparams:{}", getPathBase()
                            .getTableName(), sqlBindings.getSQL(), sqlBindings.getBindings());
                }
                return updateClause.execute();
            }
        });
    }

    private void set(SQLUpdateClause updateClause) {
        //不会更新主键
        if (withNullBindings) {
            updateClause.populate(domain, DefaultMapper.WITH_NULL_BINDINGS);
        } else {
            updateClause.populate(domain);
        }
    }

    private void where(final SQLUpdateClause updateClause) {
        WhereHandler handler = new WhereHandler(pathBase, example) {

            @Override
            public void doHandle(BooleanExpression expression) {
                updateClause.where(expression);
            }
        };
        handler.handle();
    }

    public static class Builder<T> extends TransactionBuilder {
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
            return new UpdateByExampleTransaction(this);
        }

        public T getDomain() {
            return domain;
        }

        public boolean isWithNullBindings() {
            return withNullBindings;
        }
    }
}
