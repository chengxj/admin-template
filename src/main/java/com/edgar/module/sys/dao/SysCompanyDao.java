package com.edgar.module.sys.dao;

import com.edgar.core.repository.AbstractDaoTemplate;
import org.springframework.stereotype.Repository;

import com.edgar.module.sys.repository.domain.SysCompany;
import com.edgar.module.sys.repository.querydsl.QSysCompany;
import com.mysema.query.sql.RelationalPathBase;

@Repository
public class SysCompanyDao extends
        AbstractDaoTemplate<Integer, SysCompany> {

	@Override
	public RelationalPathBase<?> getPathBase() {
		return QSysCompany.sysCompany;
	}

}
