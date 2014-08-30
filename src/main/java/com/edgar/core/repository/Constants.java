package com.edgar.core.repository;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.MySQLTemplates;

/**
 * Created by Administrator on 2014/8/30.
 */
public class Constants {

    public static final Configuration CONFIGURATION = new Configuration(new MySQLTemplates());

    static {
        CONFIGURATION.addListener(new CacheListener());
    }
}
