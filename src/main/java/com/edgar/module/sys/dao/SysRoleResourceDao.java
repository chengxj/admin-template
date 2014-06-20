package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysRoleResource;
import com.edgar.module.sys.repository.querydsl.QSysRoleResource;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 角色资源的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysRoleResourceDao extends AbstractCrudRepositoryTemplate<Integer, SysRoleResource> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysRoleResource.sysRoleResource;
        }

        @Override
        public boolean cacheEnabled() {
                return true;
        }
}
