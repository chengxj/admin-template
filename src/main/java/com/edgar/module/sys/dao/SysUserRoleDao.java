package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysUserRole;
import com.edgar.module.sys.repository.querydsl.QSysUserRole;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 用户角色的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysUserRoleDao extends AbstractCrudRepositoryTemplate<Integer, SysUserRole> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysUserRole.sysUserRole;
        }

        @Override
        public boolean cacheEnabled() {
                return true;
        }

}
