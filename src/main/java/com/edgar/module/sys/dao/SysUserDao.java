package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.repository.querydsl.QSysUser;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 系统用户的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysUserDao extends AbstractCrudRepositoryTemplate<Integer, SysUser> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysUser.sysUser;
        }

        @Override
        public boolean cacheEnabled() {
                return true;
        }

}
