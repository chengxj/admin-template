package com.edgar.core.repository;

import com.edgar.core.repository.transaction.*;
import com.edgar.core.util.ExceptionFactory;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Path;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
        TransactionBuilder builder = new QueryTransaction.Builder<T>().rowMapper(getRowMapper()).dataSource(dataSource).configuration(configuration).pathBase(getPathBase()).example(example);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public <E> List<E> querySingleColumn(QueryExample example,
                                         Class<E> elementType) {
        Assert.notNull(example);
        Assert.notEmpty(example.getFields(), "fields cannot be null");
        TransactionBuilder builder = new QueryTransaction.Builder<E>().rowMapper(new SingleColumnRowMapper<E>(elementType)).dataSource(dataSource).configuration(configuration).pathBase(getPathBase()).example(example);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public Pagination<T> pagination(QueryExample example, int page, int pageSize) {
        Assert.notNull(example);
        TransactionBuilder builder = new PageTransaction.Builder<T>().rowMapper(getRowMapper()).page(page).pageSize(pageSize).dataSource(dataSource).configuration(configuration).pathBase(getPathBase()).example(example);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public T get(PK pk) {
        Assert.notNull(pk, "primaryKey cannot be null");
        QueryExample example = createExampleByPk(pk);
        return uniqueResult(example);
    }

    @Override
    public T get(PK pk, List<String> fields) {
        Assert.notNull(pk, "primaryKey cannot be null");
        QueryExample example = createExampleByPk(pk);
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
        TransactionBuilder builder = new BatchInsertTransaction.Builder<T>().domains(domains).dataSource(dataSource).configuration(configuration).pathBase(getPathBase());
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public Long insert(T domain) {
        TransactionBuilder builder = new InsertTransaction.Builder<T>().domain(domain).dataSource(dataSource).configuration(configuration).pathBase(getPathBase());
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public Long deleteByPk(PK pk) {
        Assert.notNull(pk, "primaryKey cannot be null");
        QueryExample example = createExampleByPk(pk);
        return delete(example);
    }

    @Override
    public Long deleteByPkAndVersion(PK pk, long updatedTime) {
        Assert.notNull(pk, "primaryKey cannot be null");
        QueryExample example = createExampleByPk(pk);
        example.equalsTo(Constants.UPDATED_TIME, new Timestamp(updatedTime));
        long result = delete(example);
        if (result < 1) {
            throw ExceptionFactory.expired();
        }
        return result;
    }

    @Override
    public Long delete(final QueryExample example) {
        TransactionBuilder builder = new DeleteByExampleTransaction.Builder().dataSource(dataSource).configuration(configuration).pathBase(getPathBase()).example(example);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long update(final T domain, QueryExample example) {
        Assert.notNull(domain, "domain cannot be null");
        if (example == null) {
            QueryExample.newInstance();
        }
        TransactionBuilder builder = new UpdateByExampleTransaction.Builder<T>().defaultIgnore().domain(domain).dataSource(dataSource).configuration(configuration).pathBase(getPathBase()).example(example);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public Long update(final T domain) {
        Assert.notNull(domain, "domain cannot be null");
        QueryExample example = createUpdateExample(domain);
        return update(domain, example);
    }

    @Override
    public Long updateWithVersion(final T domain) {
        Assert.notNull(domain, "domain cannot be null");
        QueryExample example = createUpdateExampleWithVersion(domain);
        Long result = update(domain, example);
        if (result < 1) {
            throw ExceptionFactory.expired();
        }
        return result;
    }

    private QueryExample createUpdateExampleWithVersion(T domain) {
        Set<String> pks = new HashSet<String>();
        for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
            pks.add(path.getMetadata().getName());
        }
        pks.add(Constants.UPDATED_TIME);

        return createUpdateExample(domain, pks);
    }

    private QueryExample createUpdateExample(T domain, Set<String> pks) {
        QueryExample example = QueryExample.newInstance();
        SqlParameterSource source = new BeanPropertySqlParameterSource(domain);
        List<Path<?>> columns = getPathBase().getColumns();
        for (Path<?> path : columns) {
            String name = path.getMetadata().getName();
            String humpName = humpName(name);
            if (pks.contains(name)) {
                Assert.notNull(source.getValue(humpName), "the value of "
                        + name + "cannot be null");
                example.equalsTo(humpName, source.getValue(humpName));
            }
        }
        return example;
    }

    private QueryExample createUpdateExample(T domain) {
        Set<String> pks = new HashSet<String>();
        for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
            pks.add(path.getMetadata().getName());
        }

        return createUpdateExample(domain, pks);
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

    /**
     * 根据主键创建查询条件
     *
     * @param pk 主键
     * @return 查询条件
     */
    private QueryExample createExampleByPk(PK pk) {
        int numOfPk = getPathBase().getPrimaryKey().getLocalColumns().size();
        Assert.isTrue(numOfPk > 0, "primaryKey not exists");
        QueryExample example = QueryExample.newInstance();
        if (numOfPk == 1) {
            example.equalsTo(getPathBase().getPrimaryKey().getLocalColumns()
                    .get(0).getMetadata().getName(), pk);
        } else {
            SqlParameterSource source = new BeanPropertySqlParameterSource(pk);
            for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
                String name = path.getMetadata().getName();
                String humpName = humpName(name);
                example.equalsTo(humpName, source.getValue(name));
            }
        }
        return example;
    }


    /**
     * 字符串转换，将alarm_user_code转换为alarmUserCode
     *
     * @param source 需要转换的字符串
     * @return 转换后的字符串
     */
    private String humpName(final String source) {
        Assert.hasLength(source);
        if (StringUtils.contains(source, "_")) {
            String lowerSource = source.toLowerCase();
            String[] words = lowerSource.split("_");
            StringBuilder result = new StringBuilder();
            result.append(words[0]);
            int length = words.length;
            for (int i = 1; i < length; i++) {
                result.append(StringUtils.capitalize(words[i]));
            }
            return result.toString();
        }
        return source;
    }
}
