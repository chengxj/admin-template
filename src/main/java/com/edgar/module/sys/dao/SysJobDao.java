package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysJob;
import com.edgar.module.sys.repository.querydsl.QSysJob;
import com.mysema.query.sql.RelationalPathBase;

@Repository
public class SysJobDao extends AbstractCrudRepositoryTemplate<Integer, SysJob> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysJob.sysJob;
        }

}
