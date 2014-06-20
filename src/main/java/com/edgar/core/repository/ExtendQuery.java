package com.edgar.core.repository;

import java.util.List;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Path;

/**
 * 扩展查询的接口
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public interface ExtendQuery {

        /**
         * 增加额外的查询条件
         * 
         * @param sqlQuery
         *                SQLQuery
         */
        void addExtend(final SQLQuery sqlQuery);

        /**
         * 增加额外的返回字段
         * 
         * @param paths
         *                Path的集合
         */
        void addReturnPath(final List<Path<?>> paths);

}
