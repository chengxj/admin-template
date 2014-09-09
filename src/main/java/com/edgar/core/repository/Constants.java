package com.edgar.core.repository;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.MySQLTemplates;

/**
 * Created by Administrator on 2014/8/30.
 */
public class Constants {

    public static final Configuration CONFIGURATION = new Configuration(new MySQLTemplates());

    /**
     * 更新时间
     */
    public static final String UPDATED_TIME = "updatedTime";

    /**
     * 创建时间
     */
    public static final String CREATED_TIME = "createdTime";

    public static final String DEFAULT = "DEFAULT";
}
