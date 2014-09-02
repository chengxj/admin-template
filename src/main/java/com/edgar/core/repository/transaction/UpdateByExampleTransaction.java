package com.edgar.core.repository.transaction;

import com.edgar.core.repository.Constants;
import com.edgar.core.repository.handler.SQLUpdateClauseWhereHandler;
import com.edgar.core.repository.handler.WhereHandler;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.dml.DefaultMapper;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 即使修改了实体的主键，SQLUpdateClause.populate(domain)方法也不会更新主键，如果实体的属性值为null，则不会更新。
 * withNullBindings：false会忽略domain中的null值
 */
public class UpdateByExampleTransaction<T> extends TransactionTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateByExampleTransaction.class);

    private final T domain;

    private final boolean withNullBindings;

    private final Set<String> ignoreColumns = new HashSet<String>();

    public UpdateByExampleTransaction(Builder<T> builder) {
        super(builder);
        this.domain = builder.getDomain();
        this.withNullBindings = builder.isWithNullBindings();
        this.ignoreColumns.addAll(builder.getIgnoreColumns());
    }

    public Long execute() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.execute(new ConnectionCallback<Long>() {
            @Override
            public Long doInConnection(Connection connection) throws SQLException, DataAccessException {
                final SQLUpdateClause updateClause = new SQLUpdateClause(connection, configuration, pathBase);
                where(updateClause);
                set(updateClause);
                for (SQLBindings sqlBindings : updateClause.getSQL()) {
                    LOGGER.debug("update {} \nSQL[{}] \nparams:{}", getPathBase()
                            .getTableName(), sqlBindings.getSQL(), sqlBindings.getBindings());
                }
                return updateClause.execute();
            }
        });
    }

    /**
     * 因为需要忽略createdTime、updatedTime，所以并没有直接使用 updateClause.populate(domain)或updateClause.populate(domain, DefaultMapper.WITH_NULL_BINDINGS)来实现set
     *
     * @param updateClause
     * @param obj
     * @param mapper
     */
    public void populate(SQLUpdateClause updateClause, T obj, DefaultMapper mapper) {
        Collection<? extends Path<?>> primaryKeyColumns = pathBase.getPrimaryKey() != null
                ? pathBase.getPrimaryKey().getLocalColumns()
                : Collections.<Path<?>>emptyList();
        Map<Path<?>, Object> values = mapper.createMap(pathBase, obj);
        for (Map.Entry<Path<?>, Object> entry : values.entrySet()) {
            if (!primaryKeyColumns.contains(entry.getKey()) && !ignoreColumns.contains(entry.getKey().getMetadata().getName())) {
                updateClause.set((Path) entry.getKey(), entry.getValue());
            }
        }
    }

    private void set(SQLUpdateClause updateClause) {
        //不会更新主键
        if (withNullBindings) {
            populate(updateClause, domain, DefaultMapper.WITH_NULL_BINDINGS);
        } else {
            populate(updateClause, domain, DefaultMapper.DEFAULT);
        }
    }

    private void where(final SQLUpdateClause updateClause) {
        WhereHandler handler = new SQLUpdateClauseWhereHandler(pathBase, example, updateClause);
        handler.handle();
    }

    public static class Builder<T> extends TransactionBuilderTemplate {
        private T domain;
        private boolean withNullBindings = false;
        private final Set<String> ignoreColumns = new HashSet<String>();

        public Builder domain(T domain) {
            this.domain = domain;
            return this;
        }

        public Builder withNullBindings(boolean withNullBindings) {
            this.withNullBindings = withNullBindings;
            return this;
        }

        public Builder defaultIgnore() {
            this.ignoreColumns.add(Constants.CREATED_TIME);
            this.ignoreColumns.add(Constants.UPDATED_TIME);
            return this;
        }

        public Builder addIgnore(String ignore) {
            this.ignoreColumns.add(ignore);
            return this;
        }

        @Override
        public Transaction build() {
            return new UpdateByExampleTransaction(this);
        }

        public T getDomain() {
            return domain;
        }

        public boolean isWithNullBindings() {
            return withNullBindings;
        }

        public Set<String> getIgnoreColumns() {
            return ignoreColumns;
        }
    }
}
