package com.edgar.core.repository;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.util.Assert;

import com.edgar.core.util.ExceptionFactory;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.rits.cloning.Cloner;

/**
 * DAO的父类
 * 
 * http://java.dzone.com/articles/implementing-declarative-and
 * 
 * @author Edgar Zhang
 * @version 1.0
 * 
 * @param <PK>
 * @param <T>
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
	@Qualifier("jdbcTemplate")
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Class<T> getEntityBeanType() {
		return entityBeanType;
	}

	public RowMapper<T> getRowMapper() {
		return BeanPropertyRowMapper.newInstance(entityBeanType);
	}

	@Override
	public List<T> query(final QueryExample example) {
		return query(example, UNRESOLVED);
	}

	@Override
	public <E> List<E> querySingleColumn(QueryExample example,
			Class<E> elementType) {
		Assert.notNull(example);
		Assert.notEmpty(example.getFields(), "fields cannot be null");
		SQLQuery sqlQuery = createSQLQuery(example);
		List<Path<?>> paths = getReturnPath(example);
		Assert.isTrue(paths.size() == 1, "only need one filed");
		Path<?>[] pathArray = new Path<?>[paths.size()];
		SQLBindings sqlBindings = sqlQuery.getSQL(paths.toArray(pathArray));
		String sql = sqlBindings.getSQL();
		List<Object> args = sqlBindings.getBindings();
		LOGGER.debug("query {} \nSQL:{} \nparams:{}", getPathBase()
				.getTableName(), sql, args);
		return jdbcTemplate.queryForList(sql, args.toArray(), elementType);
	}

	@Override
	public List<T> query(final QueryExample example, ExtendQuery extendQuery) {
		Assert.notNull(example);
		SQLQuery sqlQuery = createSQLQuery(example);
		List<Path<?>> paths = getReturnPath(example);
		extendQuery.addExtend(sqlQuery);
		extendQuery.addReturnPath(paths);
		Path<?>[] pathArray = new Path<?>[paths.size()];
		SQLBindings sqlBindings = sqlQuery.getSQL(paths.toArray(pathArray));
		String sql = sqlBindings.getSQL();
		List<Object> args = sqlBindings.getBindings();
		LOGGER.debug("query {} \nSQL:{} \nparams:{}", getPathBase()
				.getTableName(), sql, args);
		return jdbcTemplate.query(sql, args.toArray(), getRowMapper());
	}

	@Override
	public Pagination<T> pagination(QueryExample example, int page, int pageSize) {
		return pagination(example, page, pageSize, UNRESOLVED);
	}

	@Override
	public Pagination<T> pagination(QueryExample example, int page,
			int pageSize, ExtendQuery extendQuery) {
		Assert.notNull(example);
		LOGGER.debug("pagination query {},page:{},pageSize:{}", getPathBase()
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
		long totalRecords = getTotalRecord(example, extendQuery);
		if (totalRecords <= offset) {
			page = (int) (totalRecords / pageSize);
			if (page == 0) {
				page = 1;
			}
			example.offset((page - 1) * pageSize);
		}
		List<T> records = query(example, extendQuery);
		return Pagination.newInstance(page, pageSize, totalRecords, records);
	}

	@Override
	public int[] insert(List<T> domains) {
		Assert.notEmpty(domains, "domains cannot be empty");
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName(getPathBase().getTableName());
		List<SqlParameterSource> sources = new ArrayList<SqlParameterSource>(
				domains.size());
		for (T domain : domains) {
			sources.add(new BeanPropertySqlParameterSource(domain));
			LOGGER.debug("batch insert {} \nproperties:{}", getPathBase()
					.getTableName(), ToStringBuilder.reflectionToString(domain,
					ToStringStyle.SHORT_PREFIX_STYLE));
		}
		return jdbcInsert.executeBatch(sources
				.toArray(new SqlParameterSource[sources.size()]));
	}

	@Override
	public int insert(T domain) {
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName(getPathBase().getTableName());
		LOGGER.debug("insert {} \nproperties:{}", getPathBase().getTableName(),
				ToStringBuilder.reflectionToString(domain,
						ToStringStyle.SHORT_PREFIX_STYLE));
		return jdbcInsert.execute(new BeanPropertySqlParameterSource(domain));
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
	public int deleteByPk(PK pk) {
		Assert.notNull(pk, "primaryKey cannot be null");
		QueryExample example = createExampleByPk(pk);
		return delete(example);
	}

	@Override
	public int deleteByPkAndVersion(PK pk, long updatedTime) {
		Assert.notNull(pk, "primaryKey cannot be null");
		// Assert.hasText(updatedTime);
		QueryExample example = createExampleByPk(pk);
		example.equalsTo(UPDATED_TIME, new Timestamp(updatedTime));
		int result = delete(example);
		if (result < 1) {
			throw ExceptionFactory.expired();
		}
		return result;
	}

	@Override
	public int delete(final QueryExample example) {
		SQLQuery sqlQuery = createSQLQuery(example);
		SQLBindings sqlBindings = sqlQuery.getSQL(getPathBase().getColumns()
				.get(0));
		String whereSql = StringUtils.substringAfter(sqlBindings.getSQL(),
				"where");
		List<Object> whereArgs = sqlBindings.getBindings();

		StringBuilder deleteSql = new StringBuilder("delete from ")
				.append(getPathBase().getTableName());

		if (StringUtils.isNotBlank(whereSql)) {
			deleteSql.append(" where ").append(whereSql);
			LOGGER.debug("delete {} \nSQL:{} \nparams:{}", getPathBase()
					.getTableName(), deleteSql, whereArgs);
			return jdbcTemplate.update(deleteSql.toString(),
					whereArgs.toArray());
		}
		LOGGER.debug("delete {} \nSQL:{}", getPathBase().getTableName(),
				deleteSql);
		return jdbcTemplate.update(deleteSql.toString());
	}

	// @Override
	// public void lock(PK pk) {
	// StringBuilder updateSql = new StringBuilder("update ").append(
	// getPathBase().getTableName()).append(" set ");
	// Assert.notNull(pk, "primaryKey cannot be null");
	//
	// for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
	// String name = path.getMetadata().getName();
	// updateSql.append(name).append(" = ").append(name);
	// }
	//
	// List<Object> args = new ArrayList<Object>();
	// int numOfPk = getPathBase().getPrimaryKey().getLocalColumns().size();
	// Assert.isTrue(numOfPk > 0, "表不存在主键");
	// updateSql.append(" where ");
	// if (numOfPk == 1) {
	// updateSql.append(
	// getPathBase().getPrimaryKey().getLocalColumns().get(0)
	// .getMetadata().getName()).append(" = ?");
	// args.add(pk);
	// } else {
	// SqlParameterSource source = new BeanPropertySqlParameterSource(pk);
	// for (Path<?> path : getPathBase().getPrimaryKey().getLocalColumns()) {
	// String name = path.getMetadata().getName();
	// String humpName = humpName(name);
	// updateSql.append(name).append(" = ?");
	// args.add(source.getValue(humpName));
	// }
	// }
	// LOGGER.debug("锁{}表 \nSQL{} \nparams:{}", getPathBase().getTableName(),
	// updateSql, args);
	// jdbcTemplate.update(updateSql.toString(), args.toArray());
	// }

	@Override
	public int update(final T domain, QueryExample example) {
		Assert.notNull(domain, "domain cannot be null");
		if (example == null) {
			example = QueryExample.newInstance();
		}
		Set<String> pks = createPrimaryKeySet();
		pks.add(CREATED_TIME);
		pks.add(UPDATED_TIME);

		StringBuilder updateSql = new StringBuilder("update ").append(
				getPathBase().getTableName()).append(" set ");

		SQLQuery sqlQuery = createSQLQuery(example);
		SQLBindings sqlBindings = sqlQuery.getSQL(getPathBase().getColumns()
				.get(0));
		String whereSql = StringUtils.substringAfter(sqlBindings.getSQL(),
				"where");
		List<Object> whereArgs = sqlBindings.getBindings();

		List<String> setString = new ArrayList<String>();
		List<Object> args = new ArrayList<Object>();
		SqlParameterSource source = new BeanPropertySqlParameterSource(domain);
		List<Path<?>> columns = getPathBase().getColumns();
		for (Path<?> path : columns) {
			String name = path.getMetadata().getName();
			String humpName = humpName(name);
			if (pks.contains(name)) {
				continue;
			}
			Object value = source.getValue(humpName);
			if (value != null) {
				if (value instanceof String
						&& StringUtils.isBlank(value.toString())) {
					continue;
				}
				setString.add(underscoreName(name) + " = ?");
				args.add(value);
			}
		}
		Assert.notEmpty(setString, "update sql error");
		updateSql.append(StringUtils.join(setString.iterator(), " , "));
		if (StringUtils.isNotBlank(whereSql)) {
			updateSql.append(" where ").append(whereSql);
			args.addAll(whereArgs);
		}
		LOGGER.debug("update{} \nSQL{} \nparams:{}", getPathBase()
				.getTableName(), updateSql, args);
		return jdbcTemplate.update(updateSql.toString(), args.toArray());
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
	 * 根据字段，更新记录
	 * 
	 * @param domain
	 *            实体类
	 * @param pks
	 *            用作查询条件的字段
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
	 * 根据查询条件查询总数
	 * 
	 * @param example
	 *            查询条件
	 * @param extendQuery
	 *            扩展查询
	 * @return 总数
	 */
	private long getTotalRecord(final QueryExample example,
			ExtendQuery extendQuery) {
		final QueryExample COUNT_EXAMPLE = cloneExample(example);
		if (example.getMaxNumOfRecords() > 0) {
			COUNT_EXAMPLE.limit(example.getMaxNumOfRecords());
		} else {
			COUNT_EXAMPLE.limit(0);
		}
		COUNT_EXAMPLE.offset(0);
		SQLQuery sqlQuery = createSQLQuery(COUNT_EXAMPLE);
		extendQuery.addExtend(sqlQuery);
		SQLBindings sqlBindings = sqlQuery.getSQL(getPathBase().getPrimaryKey()
				.getLocalColumns().get(0));
		StringBuilder sql = new StringBuilder("select count(*) from ("
				+ sqlBindings.getSQL() + ") x");
		LOGGER.debug("query table: {}\nSQL:{} \nparams:{}", getPathBase()
				.getTableName(), sql, sqlBindings.getBindings());
		return jdbcTemplate.queryForObject(sql.toString(), sqlBindings
				.getBindings().toArray(), Long.class);
	}

	/**
	 * 根据主键创建查询条件
	 * 
	 * @param pk
	 *            主键
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
	 * 克隆查询条件
	 * 
	 * @param example
	 *            查询条件
	 * @return 克隆后的查询条件
	 */
	private QueryExample cloneExample(final QueryExample example) {
		Cloner cloner = new Cloner();
		return cloner.deepClone(example);
	}

	/**
	 * 根据查询条件创建SQLQuery
	 * 
	 * @param example
	 *            查询条件
	 * @return QueryDSL的查询核心类SQLQuery
	 */
	private SQLQuery createSQLQuery(QueryExample example) {
		SQLQuery sqlQuery = new SQLQuery(dialect);
		sqlQuery.from(getPathBase());
		addWhere(example, sqlQuery);
		addLimit(example, sqlQuery);
		addOffset(example, sqlQuery);
		addOrderBy(example, sqlQuery);
		return sqlQuery;
	}

	/**
	 * 根据查询条件设置返回值
	 * 
	 * @param example
	 *            查询条件
	 * @return 返回的值
	 */
	private List<Path<?>> getReturnPath(QueryExample example) {
		if (example.isAll()) {
			return Arrays.asList(getPathBase().all());
		} else {
			List<Path<?>> returnPaths = new ArrayList<Path<?>>();
			List<Path<?>> columns = getPathBase().getColumns();
			List<String> fields = example.getFields();
			for (String field : fields) {
				for (Path<?> path : columns) {
					if (checkColumn(path, field)) {
						returnPaths.add(path);
					}
				}
			}
			if (returnPaths.isEmpty()) {
				return Arrays.asList(getPathBase().all());
			}
			return returnPaths;
		}
	}

	/**
	 * 根据查询条件设置查询
	 * 
	 * @param example
	 *            查询条件
	 * @param sqlQuery
	 *            QueryDSL的查询核心类SQLQuery
	 */
	private void addWhere(QueryExample example, SQLQuery sqlQuery) {
		if (example.isValid()) {
			List<Path<?>> columns = getPathBase().getColumns();
			Set<Criteria> criterias = example.getCriterias();
			for (Criteria criteria : criterias) {
				for (Path<?> path : columns) {
					if (checkColumn(path, criteria.getField())) {
						sqlQuery.where(caseSqlOp(criteria, path));
					}
				}
			}
		}
	}

	/**
	 * 根据查询条件设置排序
	 * 
	 * @param example
	 *            查询条件
	 * @param sqlQuery
	 *            QueryDSL的查询核心类SQLQuery
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addOrderBy(QueryExample example, SQLQuery sqlQuery) {
		List<Path<?>> columns = getPathBase().getColumns();
		List<OrderBy> orderBies = example.getOrderBies();
		for (OrderBy orderBy : orderBies) {
			for (Path<?> path : columns) {
				if (checkColumn(path, orderBy.getField())) {
					if (path instanceof ComparableExpressionBase
							&& Comparable.class
									.isAssignableFrom(path.getType())) {
						ComparableExpressionBase<? extends Comparable> expressionBase = (ComparableExpressionBase<? extends Comparable>) path;
						sqlQuery.orderBy(caseOrderBy(orderBy, expressionBase));
					}
				}
			}
		}
	}

	/**
	 * 设置排序
	 * 
	 * @param orderBy
	 *            排序条件
	 * @param expressionBase
	 *            排序字段
	 * @return OrderSpecifier
	 */
	@SuppressWarnings("rawtypes")
	private OrderSpecifier<?> caseOrderBy(OrderBy orderBy,
			ComparableExpressionBase<? extends Comparable> expressionBase) {
		OrderSpecifier<?> specifier = null;
		switch (orderBy.getOrder()) {
		case ASC:
			specifier = expressionBase.asc();
			break;
		case DESC:
			specifier = expressionBase.desc();
			break;
		default:
			break;
		}
		return specifier;

	}

	/**
	 * 设置limit值，如果limit小于0，则不设置此值
	 * 
	 * @param example
	 *            查询条件
	 * @param sqlQuery
	 *            QueryDSL的查询核心类SQLQuery
	 */
	private void addLimit(QueryExample example, SQLQuery sqlQuery) {
		if (example.getLimit() > 0) {
			sqlQuery.limit(example.getLimit());
		}
	}

	/**
	 * 设置offset，如果offset小于0，则不设置此值
	 * 
	 * @param example
	 *            查询条件
	 * @param sqlQuery
	 *            QueryDSL的查询核心类SQLQuery
	 */
	private void addOffset(QueryExample example, SQLQuery sqlQuery) {
		if (example.getOffset() > 0) {
			sqlQuery.offset(example.getOffset());
		}
	}

	/**
	 * 生成不同的查询条件
	 * 
	 * @param criteria
	 *            查询标准
	 * @param path
	 *            查询字段
	 * @return QueryDSL的Expression
	 */
	@SuppressWarnings("unchecked")
	private BooleanExpression caseSqlOp(Criteria criteria, Path<?> path) {
		BooleanExpression expression = null;
		switch (criteria.getOp()) {
		case EQUALS_TO:
			expression = Expressions.predicate(Ops.EQ, path,
					Expressions.constant(criteria.getValue().toString()));
			break;
		case NOT_EQUALS_TO:
			expression = Expressions.predicate(Ops.NE, path,
					Expressions.constant(criteria.getValue().toString()));
			break;
		case GREATER_THAN:
			expression = Expressions.predicate(Ops.GT, path,
					Expressions.constant(criteria.getValue().toString()));
			break;
		case GREATER_THAN_AND_EQUALS_TO:
			expression = Expressions.predicate(Ops.GOE, path,
					Expressions.constant(criteria.getValue().toString()));
			break;
		case LESS_THAN:
			expression = Expressions.predicate(Ops.LT, path,
					Expressions.constant(criteria.getValue().toString()));
			break;
		case LESS_THAN_AND_EQUALS_TO:
			expression = Expressions.predicate(Ops.LOE, path,
					Expressions.constant(criteria.getValue().toString()));
			break;
		case LIKE:
			expression = Expressions.predicate(Ops.LIKE, path,
					Expressions.constant(criteria.getValue().toString()));
			break;
		case NOT_LIKE:
			expression = Expressions.predicate(Ops.LIKE, path,
					Expressions.constant(criteria.getValue().toString())).not();
			break;
		case IS_NULL:
			expression = Expressions.predicate(Ops.IS_NULL, path);
			break;
		case IS_NOT_NULL:
			expression = Expressions.predicate(Ops.IS_NOT_NULL, path);
			break;
		case IN:
			if (criteria.getValue() instanceof List<?>) {
				expression = Expressions.predicate(
						Ops.IN,
						path,
						new ConstantImpl<Collection<? extends String>>(
								(Collection<? extends String>) criteria
										.getValue()));
			}
			break;
		case NOT_IN:
			if (criteria.getValue() instanceof List<?>) {
				expression = Expressions.predicate(
						Ops.IN,
						path,
						new ConstantImpl<Collection<? extends String>>(
								(Collection<? extends String>) criteria
										.getValue())).not();
			}
			break;
		case BETWEEN:
			expression = Expressions.predicate(Ops.BETWEEN, path,
					Expressions.constant(criteria.getValue().toString()),
					Expressions.constant(criteria.getSecondValue().toString()));
			break;
		case NOT_BETWEEN:
			expression = Expressions.predicate(Ops.BETWEEN, path,
					Expressions.constant(criteria.getValue().toString()),
					Expressions.constant(criteria.getSecondValue().toString()))
					.not();
			break;
		default:
			break;
		}
		return expression;
	}

	/**
	 * 检查字段是否是数据库的字段
	 * 
	 * @param path
	 *            path
	 * @param field
	 *            字段名
	 * @return 是，返回true,不是，返回false
	 */
	public boolean checkColumn(Path<?> path, String field) {
        return path.getMetadata().getName().equals(field) || path.getMetadata().getName().equals(humpName(field));
    }

	/**
	 * 字符串转换，将alarmUserCode转换为alarm_user_code
	 * 
	 * @param source
	 *            需要转换的字符串
	 * @return 转换后的字符串
	 */
	public String underscoreName(String source) {
		Assert.hasLength(source);
		StringBuilder result = new StringBuilder();
		result.append(source.substring(0, 1).toLowerCase());
		for (int i = 1; i < source.length(); i++) {
			String s = source.substring(i, i + 1);
			String slc = s.toLowerCase();
			if (!s.equals(slc)) {
				result.append("_").append(slc);
			} else {
				result.append(s);
			}
		}
		return result.toString();
	}

	/**
	 * 字符串转换，将alarm_user_code转换为alarmUserCode
	 * 
	 * @param source
	 *            需要转换的字符串
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
