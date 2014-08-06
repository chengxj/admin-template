package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysRoleRes;
import com.edgar.module.sys.repository.querydsl.QSysRoleRes;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 角色资源的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysRoleResDao extends AbstractCrudRepositoryTemplate<Integer, SysRoleRes> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysRoleRes.sysRoleRes;
        }

        @Override
        public boolean cacheEnabled() {
                return true;
        }
}
