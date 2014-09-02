package com.edgar.core.repository.transaction;

import com.edgar.core.repository.QueryExample;
import com.edgar.core.repository.handler.*;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by Administrator on 2014/8/25.
 */
public class QueryTransaction<T> extends TransactionTemplate {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueryTransaction.class);

    private final RowMapper<T> rowMapper;

    private final List<Path<?>> returnPaths = new ArrayList<Path<?>>();

    protected QueryTransaction(Builder<T> builder) {
        super(builder);
        this.rowMapper = builder.getRowMapper();
    }

    public List<T> execute() {
        Assert.notNull(example);
        final SQLQuery sqlQuery = new SQLQuery(configuration);
        sqlQuery.from(pathBase);
        handle(sqlQuery);
        Path<?>[] pathArray = new Path<?>[returnPaths.size()];
        SQLBindings sqlBindings = sqlQuery.getSQL(returnPaths.toArray(pathArray));
        String sql = sqlBindings.getSQL();
        List<Object> args = sqlBindings.getBindings();
        LOGGER.debug("query {} \nSQL:{} \nparams:{}", pathBase
                .getTableName(), sql, args);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(sql, args.toArray(), rowMapper);
    }

    private void handle(final SQLQuery sqlQuery) {
        List<QueryExampleHandler> handlers = new ArrayList<QueryExampleHandler>();
        WhereHandler whereHandler = new SQLQueryWhereHandler(pathBase, example, sqlQuery);
        handlers.add(whereHandler);
        PageHandler pageHandler = new PageHandler(pathBase, example, sqlQuery);
        handlers.add(pageHandler);
        OrderHandler orderHandler = new OrderHandler(pathBase, example, sqlQuery);
        handlers.add(orderHandler);
        FieldHandler fieldHandler = new FieldHandler(pathBase, example, returnPaths);
        handlers.add(fieldHandler);
        for (QueryExampleHandler handler : handlers) {
            handler.handle();
        }
    }

    public static class Builder<T> extends TransactionBuilderTemplate {
        private RowMapper<T> rowMapper;

        @Override
        public Transaction build() {
            return new QueryTransaction<T>(this);
        }

        public RowMapper<T> getRowMapper() {
            return rowMapper;
        }

        public Builder<T> rowMapper(RowMapper<T> rowMapper) {
            this.rowMapper = rowMapper;
            return this;
        }
    }

}
