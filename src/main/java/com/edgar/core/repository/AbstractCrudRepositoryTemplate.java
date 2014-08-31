package com.edgar.core.repository;

import com.edgar.core.repository.transaction.*;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.querydsl.QTestTable;
import com.mysema.query.sql.*;
import com.mysema.query.types.Path;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractCrudRepositoryTemplate.class);

    /**
     * 更新时间
     */
    private static final String UPDATED_TIME = "updatedTime";

    /**
     * 创建时间
     */
    private static final String CREATED_TIME = "createdTime";

    /**
     * MySQL的模板
     */
    private SQLTemplates dialect;

    private final Class<T> entityBeanType;

    private JdbcTemplate jdbcTemplate;

    private Configuration configuration;

    private static final ExtendQuery UNRESOLVED = new ExtendQuery() {

        @Override
        public void addReturnPath(List<Path<?>> paths) {
        }

        @Override
        public void addExtend(SQLQuery sqlQuery) {
        }
    };

    @SuppressWarnings("unchecked")
    public AbstractCrudRepositoryTemplate() {
        this.entityBeanType = (Class<T>) (((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[1]);
        setDialect();
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

    /**
     * 设置数据库方言
     */
    protected void setDialect() {
        this.dialect = new MySQLTemplates();
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("jdbcTemplate")
    protected void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        TransactionBuilder builder = new QueryTransaction.Builder<T>().rowMapper(getRowMapper()).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public <E> List<E> querySingleColumn(QueryExample example,
                                         Class<E> elementType) {
        Assert.notNull(example);
        Assert.notEmpty(example.getFields(), "fields cannot be null");
        TransactionBuilder builder = new QueryTransaction.Builder<E>().rowMapper(new SingleColumnRowMapper<E>(elementType)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public List<T> query(final QueryExample example, ExtendQuery extendQuery) {
        Assert.notNull(example);
        TransactionBuilder builder = new QueryTransaction.Builder<T>().rowMapper(getRowMapper()).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
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
    public Pagination<T> pagination(QueryExample example, int page,
                                    int pageSize, ExtendQuery extendQuery) {
        Assert.notNull(example);
        TransactionBuilder builder = new PageTransaction.Builder<T>().rowMapper(getRowMapper()).page(page).pageSize(pageSize).dataSource(dataSource).configuration(configuration).pathBase(getPathBase()).example(example);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public Long insert(List<T> domains) {
        Assert.notEmpty(domains, "domains cannot be empty");
        TransactionBuilder builder = new BatchInsertTransaction.Builder<T>().domains(domains).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable);
        Transaction transaction = builder.build();
        return transaction.execute();
    }

    @Override
    public Long insert(T domain) {
        TransactionBuilder builder = new InsertTransaction.Builder<T>().domain(domain).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable);
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
    public Long deleteByPk(PK pk) {
        Assert.notNull(pk, "primaryKey cannot be null");
        QueryExample example = createExampleByPk(pk);
        return delete(example);
    }

    @Override
    public Long deleteByPkAndVersion(PK pk, long updatedTime) {
        Assert.notNull(pk, "primaryKey cannot be null");
        // Assert.hasText(updatedTime);
        QueryExample example = createExampleByPk(pk);
        example.equalsTo(UPDATED_TIME, new Timestamp(updatedTime));
        long result = delete(example);
        if (result < 1) {
            throw ExceptionFactory.expired();
        }
        return result;
    }

    @Override
    public Long delete(final QueryExample example) {
        TransactionBuilder builder = new DeleteByExampleTransaction.Builder().configuration(configuration).pathBase(getPathBase()).example(example);
        Transaction transaction = builder.build();
        Long result = transaction.execute();
        return transaction.execute();
    }

    @Override
    public int update(final T domain, QueryExample example) {
        Assert.notNull(domain, "domain cannot be null");
        Assert.notNull(example, "example cannot be null");
        Set<String> pks = createPrimaryKeySet();
        pks.add(CREATED_TIME);
        pks.add(UPDATED_TIME);

        TransactionBuilder builder = new UpdateByExampleTransaction.Builder<T>().domain(domain).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        Long result = transaction.execute();
        return result.intValue();
    }

    @Override
    public int update(final T domain) {
        Assert.notNull(domain, "domain cannot be null");
        Set<String> pks = new HashSet<String>();
        for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
            pks.add(path.getMetadata().getName());
        }
        return updateByPk(domain, pks);
    }

    @Override
    public int updateByVersion(final T domain) {
        Assert.notNull(domain, "domain cannot be null");
        Set<String> pks = createPrimaryKeySet();
        pks.add(UPDATED_TIME);
        int result = updateByPk(domain, pks);
        if (result < 1) {
            throw ExceptionFactory.expired();
        }
        return result;
    }

    /**
     * 根据字段，更新记录
     *
     * @param domain 实体类
     * @param pks    用作查询条件的字段
     * @return 如果更新成功，返回1，更新失败，返回0
     */
    private int updateByPk(final T domain, Set<String> pks) {
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
        return update(domain, example);
    }

    /**
     * 返回主键的集合
     *
     * @return 主键的集合
     */
    private Set<String> createPrimaryKeySet() {
        Set<String> pks = new HashSet<String>();
        for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
            pks.add(path.getMetadata().getName());
        }
        return pks;
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
                example.equalsTo(humpName, source.getValue(humpName));
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
