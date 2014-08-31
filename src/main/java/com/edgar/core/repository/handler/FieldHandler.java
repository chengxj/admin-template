package com.edgar.core.repository.handler;

import com.edgar.core.repository.QueryExample;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2014/8/29.
 */
public abstract class FieldHandler extends QueryExampleHandlerTemplate {
    public FieldHandler(RelationalPathBase<?> pathBase, QueryExample example) {
        super(pathBase, example);
    }

    @Override
    public final void handle() {
        List<Path<?>> paths = getReturnPath();
        doHandle(paths);
    }

    public abstract void doHandle(List<Path<?>> paths);

    /**
     * 根据查询条件设置返回值
     *
     * @return 返回的值
     */
    protected final List<Path<?>> getReturnPath() {
        if (example.isAll()) {
            return Arrays.asList(pathBase.all());
        } else {
            List<Path<?>> returnPaths = new ArrayList<Path<?>>();
            List<Path<?>> columns = pathBase.getColumns();
            List<String> fields = example.getFields();
            for (String field : fields) {
                for (Path<?> path : columns) {
                    if (checkColumn(path, field)) {
                        returnPaths.add(path);
                    }
                }
            }
            if (returnPaths.isEmpty()) {
                return Arrays.asList(pathBase.all());
            }
            return returnPaths;
        }
    }
}
