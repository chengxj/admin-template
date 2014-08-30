package com.edgar.core.repository;

import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.types.expr.BooleanExpression;
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
public class DeleteByExampleTransaction extends TransactionTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteByExampleTransaction.class);

    protected DeleteByExampleTransaction(DeleteByExampleTransactionBuilder builder) {
        super(builder);
    }

    @Override
    public Long execute() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.execute(new ConnectionCallback<Long>() {
            @Override
            public Long doInConnection(Connection connection) throws SQLException, DataAccessException {
                final SQLDeleteClause deleteClause = new SQLDeleteClause(connection, configuration, pathBase);
                WhereHandler handler = new WhereHandler(pathBase, example) {

                    @Override
                    public void doHandle(BooleanExpression expression) {
                        deleteClause.where(expression);
                    }
                };
                SQLBindings sqlBindings = deleteClause.getSQL().get(0);
                LOGGER.debug("update{} \nSQL{} \nparams:{}", getPathBase()
                        .getTableName(), sqlBindings.getSQL(), sqlBindings.getBindings());
                return deleteClause.execute();
            }
        });
    }

    public static class DeleteByExampleTransactionBuilder extends TransactionBuilder {
        @Override
        public Transaction build() {
            return new DeleteByExampleTransaction(this);
        }
    }
}
