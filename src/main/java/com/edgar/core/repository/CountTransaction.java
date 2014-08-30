package com.edgar.core.repository;

import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import com.rits.cloning.Cloner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/8/25.
 */
public class CountTransaction extends TransactionTemplate {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CountTransaction.class);

    protected CountTransaction(CountTransactionBuilder builder) {
        super(builder);
    }

    public Long execute() {
        final QueryExample COUNT_EXAMPLE = cloneExample(example);
        if (example.getMaxNumOfRecords() > 0) {
            COUNT_EXAMPLE.limit(example.getMaxNumOfRecords());
        } else {
            COUNT_EXAMPLE.limit(0);
        }
        COUNT_EXAMPLE.offset(0);
        final SQLQuery sqlQuery = new SQLQuery(configuration);
        sqlQuery.from(pathBase);

        WhereHandler whereHandler = new WhereHandler(pathBase, COUNT_EXAMPLE) {

            @Override
            public void doHandle(BooleanExpression expression) {
                sqlQuery.where(expression);
            }
        };
        whereHandler.handle();
        PageHandler pageHandler = new PageHandler(pathBase, COUNT_EXAMPLE, sqlQuery);
        pageHandler.handle();
        OrderHandler orderHandler = new OrderHandler(pathBase, COUNT_EXAMPLE, sqlQuery);
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

    /**
     * 克隆查询条件
     *
     * @param example
     *            查询条件
     * @return 克隆后的查询条件
     */
    private QueryExample cloneExample(final QueryExample example) {
        Cloner cloner = new Cloner();
        return cloner.deepClone(example);
    }

    public static class CountTransactionBuilder extends TransactionBuilder {
        @Override
        public Transaction build() {
            return new CountTransaction(this);
        }
    }
}
