package com.edgar.core.repository;

import com.edgar.core.repository.transaction.Transaction;
import com.edgar.core.repository.transaction.TransactionConfig;
import com.edgar.core.repository.transaction.TransactionFactory;
import com.google.common.base.Preconditions;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.Path;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DAO的父类
 *
 * @param <PK>
 * @param <T>
 * @author Edgar Zhang
 * @version 1.0
 */
public abstract class AbstractDaoTemplate<PK, T> implements
        BaseDao<PK, T> {

    private final Class<T> entityBeanType;

    private final Class<T> primaryKeyType;

    @SuppressWarnings("unchecked")
    public AbstractDaoTemplate() {
        this.primaryKeyType = (Class<T>) (((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]);
        this.entityBeanType = (Class<T>) (((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[1]);
    }

    /**
     * 返回实体类的QueryDSL查询类
     *
     * @return RelationalPathBase
     */
    public abstract RelationalPathBase<?> getPathBase();

    public String getDataSourceKey() {
        return Constants.DEFAULT;
    }

    public String getConfigurationKey() {
        return Constants.DEFAULT;
    }

    public TransactionConfig config() {
        return new TransactionConfig(DataSourceFactory.createDataSource(getDataSourceKey()), ConfigurationFactory.createConfiguration(getConfigurationKey()), getPathBase());
    }

    public Class<T> getEntityBeanType() {
        return entityBeanType;
    }

    public Class<T> getPrimaryKeyType() {
        return primaryKeyType;
    }

    protected RowMapper<T> getRowMapper() {
        return BeanPropertyRowMapper.newInstance(entityBeanType);
    }

    @Override
    public List<T> query(final QueryExample example) {
        Preconditions.checkNotNull(example);
        Transaction transaction = TransactionFactory.createQueryTransaction(config(), example, getRowMapper());
        return transaction.execute();
    }

    @Override
    public <E> List<E> querySingleColumn(QueryExample example,
                                         Class<E> elementType) {
        Preconditions.checkNotNull(example);
        Preconditions.checkArgument(example.getFields().size() == 1);
        Transaction transaction = TransactionFactory.createQueryTransaction(config(), example, new SingleColumnRowMapper<E>(elementType));
        return transaction.execute();
    }

    @Override
    public Pagination<T> pagination(QueryExample example, int page, int pageSize) {
        Preconditions.checkNotNull(example);
        Transaction transaction = TransactionFactory.createPageTransaction(config(), example, page, pageSize, getRowMapper());
        return transaction.execute();
    }

    @Override
    public T get(PK pk) {
        Preconditions.checkNotNull(pk, "primaryKey cannot be null");
        QueryExample example = QueryExampleHelper.createExampleByPk(getPathBase(), pk);
        return uniqueResult(example);
    }

    @Override
    public T get(PK pk, List<String> fields) {
        Preconditions.checkNotNull(pk, "primaryKey cannot be null");
        QueryExample example = QueryExampleHelper.createExampleByPk(getPathBase(), pk);
        example.addFields(fields);
        return uniqueResult(example);
    }

    @Override
    public T uniqueResult(QueryExample example) {
        List<T> records = query(example);
        if (records.isEmpty()) {
            return null;
        }
        Preconditions.checkArgument(records.size() == 1);
        return records.get(0);
    }

    @Override
    public Long insert(List<T> domains) {
        Preconditions.checkNotNull(domains);
        Preconditions.checkArgument(domains.size() > 0);
        Transaction transaction = TransactionFactory.createDefaultBatchInsertTransaction(config(), domains);
        return transaction.execute();
    }

    @Override
    public Long insert(T domain) {
        Transaction transaction = TransactionFactory.createDefaultInsertTransaction(config(), domain);
        return transaction.execute();
    }

    @Override
    public Long deleteByPk(PK pk) {
        Preconditions.checkNotNull(pk, "primaryKey cannot be null");
        QueryExample example = QueryExampleHelper.createExampleByPk(getPathBase(), pk);
        return delete(example);
    }

    @Override
    public Long deleteByPkAndVersion(PK pk, long updatedTime) {
        Preconditions.checkNotNull(pk, "primaryKey cannot be null");
        QueryExample example = QueryExampleHelper.createExampleByPk(getPathBase(), pk);
        example.equalsTo(Constants.UPDATED_TIME, new Timestamp(updatedTime));
        long result = delete(example);
        if (result < 1) {
            throw new StaleObjectStateException();
        }
        return result;
    }

    @Override
    public Long delete(final QueryExample example) {
        Transaction transaction = TransactionFactory.createDeleteTransaction(config(), example);
        return transaction.execute();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long update(final T domain, QueryExample example) {
        Preconditions.checkNotNull(domain, "domain cannot be null");
        if (example == null) {
            QueryExample.newInstance();
        }
        Transaction transaction = TransactionFactory.createDefaultUpdateTransaction(config(), domain, example);
        return transaction.execute();
    }

    @Override
    public Long update(final T domain) {
        Preconditions.checkNotNull(domain, "domain cannot be null");
        QueryExample example = QueryExampleHelper.createUpdateExample(getPathBase(), domain);
        return update(domain, example);
    }

    @Override
    public Long updateWithVersion(final T domain) {
        Preconditions.checkNotNull(domain, "domain cannot be null");
        QueryExample example = createUpdateExampleWithVersion(domain);
        Long result = update(domain, example);
        if (result < 1) {
            throw new StaleObjectStateException();
        }
        return result;
    }

    private QueryExample createUpdateExampleWithVersion(T domain) {
        Set<String> pks = new HashSet<String>();
        for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
            pks.add(path.getMetadata().getName());
        }
        pks.add(Constants.UPDATED_TIME);

        return QueryExampleHelper.createUpdateExample(getPathBase(), domain, pks);
    }

}
