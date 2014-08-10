package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRes;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.validator.SysMenuUpdateValidator;
import com.edgar.module.sys.validator.SysMenuValidator;

/**
 * 系统菜单的业务逻辑实现
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
	@Autowired
	private CrudRepository<Integer, SysMenu> sysMenuDao;

	@Autowired
	private CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao;

	@Autowired
	private CrudRepository<Integer, SysMenuRes> sysMenuResDao;

	@Autowired
	private CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao;

	private final ValidatorStrategy validator = new SysMenuValidator();

	private final ValidatorStrategy updateValidator = new SysMenuUpdateValidator();

	@Override
	public SysMenu get(int menuId) {
		Assert.notNull(menuId);
		return sysMenuDao.get(menuId);
	}

	@Override
	@Transactional
	public int save(SysMenuVo sysMenu) {
		Assert.notNull(sysMenu);
		sysMenu.setIsRoot(false);
		if (sysMenu.getParentId() == null || sysMenu.getParentId() == 0) {
			sysMenu.setParentId(-1);
		}
		validator.validator(sysMenu);
		if (StringUtils.isNotBlank(sysMenu.getMenuType())) {
			sysMenu.setMenuType("button");
		}
		sysMenu.setMenuId(IDUtils.getNextId());
		int result = sysMenuDao.insert(sysMenu);

		insertMenuRoute(sysMenu);
		insertMenuRes(sysMenu);
		checkMenu(sysMenu);
		return result;
	}

	private void checkMenu(SysMenuVo sysMenu) {
		if (sysMenu.getParentId() != -1) {
			SysMenu parent = get(sysMenu.getParentId());
			if (parent == null) {
				throw ExceptionFactory.isNull();
			}
		}
	}

	private void insertMenuRes(SysMenuVo sysMenu) {
		if (CollectionUtils.isNotEmpty(sysMenu.getResourceIds())) {
			List<SysMenuRes> sysMenuReses = new ArrayList<SysMenuRes>();
			for (Integer resourceId : sysMenu.getResourceIds()) {
				SysMenuRes sysMenuRoute = new SysMenuRes();
				sysMenuRoute.setMenuResId(IDUtils.getNextId());
				sysMenuRoute.setMenuId(sysMenu.getMenuId());
				sysMenuRoute.setResourceId(resourceId);
				sysMenuReses.add(sysMenuRoute);
			}
			sysMenuResDao.insert(sysMenuReses);
		}
	}

	private void insertMenuRoute(SysMenuVo sysMenu) {
		if (CollectionUtils.isNotEmpty(sysMenu.getRouteIds())) {
			List<SysMenuRoute> sysMenuRoutes = new ArrayList<SysMenuRoute>();
			for (Integer routeId : sysMenu.getRouteIds()) {
				SysMenuRoute sysMenuRoute = new SysMenuRoute();
				sysMenuRoute.setMenuRouteId(IDUtils.getNextId());
				sysMenuRoute.setMenuId(sysMenu.getMenuId());
				sysMenuRoute.setRouteId(routeId);
				sysMenuRoutes.add(sysMenuRoute);
			}
			sysMenuRouteDao.insert(sysMenuRoutes);
		}
	}

	@Override
	@Transactional
	public int update(SysMenuVo sysMenu) {
		updateValidator.validator(sysMenu);
		int result = sysMenuDao.update(sysMenu);

		QueryExample example = QueryExample.newInstance();
		example.equalsTo("menuId", sysMenu.getMenuId());
		sysMenuRouteDao.delete(example);
		sysMenuResDao.delete(example);
		insertMenuRoute(sysMenu);
		insertMenuRes(sysMenu);
		return result;
	}

	private void deleteMenuRelation(int menuId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("menuId", menuId);
		sysRoleMenuDao.delete(example);
		sysMenuRouteDao.delete(example);
		sysMenuResDao.delete(example);
	}

	@Override
	@Transactional
	public int deleteWithLock(int menuId, long updatedTime) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("parentId", menuId);
		List<SysMenu> children = sysMenuDao.query(example);
		for (SysMenu sysMenu : children) {
			deleteMenuRelation(sysMenu.getMenuId());
		}
		int result = sysMenuDao.deleteByPkAndVersion(menuId, updatedTime);
		deleteMenuRelation(menuId);
		return result;
	}

	@Override
	public List<SysMenuVo> query(QueryExample example) {
		example.asc("sorted");
		example.equalsTo("isRoot", 0);
		example.equalsTo("parentId", -1);
		// example.equalsTo("menuType", "")
		List<SysMenu> sysMenus = sysMenuDao.query(example);
		List<SysMenuVo> sysMenuVos = new ArrayList<SysMenuVo>();
		for (SysMenu sysMenu : sysMenus) {
			sysMenuVos.add(getMenu(sysMenu));
		}
		return sysMenuVos;
	}

	private SysMenuVo getMenu(SysMenu sysMenu) {
		SysMenuVo sysMenuVo = new SysMenuVo();
		BeanUtils.copyProperties(sysMenu, sysMenuVo);
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("parentId", sysMenu.getMenuId());
		example.equalsTo("isRoot", 0);
		List<SysMenu> children = sysMenuDao.query(example);
		for (SysMenu childMenu : children) {
			sysMenuVo.addChild(getMenu(childMenu));
		}
		return sysMenuVo;
	}

	@Override
	public List<SysMenuRoute> getRoute(int menuId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("menuId", menuId);
		return sysMenuRouteDao.query(example);
	}

	@Override
	public List<SysMenuRes> getResource(int menuId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("menuId", menuId);
		return sysMenuResDao.query(example);
	}

	@Override
	public boolean checkPermisson(String permisson) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("permission", permisson);
		List<SysMenu> sysUsers = sysMenuDao.query(example);
        return sysUsers.isEmpty();
    }

    public void setSysMenuDao(CrudRepository<Integer, SysMenu> sysMenuDao) {
        this.sysMenuDao = sysMenuDao;
    }

    public void setSysMenuRouteDao(CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao) {
        this.sysMenuRouteDao = sysMenuRouteDao;
    }

    public void setSysMenuResDao(CrudRepository<Integer, SysMenuRes> sysMenuResDao) {
        this.sysMenuResDao = sysMenuResDao;
    }

    public void setSysRoleMenuDao(CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao) {
        this.sysRoleMenuDao = sysRoleMenuDao;
    }
}
