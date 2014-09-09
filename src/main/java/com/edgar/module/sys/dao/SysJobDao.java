package com.edgar.module.sys.dao;

import com.edgar.core.repository.AbstractDaoTemplate;
import org.springframework.stereotype.Repository;

import com.edgar.module.sys.repository.domain.SysJob;
import com.edgar.module.sys.repository.querydsl.QSysJob;
import com.mysema.query.sql.RelationalPathBase;

@Repository
public class SysJobDao extends AbstractDaoTemplate<Integer, SysJob> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysJob.sysJob;
        }

}
