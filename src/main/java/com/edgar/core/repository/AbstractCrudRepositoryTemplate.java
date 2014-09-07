package com.edgar.core.repository;

import com.edgar.core.repository.transaction.Transaction;
import com.edgar.core.repository.transaction.TransactionConfig;
import com.edgar.core.repository.transaction.TransactionFactory;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.*;

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
public abstract class AbstractCrudRepositoryTemplate<PK, T> implements
        CrudRepository<PK, T> {

    private final Class<T> entityBeanType;

    private final Configuration configuration;

    @SuppressWarnings("unchecked")
    public AbstractCrudRepositoryTemplate() {
        this.entityBeanType = (Class<T>) (((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[1]);
        configuration = Constants.CONFIGURATION;
    }

    /**
     * 返回实体类的QueryDSL查询类
     *
     * @return RelationalPathBase
     */
    public abstract RelationalPathBase<?> getPathBase();

    public TransactionConfig config() {
        return new TransactionConfig(dataSource, configuration, getPathBase());
    }

    /**
     * 是否开启缓存
     *
     * @return 默认为false
     */
    public boolean cacheEnabled() {
        return false;
    }

    @Autowired
    private DataSource dataSource;

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

    /**
     * 返回主键值
     *
     * @return 如果主键只有一个，则直接返回该值，如果有多个，则返回MAP对象
     */
    public Object getPrimaryKeyValue(T domain) {
        Assert.notEmpty(getPathBase().getPrimaryKey().getLocalColumns(),
                getPathBase().getTableName() + "has 0 primaryKey");
        SqlParameterSource source = new BeanPropertySqlParameterSource(domain);
        if (getPathBase().getPrimaryKey().getLocalColumns().size() == 1) {
            Path<?> path = getPathBase().getPrimaryKey().getLocalColumns()
                    .get(0);
            return source.getValue(path.getMetadata().getName());
        }
        Map<String, Object> pkMap = new HashMap<String, Object>();
        for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
            pkMap.put(path.getMetadata().getName(),
                    source.getValue(path.getMetadata().getName()));
        }
        return pkMap;
    }

}
