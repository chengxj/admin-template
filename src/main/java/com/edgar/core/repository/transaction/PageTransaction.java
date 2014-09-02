package com.edgar.core.repository.transaction;

import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.rits.cloning.Cloner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by Administrator on 2014/8/25.
 */
public class PageTransaction<T> extends TransactionTemplate {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PageTransaction.class);

    private final RowMapper<T> rowMapper;

    private int page;

    private final int pageSize;

    protected PageTransaction(Builder builder) {
        super(builder);
        this.rowMapper = builder.getRowMapper();
        this.page = builder.getPage();
        this.pageSize = builder.getPageSize();
    }

    public Pagination<T> execute() {
        Assert.notNull(example);
        LOGGER.debug("pagination query {},page:{},pageSize:{}", pathBase
                .getTableName(), page, pageSize);
        Assert.isTrue(page > 0, "page must > 1");
        Assert.isTrue(pageSize > 0, "pageSize must > 0");
        if (example.getMaxNumOfRecords() > 0) {
            Assert.isTrue(page * pageSize <= example.getMaxNumOfRecords(),
                    "page * pageSize cannot >：" + example.getMaxNumOfRecords());
        }

        example.limit(pageSize);
        int offset = (page - 1) * pageSize;
        example.offset(offset);

        Long totalRecords = count();
        if (totalRecords <= offset) {
            page = (int) (totalRecords / pageSize);
            if (page == 0) {
                page = 1;
            }
            example.offset((page - 1) * pageSize);
        }
        TransactionBuilder queryBuilder = new QueryTransaction.Builder<T>().rowMapper(rowMapper).dataSource(dataSource).configuration(configuration).pathBase(pathBase).example(example);
        Transaction query = queryBuilder.build();
        List<T> records = query.execute();
        return Pagination.newInstance(page, pageSize, totalRecords, records);
    }

    private Long count() {
        final QueryExample COUNT_EXAMPLE = cloneExample(example);
        if (example.getMaxNumOfRecords() > 0) {
            COUNT_EXAMPLE.limit(example.getMaxNumOfRecords());
        } else {
            COUNT_EXAMPLE.limit(0);
        }
        COUNT_EXAMPLE.offset(0);
        TransactionBuilder countBuilder = new CountTransaction.Builder().dataSource(dataSource).configuration(configuration).pathBase(pathBase).example(COUNT_EXAMPLE);
        Transaction countTransaction = countBuilder.build();

        return countTransaction.execute();
    }

    /**
     * 克隆查询条件
     *
     * @param example 查询条件
     * @return 克隆后的查询条件
     */
    private QueryExample cloneExample(final QueryExample example) {
        Cloner cloner = new Cloner();
        return cloner.deepClone(example);
    }

    public static class Builder<T> extends TransactionBuilderTemplate {
        private RowMapper<T> rowMapper;

        private int page;

        private int pageSize;

        @Override
        public Transaction build() {
            return new PageTransaction<T>(this);
        }

        public Builder rowMapper(RowMapper<T> rowMapper) {
            this.rowMapper = rowMapper;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public RowMapper<T> getRowMapper() {
            return rowMapper;
        }

        public int getPage() {
            return page;
        }

        public int getPageSize() {
            return pageSize;
        }
    }
}
