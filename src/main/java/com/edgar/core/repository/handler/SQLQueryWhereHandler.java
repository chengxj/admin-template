package com.edgar.core.repository.handler;

import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by Administrator on 2014/8/31.
 */
public class SQLQueryWhereHandler extends WhereHandler {
    private final SQLQuery sqlQuery;

    public SQLQueryWhereHandler(RelationalPathBase<?> pathBase, QueryExample example, SQLQuery sqlQuery) {
        super(pathBase, example);
        this.sqlQuery = sqlQuery;
    }

    @Override
    public void doHandle(BooleanExpression expression) {
        sqlQuery.where(expression);
    }
}
