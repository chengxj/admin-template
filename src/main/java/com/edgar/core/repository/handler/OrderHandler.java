package com.edgar.core.repository.handler;

import com.edgar.core.repository.OrderBy;
import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.ComparableExpressionBase;

import java.util.List;

/**
 * Created by Administrator on 2014/8/29.
 */
public class OrderHandler extends QueryExampleHandlerTemplate {

    private final SQLQuery sqlQuery;

    public OrderHandler(RelationalPathBase<?> pathBase, QueryExample example, SQLQuery sqlQuery) {
        super(pathBase, example);
        this.sqlQuery = sqlQuery;
    }

    public final void handle() {
        List<Path<?>> columns = pathBase.getColumns();
        List<OrderBy> orderBies = example.getOrderBies();
        for (OrderBy orderBy : orderBies) {
            for (Path<?> path : columns) {
                if (checkColumn(path, orderBy.getField())) {
                    if (path instanceof ComparableExpressionBase
                            && Comparable.class
                            .isAssignableFrom(path.getType())) {
                        ComparableExpressionBase<? extends Comparable> expressionBase = (ComparableExpressionBase<? extends Comparable>) path;
                        doHandle(caseOrderBy(orderBy, expressionBase));
                    }
                }
            }
        }
    }

    public void doHandle(OrderSpecifier<?> specifier) {
        sqlQuery.orderBy(specifier);
    }

    /**
     * 设置排序
     *
     * @param orderBy
     *            排序条件
     * @param expressionBase
     *            排序字段
     * @return OrderSpecifier
     */
    @SuppressWarnings("rawtypes")
    private OrderSpecifier<?> caseOrderBy(OrderBy orderBy,
                                          ComparableExpressionBase<? extends Comparable> expressionBase) {
        OrderSpecifier<?> specifier = null;
        switch (orderBy.getOrder()) {
            case ASC:
                specifier = expressionBase.asc();
                break;
            case DESC:
                specifier = expressionBase.desc();
                break;
            default:
                break;
        }
        return specifier;

    }
}
