package com.edgar.core.repository;

import com.google.common.collect.ImmutableList;
import com.mysema.commons.lang.Pair;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.*;
import com.mysema.query.sql.dml.SQLInsertBatch;
import com.mysema.query.sql.dml.SQLMergeBatch;
import com.mysema.query.sql.dml.SQLUpdateBatch;
import com.mysema.query.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/8/30.
 */
@Service
public class CacheListener implements SQLListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheListener.class);

    @Override
    public void notifyQuery(QueryMetadata md) {

    }

    @Override
    public void notifyDelete(RelationalPath<?> entity, QueryMetadata md) {

    }

    @Override
    public void notifyDeletes(RelationalPath<?> entity, List<QueryMetadata> batches) {

    }

    @Override
    public void notifyMerge(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> keys, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {

    }

    @Override
    public void notifyMerges(RelationalPath<?> entity, QueryMetadata md, List<SQLMergeBatch> batches) {

    }

    @Override
    public void notifyInsert(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {

    }

    @Override
    public void notifyInserts(RelationalPath<?> entity, QueryMetadata md, List<SQLInsertBatch> batches) {

    }

    @Override
    public void notifyUpdate(RelationalPath<?> entity, QueryMetadata md, List<Pair<Path<?>, Expression<?>>> updates) {
        Configuration configuration = new Configuration(new MySQLTemplates());
        SQLSerializer serializer = new SQLSerializer(configuration, true);
        serializer.serializeUpdate(md, entity, updates);
        SQLBindings sqlBindings = createBindings(md, serializer);
        LOGGER.error("update {} \nSQL[{}] \nparams:{}", entity
                .getTableName(), sqlBindings.getSQL(), sqlBindings.getBindings());
    }

    @Override
    public void notifyUpdates(RelationalPath<?> entity, List<SQLUpdateBatch> batches) {
        System.out.println("updates");
    }

    private SQLBindings createBindings(QueryMetadata metadata, SQLSerializer serializer) {
        String queryString = serializer.toString();
        ImmutableList.Builder<Object> args = ImmutableList.builder();
        Map<ParamExpression<?>, Object> params = metadata.getParams();
        for (Object o : serializer.getConstants()) {
            if (o instanceof ParamExpression) {
                if (!params.containsKey(o)) {
                    throw new ParamNotSetException((ParamExpression<?>) o);
                }
                o = metadata.getParams().get(o);
            }
            args.add(o);
        }
        return new SQLBindings(queryString, args.build());
    }
}
