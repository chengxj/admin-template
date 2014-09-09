package com.edgar.module.sys.dao;

import com.edgar.core.repository.AbstractDaoTemplate;
import org.springframework.stereotype.Repository;

import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.querydsl.QSysRoleRoute;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 角色路由DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysRoleRouteDao extends AbstractDaoTemplate<Integer, SysRoleRoute> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysRoleRoute.sysRoleRoute;
        }

}
