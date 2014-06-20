package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysRouteRes;
import com.edgar.module.sys.repository.querydsl.QSysRouteRes;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 路由资源的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysRouteResDao extends AbstractCrudRepositoryTemplate<Integer, SysRouteRes> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysRouteRes.sysRouteRes;
        }

        @Override
        public boolean cacheEnabled() {
                return true;
        }

}
