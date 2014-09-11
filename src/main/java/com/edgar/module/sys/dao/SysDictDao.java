package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractDaoTemplate;
import com.edgar.module.sys.repository.domain.SysDict;
import com.edgar.module.sys.repository.querydsl.QSysDict;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 系统字典的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysDictDao extends AbstractDaoTemplate<String, SysDict> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysDict.sysDict;
        }

}
