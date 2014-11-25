package com.edgar.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2014/11/25.
 */
public class DaoManager {

    private DataSource dataSource;

    public DaoManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 插入数据
     * 自动将Bean的Class转为对应的表名,如SysUser->sys_user
     *
     * @param bean 实体类
     * @return 成功插入的记录数
     */
    public int insert(Object bean) {
        String tableName = tableName(bean);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
        simpleJdbcInsert.withTableName(tableName);
        return simpleJdbcInsert.execute(new BeanPropertySqlParameterSource(bean));
    }

    /**
     * 插入数据并返回自动生成的主键。
     * 不支持联合主键
     * 自动将Bean的Class转为对应的表名,如SysUser->sys_user
     *
     * @param bean       实体类
     * @param columnName 主键名称
     * @return 主键值
     */
    public Number insertAndGetKey(Object bean, String columnName) {
        String tableName = tableName(bean);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
        simpleJdbcInsert.withTableName(tableName).usingGeneratedKeyColumns(columnName);
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(bean));
    }

    /**
     * 删除数据
     * 使用<code>delete from tableName where columnName = tableName</code>的sql语句删除数据
     *
     * @param tableName  表名
     * @param columnName 字段名
     * @param value      字段值
     * @return 删除的记录数
     */
    public int delete(String tableName, String columnName, Object value) {
        StringBuilder sql = new StringBuilder("delete from ").append(underscoreName(tableName)).append(" where ").append(underscoreName(columnName)).append(" = ?");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.update(sql.toString(), value);
    }

    /**
     * 修改数据
     * @param bean 实体类
     * @param columnName 字段名
     * @param value 字段值
     * @param withNullBinding 是否允许将null值设置到字段上
     * @return 修改的记录数
     */
    public int update(Object bean, String columnName, Object value, boolean withNullBinding) {
        StringBuilder sql = new StringBuilder("update ").append(tableName(bean));
        List<Object> args = new ArrayList<Object>();
        List<String> setArrays = new ArrayList<String>();
        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(bean);
        String[] propertyNames = sqlParameterSource.getReadablePropertyNames();
        for (String propertyName : propertyNames) {
            if (propertyName.equalsIgnoreCase("class")) {
                continue;
            }
            Object propertyValue = sqlParameterSource.getValue(propertyName);
            if (withNullBinding) {
                setArrays.add(underscoreName(propertyName) + "= ?");
                args.add(propertyValue);
            } else if (propertyValue != null) {
                setArrays.add(underscoreName(propertyName) + "= ?");
                args.add(propertyValue);
            }
        }
        Validate.notEmpty(setArrays);
        Validate.notEmpty(args);
        sql.append(" set ").append(StringUtils.join(setArrays, ", ")).append(" where ").append(underscoreName(columnName)).append(" = ?");
        args.add(value);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.update(sql.toString(), args.toArray());
    }

    private String tableName(Object bean) {
        String className = bean.getClass().getSimpleName();
        return underscoreName(className);
    }

    private String underscoreName(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }
}
