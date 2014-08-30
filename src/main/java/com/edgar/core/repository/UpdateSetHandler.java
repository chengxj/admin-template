package com.edgar.core.repository;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public abstract class UpdateSetHandler<T> extends QueryExampleHandlerTemplate {

    private T domain;

    public UpdateSetHandler(RelationalPathBase<?> pathBase, QueryExample example, T domain) {
        super(pathBase, example);
        this.domain = domain;
    }

    public final void handle() {

        List<String> setString = new ArrayList<String>();
        List<Object> args = new ArrayList<Object>();
        SqlParameterSource source = new BeanPropertySqlParameterSource(domain);
        List<Path<?>> columns = pathBase.getColumns();
        for (Path<?> path : columns) {
            String name = path.getMetadata().getName();
            String humpName = humpName(name);
//            if (pks.contains(name)) {
//                continue;
//            }
            Object value = source.getValue(humpName);
            doHandle(path, value);
        }
    }

    public abstract void doHandle(Path<?> path, Object value);
}
