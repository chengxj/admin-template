package com.edgar.core.repository.transaction;

import com.edgar.core.repository.handler.FieldHandler;
import com.edgar.core.repository.handler.OrderHandler;
import com.edgar.core.repository.handler.PageHandler;
import com.edgar.core.repository.handler.WhereHandler;
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

    private RowMapper<T> rowMapper;

    protected QueryTransaction(Builder<T> builder) {
        super(builder);
        this.rowMapper = builder.getRowMapper();
    }

    public List<T> execute() {
        Assert.notNull(example);
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
        final List<Path<?>> returnPaths = new ArrayList<Path<?>>();
        FieldHandler fieldHandler = new FieldHandler(pathBase, example) {
            @Override
            public void doHandle(List<Path<?>> paths) {
                returnPaths.addAll(paths);
            }
        };
        fieldHandler.handle();

        Path<?>[] pathArray = new Path<?>[returnPaths.size()];
        SQLBindings sqlBindings = sqlQuery.getSQL(returnPaths.toArray(pathArray));
        String sql = sqlBindings.getSQL();
        List<Object> args = sqlBindings.getBindings();
        LOGGER.debug("query {} \nSQL:{} \nparams:{}", pathBase
                .getTableName(), sql, args);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(sql, args.toArray(), rowMapper);
    }

    public static class Builder<T> extends TransactionBuilder {
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
