package com.edgar.core.repository.handler;

import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by Administrator on 2014/8/31.
 */
public class SQLUpdateClauseWhereHandler extends WhereHandler {

    private final SQLUpdateClause updateClause;

    public SQLUpdateClauseWhereHandler(RelationalPathBase<?> pathBase, QueryExample example, SQLUpdateClause updateClause) {
        super(pathBase, example);
        this.updateClause = updateClause;
    }

    @Override
    public void doHandle(BooleanExpression expression) {
        updateClause.where(expression);
    }
}
