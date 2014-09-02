package com.edgar.core.repository.transaction;

import com.edgar.core.repository.handler.SQLDeleteClauseWhereHandler;
import com.edgar.core.repository.handler.WhereHandler;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.dml.SQLDeleteClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2014/8/29.
 */
public class DeleteTransaction extends TransactionTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTransaction.class);

    protected DeleteTransaction(Builder builder) {
        super(builder);
    }

    @Override
    public Long execute() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.execute(new ConnectionCallback<Long>() {
            @Override
            public Long doInConnection(Connection connection) throws SQLException, DataAccessException {
                final SQLDeleteClause deleteClause = new SQLDeleteClause(connection, configuration, pathBase);
                WhereHandler handler = new SQLDeleteClauseWhereHandler(pathBase, example, deleteClause);
                handler.handle();
                for (SQLBindings sqlBindings : deleteClause.getSQL()) {
                    LOGGER.debug("delete {} \nSQL[{}] \nparams:{}", getPathBase()
                            .getTableName(), sqlBindings.getSQL(), sqlBindings.getBindings());
                }
                return deleteClause.execute();
            }
        });
    }

    public static class Builder extends TransactionBuilderTemplate {
        @Override
        public Transaction build() {
            return new DeleteTransaction(this);
        }
    }
}
