package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysMenuRes;
import com.edgar.module.sys.repository.querydsl.QSysMenuRes;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 菜单资源的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysMenuResDao extends
		AbstractCrudRepositoryTemplate<Integer, SysMenuRes> {

	@Override
	public RelationalPathBase<?> getPathBase() {
		return QSysMenuRes.sysMenuRes;
	}

	@Override
	public boolean cacheEnabled() {
		return true;
	}

}
