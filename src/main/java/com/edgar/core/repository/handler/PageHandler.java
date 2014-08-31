package com.edgar.core.repository.handler;

import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;

/**
 * Created by Administrator on 2014/8/29.
 */
public class PageHandler extends QueryExampleHandlerTemplate {
    private SQLQuery sqlQuery;

    public PageHandler(RelationalPathBase<?> pathBase, QueryExample example, SQLQuery sqlQuery) {
        super(pathBase, example);
        this.sqlQuery = sqlQuery;
    }

    @Override
    public final void handle() {
        addLimit(example, sqlQuery);
       addOffset(example,sqlQuery);
    }

    /**
     * 设置limit值，如果limit小于0，则不设置此值
     *
     * @param example  查询条件
     * @param sqlQuery QueryDSL的查询核心类SQLQuery
     */
    private void addLimit(QueryExample example, SQLQuery sqlQuery) {
        if (example.getLimit() > 0) {
            sqlQuery.limit(example.getLimit());
        }
    }

    /**
     * 设置offset，如果offset小于0，则不设置此值
     *
     * @param example  查询条件
     * @param sqlQuery QueryDSL的查询核心类SQLQuery
     */
    private void addOffset(QueryExample example, SQLQuery sqlQuery) {
        if (example.getOffset() > 0) {
            sqlQuery.offset(example.getOffset());
        }
    }
}
