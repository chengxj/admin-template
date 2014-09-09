package com.edgar.core.repository;

import com.edgar.core.repository.transaction.Transaction;
import com.edgar.core.repository.transaction.TransactionConfig;
import com.edgar.core.repository.transaction.TransactionFactory;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.Path;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DAO的父类
 * <p/>
 * http://java.dzone.com/articles/implementing-declarative-and
 *
 * @param <PK>
 * @param <T>
 * @author Edgar Zhang
 * @version 1.0
 */
public abstract class AbstractDaoTemplate<PK, T> implements
        BaseDao<PK, T> {

    private final Class<T> entityBeanType;

    private final Configuration configuration;

    @SuppressWarnings("unchecked")
    public AbstractDaoTemplate() {
        this.entityBeanType = (Class<T>) (((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[1]);
        System.out.println((Class<T>) (((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]));
        configuration = Constants.CONFIGURATION;
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

    public TransactionConfig config() {
        return new TransactionConfig(DataSourceFactory.createDataSource(getDataSourceKey()), configuration, getPathBase());
    }

    public Class<T> getEntityBeanType() {
        return entityBeanType;
    }

    protected RowMapper<T> getRowMapper() {
        return BeanPropertyRowMapper.newInstance(entityBeanType);
    }

    @Override
    public List<T> query(final QueryExample example) {
        Assert.notNull(example);
        Transaction transaction = TransactionFactory.createQueryTransaction(config(), example, getRowMapper());
        return transaction.execute();
    }

    @Override
    public <E> List<E> querySingleColumn(QueryExample example,
                                         Class<E> elementType) {
        Assert.notNull(example);
        Assert.notEmpty(example.getFields(), "fields cannot be null");
        Transaction transaction = TransactionFactory.createQueryTransaction(config(), example, new SingleColumnRowMapper<E>(elementType));
        return transaction.execute();
    }

    @Override
    public Pagination<T> pagination(QueryExample example, int page, int pageSize) {
        Assert.notNull(example);
        Transaction transaction = TransactionFactory.createPageTransaction(config(), example, page, pageSize, getRowMapper());
        return transaction.execute();
    }

    @Override
    public T get(PK pk) {
        Assert.notNull(pk, "primaryKey cannot be null");
        QueryExample example = QueryExampleHelper.createExampleByPk(getPathBase(), pk);
        return uniqueResult(example);
    }

    @Override
    public T get(PK pk, List<String> fields) {
        Assert.notNull(pk, "primaryKey cannot be null");
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
        Assert.isTrue(records.size() == 1);
        return records.get(0);
    }

    @Override
    public Long insert(List<T> domains) {
        Assert.notEmpty(domains, "domains cannot be empty");
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
        Assert.notNull(pk, "primaryKey cannot be null");
        QueryExample example = QueryExampleHelper.createExampleByPk(getPathBase(), pk);
        return delete(example);
    }

    @Override
    public Long deleteByPkAndVersion(PK pk, long updatedTime) {
        Assert.notNull(pk, "primaryKey cannot be null");
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
        Assert.notNull(domain, "domain cannot be null");
        if (example == null) {
            QueryExample.newInstance();
        }
        Transaction transaction = TransactionFactory.createDefaultUpdateTransaction(config(), domain, example);
        return transaction.execute();
    }

    @Override
    public Long update(final T domain) {
        Assert.notNull(domain, "domain cannot be null");
        QueryExample example = QueryExampleHelper.createUpdateExample(getPathBase(), domain);
        return update(domain, example);
    }

    @Override
    public Long updateWithVersion(final T domain) {
        Assert.notNull(domain, "domain cannot be null");
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
