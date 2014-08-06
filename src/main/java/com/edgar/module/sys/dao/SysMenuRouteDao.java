package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.querydsl.QSysMenuRoute;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 菜单路由关联的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysMenuRouteDao extends AbstractCrudRepositoryTemplate<Integer, SysMenuRoute> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysMenuRoute.sysMenuRoute;
        }

        @Override
        public boolean cacheEnabled() {
                return true;
        }
}
