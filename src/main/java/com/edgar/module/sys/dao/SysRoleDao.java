package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.querydsl.QSysRole;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 系统角色的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysRoleDao extends AbstractCrudRepositoryTemplate<Integer, SysRole> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysRole.sysRole;
        }

}
