package com.edgar.core.repository.handler;

import com.edgar.core.repository.Criteria;
import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2014/8/26.
 */
public abstract class WhereHandler extends QueryExampleHandlerTemplate {

    public WhereHandler(RelationalPathBase<?> pathBase, QueryExample example) {
        super(pathBase,example);
    }

    public final void handle() {
        if (example.isValid()) {
            List<Path<?>> columns = pathBase.getColumns();
            Set<Criteria> criterias = example.getCriterias();
            for (Criteria criteria : criterias) {
                for (Path<?> path : columns) {
                    if (checkColumn(path, criteria.getField())) {
                        BooleanExpression expression = caseSqlOp(criteria, path);
                        doHandle(expression);
                    }
                }
            }
        }
    }

    public abstract void doHandle(BooleanExpression expression);

    /**
     * 生成不同的查询条件
     *
     * @param criteria
     *            查询标准
     * @param path
     *            查询字段
     * @return QueryDSL的Expression
     */
    @SuppressWarnings("unchecked")
    private BooleanExpression caseSqlOp(Criteria criteria, Path<?> path) {
        BooleanExpression expression = null;
        switch (criteria.getOp()) {
            case EQUALS_TO:
                expression = Expressions.predicate(Ops.EQ, path,
                        Expressions.constant(criteria.getValue().toString()));
                break;
            case NOT_EQUALS_TO:
                expression = Expressions.predicate(Ops.NE, path,
                        Expressions.constant(criteria.getValue().toString()));
                break;
            case GREATER_THAN:
                expression = Expressions.predicate(Ops.GT, path,
                        Expressions.constant(criteria.getValue().toString()));
                break;
            case GREATER_THAN_AND_EQUALS_TO:
                expression = Expressions.predicate(Ops.GOE, path,
                        Expressions.constant(criteria.getValue().toString()));
                break;
            case LESS_THAN:
                expression = Expressions.predicate(Ops.LT, path,
                        Expressions.constant(criteria.getValue().toString()));
                break;
            case LESS_THAN_AND_EQUALS_TO:
                expression = Expressions.predicate(Ops.LOE, path,
                        Expressions.constant(criteria.getValue().toString()));
                break;
            case LIKE:
                expression = Expressions.predicate(Ops.LIKE, path,
                        Expressions.constant(criteria.getValue().toString()));
                break;
            case NOT_LIKE:
                expression = Expressions.predicate(Ops.LIKE, path,
                        Expressions.constant(criteria.getValue().toString())).not();
                break;
            case IS_NULL:
                expression = Expressions.predicate(Ops.IS_NULL, path);
                break;
            case IS_NOT_NULL:
                expression = Expressions.predicate(Ops.IS_NOT_NULL, path);
                break;
            case IN:
                if (criteria.getValue() instanceof List<?>) {
                    expression = Expressions.predicate(
                            Ops.IN,
                            path,
                            new ConstantImpl<Collection<? extends String>>(
                                    (Collection<? extends String>) criteria
                                            .getValue()));
                }
                break;
            case NOT_IN:
                if (criteria.getValue() instanceof List<?>) {
                    expression = Expressions.predicate(
                            Ops.IN,
                            path,
                            new ConstantImpl<Collection<? extends String>>(
                                    (Collection<? extends String>) criteria
                                            .getValue())).not();
                }
                break;
            case BETWEEN:
                expression = Expressions.predicate(Ops.BETWEEN, path,
                        Expressions.constant(criteria.getValue().toString()),
                        Expressions.constant(criteria.getSecondValue().toString()));
                break;
            case NOT_BETWEEN:
                expression = Expressions.predicate(Ops.BETWEEN, path,
                        Expressions.constant(criteria.getValue().toString()),
                        Expressions.constant(criteria.getSecondValue().toString()))
                        .not();
                break;
            default:
                break;
        }
        return expression;
    }
}
