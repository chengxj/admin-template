package com.edgar.core.repository.transaction;

import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.dml.DefaultMapper;
import com.mysema.query.sql.dml.SQLInsertClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2014/8/31.
 */
public class BatchInsertTransaction<T> extends TransactionTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchInsertTransaction.class);

    private List<T> domains;

    private final boolean withNullBindings;

    public BatchInsertTransaction(Builder<T> builder) {
        super(builder);
        this.domains = builder.getDomains();
        this.withNullBindings = builder.isWithNullBindings();
    }

    @Override
    public Long execute() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.execute(new ConnectionCallback<Long>() {
            @Override
            public Long doInConnection(Connection connection) throws SQLException, DataAccessException {
                final SQLInsertClause insertClause = new SQLInsertClause(connection, configuration, pathBase);
                for (T domain : domains) {
                    if (withNullBindings) {
                        insertClause.populate(domain, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
                    } else {
                        insertClause.populate(domain).addBatch();
                    }
                }
                for (SQLBindings sqlBindings : insertClause.getSQL()) {
                    LOGGER.debug("insert {} \nSQL[{}] \nparams:{}", getPathBase()
                            .getTableName(), sqlBindings.getSQL(), sqlBindings.getBindings());
                }

                return insertClause.execute();
            }
        });
    }

    public static class Builder<T> extends TransactionBuilderTemplate {
        private List<T> domains;
        private boolean withNullBindings = false;

        public Builder domains(List<T> domains) {
            this.domains = domains;
            return this;
        }

        public Builder withNullBindings(boolean withNullBindings) {
            this.withNullBindings = withNullBindings;
            return this;
        }

        @Override
        public Transaction build() {
            return new BatchInsertTransaction(this);
        }

        public List<T> getDomains() {
            return domains;
        }

        public boolean isWithNullBindings() {
            return withNullBindings;
        }
    }
}
