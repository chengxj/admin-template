package com.edgar.module.sys.dao;

import com.edgar.core.repository.AbstractDaoTemplate;
import org.springframework.stereotype.Repository;

import com.edgar.module.sys.repository.domain.SysResource;
import com.edgar.module.sys.repository.querydsl.QSysResource;
import com.mysema.query.sql.RelationalPathBase;

/**
 * Rest资源的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysResourceDao extends AbstractDaoTemplate<Integer, SysResource> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysResource.sysResource;
        }

}
