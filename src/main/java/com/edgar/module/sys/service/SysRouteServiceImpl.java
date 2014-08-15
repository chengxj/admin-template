package com.edgar.module.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.domain.SysRoute;
import com.edgar.module.sys.validator.SysRouteUpdateValidator;
import com.edgar.module.sys.validator.SysRouteValidator;

/**
 * 路由的业务逻辑实现类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysRouteServiceImpl implements SysRouteService {
	@Autowired
	private CrudRepository<Integer, SysRoute> sysRouteDao;

	@Autowired
	private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;

	@Autowired
	private CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao;

	private final ValidatorStrategy validator = new SysRouteValidator();

	private final ValidatorStrategy updateValidator = new SysRouteUpdateValidator();

	@Override
	public List<SysRoute> findAll() {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("isRoot", 0);
		return sysRouteDao.query(example);
	}

    @Override
    public List<SysRoute> findAllWithRoot() {
        QueryExample example = QueryExample.newInstance();
        return sysRouteDao.query(example);
    }


	@Override
	@Transactional
	public int save(SysRoute sysRoute) {
		Assert.notNull(sysRoute);
		validator.validator(sysRoute);
		sysRoute.setIsRoot(false);
		sysRoute.setRouteId(IDUtils.getNextId());
		return sysRouteDao.insert(sysRoute);
	}

	@Override
	@Transactional
	public int update(SysRoute sysRoute) {
		updateValidator.validator(sysRoute);
		Assert.notNull(sysRoute);
		return sysRouteDao.update(sysRoute);
	}

	@Override
	public SysRoute get(int routeId) {
		return sysRouteDao.get(routeId);
	}

	@Override
	public Pagination<SysRoute> pagination(QueryExample example, int page,
			int pageSize) {
		example.equalsTo("isRoot", 0);
		return sysRouteDao.pagination(example, page, pageSize);
	}

	@Override
	@Transactional
	public int deleteWithLock(int routeId, long updatedTime) {
		int result = sysRouteDao.deleteByPkAndVersion(routeId, updatedTime);
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("routeId", routeId);
		sysRoleRouteDao.delete(example);
		sysMenuRouteDao.delete(example);
		return result;
	}

    public void setSysRouteDao(CrudRepository<Integer, SysRoute> sysRouteDao) {
        this.sysRouteDao = sysRouteDao;
    }

    public void setSysRoleRouteDao(CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao) {
        this.sysRoleRouteDao = sysRoleRouteDao;
    }

    public void setSysMenuRouteDao(CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao) {
        this.sysMenuRouteDao = sysMenuRouteDao;
    }
}
