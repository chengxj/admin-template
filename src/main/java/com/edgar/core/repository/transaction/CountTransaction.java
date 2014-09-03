package com.edgar.core.repository.transaction;

import com.edgar.core.repository.QueryExample;
import com.edgar.core.repository.handler.*;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLQuery;
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

    private final QueryExample example;

    protected CountTransaction(TransactionConfig config, QueryExample example) {
        super(config);
        this.example = example;
    }

    public Long execute() {
        final SQLQuery sqlQuery = new SQLQuery(configuration);
        sqlQuery.from(pathBase);
        handle(sqlQuery);
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

    private void handle(final SQLQuery sqlQuery) {
        List<QueryExampleHandler> handlers = new ArrayList<QueryExampleHandler>();

        WhereHandler whereHandler = new SQLQueryWhereHandler(pathBase, example, sqlQuery);
        handlers.add(whereHandler);
        PageHandler pageHandler = new PageHandler(pathBase, example, sqlQuery);
        handlers.add(pageHandler);
        OrderHandler orderHandler = new OrderHandler(pathBase, example, sqlQuery);
        handlers.add(orderHandler);

        for (QueryExampleHandler handler : handlers) {
            handler.handle();
        }
    }

}
