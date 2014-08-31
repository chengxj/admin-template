package com.edgar.core.repository.transaction;

import com.edgar.core.repository.handler.OrderHandler;
import com.edgar.core.repository.handler.PageHandler;
import com.edgar.core.repository.handler.WhereHandler;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.expr.BooleanExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Administrator on 2014/8/25.
 */
public class CountTransaction extends TransactionTemplate {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CountTransaction.class);

    protected CountTransaction(Builder builder) {
        super(builder);
    }

    public Long execute() {
        final SQLQuery sqlQuery = new SQLQuery(configuration);
        sqlQuery.from(pathBase);

        WhereHandler whereHandler = new WhereHandler(pathBase, example) {

            @Override
            public void doHandle(BooleanExpression expression) {
                sqlQuery.where(expression);
            }
        };
        whereHandler.handle();
        PageHandler pageHandler = new PageHandler(pathBase, example, sqlQuery);
        pageHandler.handle();
        OrderHandler orderHandler = new OrderHandler(pathBase, example, sqlQuery);
        orderHandler.handle();
//        extendQuery.addExtend(sqlQuery);
        SQLBindings sqlBindings = sqlQuery.getSQL(pathBase.getPrimaryKey()
                .getLocalColumns().get(0));
        StringBuilder sql = new StringBuilder("select count(*) from ("
                + sqlBindings.getSQL() + ") x");
        LOGGER.debug("query table: {}\nSQL:{} \nparams:{}", pathBase
                .getTableName(), sql, sqlBindings.getBindings());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(sql.toString(), sqlBindings
                .getBindings().toArray(), Long.class);
    }

    public static class Builder extends TransactionBuilder {
        @Override
        public Transaction build() {
            return new CountTransaction(this);
        }
    }
}
