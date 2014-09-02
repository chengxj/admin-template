package com.edgar.core.repository.handler;

import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by Administrator on 2014/8/31.
 */
public class SQLDeleteClauseWhereHandler extends WhereHandler {

    private final SQLDeleteClause deleteClause;

    public SQLDeleteClauseWhereHandler(RelationalPathBase<?> pathBase, QueryExample example, SQLDeleteClause deleteClause) {
        super(pathBase, example);
        this.deleteClause = deleteClause;
    }

    @Override
    public void doHandle(BooleanExpression expression) {
        deleteClause.where(expression);
    }
}
