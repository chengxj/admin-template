package com.edgar.core.repository;

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

    private RowMapper<T> rowMapper;

    private int page;

    private int pageSize;

    protected PageTransaction(PageTransactionBuilder<T> builder) {
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
        TransactionBuilder countBuilder = new CountTransaction.CountTransactionBuilder().dataSource(dataSource).configuration(configuration).pathBase(pathBase).example(example);
        Transaction countTransaction = countBuilder.build();

        Long totalRecords = countTransaction.execute();
        if (totalRecords <= offset) {
            page = (int) (totalRecords / pageSize);
            if (page == 0) {
                page = 1;
            }
            example.offset((page - 1) * pageSize);
        }
        TransactionBuilder queryBuilder = new QueryTransaction.QueryTransactionBuilder<T>().rowMapper(rowMapper).dataSource(dataSource).configuration(configuration).pathBase(pathBase).example(example);
        Transaction query = queryBuilder.build();
        List<T> records = query.execute();
        return Pagination.newInstance(page, pageSize, totalRecords, records);
    }

    public static class PageTransactionBuilder<T> extends TransactionBuilder {
        private RowMapper<T> rowMapper;

        private int page;

        private int pageSize;
        @Override
        public Transaction build() {
            return new PageTransaction<T>(this);
        }

        public PageTransactionBuilder rowMapper(RowMapper<T> rowMapper) {
            this.rowMapper = rowMapper;
            return this;
        }

        public PageTransactionBuilder page(int page) {
            this.page = page;
            return this;
        }

        public PageTransactionBuilder pageSize(int pageSize) {
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
