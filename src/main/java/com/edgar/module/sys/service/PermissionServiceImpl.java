package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Setter;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.domain.SysResource;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.repository.domain.SysRoleResource;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.domain.SysRoute;
import com.edgar.module.sys.repository.domain.SysRouteRes;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	@Setter
	private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRoleResource> sysRoleResourceDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRole> sysRoleDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysResource> sysResourceDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRoute> sysRouteDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysMenu> sysMenuDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRouteRes> sysRouteResDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao;

	@Override
	public List<SysMenuVo> getMenus(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		example.addField("resourceId");
		Set<Integer> roleResourceIds = new HashSet<Integer>(
				sysRoleResourceDao.querySingleColumn(example, Integer.class));

		example.clear();
		example.equalsTo("isRoot", 0);
		example.asc("sorted");
		example.notEqualsTo("parentId", -1);
		List<SysMenu> sysMenus = sysMenuDao.query(example);
		List<SysMenuVo> sysMenuVos = new ArrayList<SysMenuVo>();

		for (SysMenu sysMenu : sysMenus) {
			SysMenuVo sysMenuVo = new SysMenuVo();
			BeanUtils.copyProperties(sysMenu, sysMenuVo);

			example.clear();
			example.equalsTo("menuId", sysMenu.getMenuId());
			example.addField("routeId");
			List<Integer> routeIds = sysMenuRouteDao.querySingleColumn(example,
					Integer.class);
			for (Integer routeId : routeIds) {
				SysRouteVo routeVo = new SysRouteVo();
				SysRoute sysRoute = sysRouteDao.get(routeId);
				BeanUtils.copyProperties(sysRoute, routeVo);
				example.clear();
				example.equalsTo("routeId", sysRoute.getRouteId());
				example.addField("resourceId");

				List<Integer> resourceIds = sysRouteResDao.querySingleColumn(
						example, Integer.class);

				for (Integer resourceId : resourceIds) {
					SysResource resource = sysResourceDao.get(resourceId);
					SysResourceVo resourceVo = new SysResourceVo();
					BeanUtils.copyProperties(resource, resourceVo);
					resourceVo.setChecked(roleResourceIds.contains(resourceId));
					routeVo.addResource(resourceVo);
				}

				sysMenuVo.addRoute(routeVo);
			}
			sysMenuVos.add(sysMenuVo);
		}
		return sysMenuVos;
	}

	@Override
	public List<SysRoleMenu> getMenu(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		List<SysRoleMenu> sysRoleMenus = sysRoleMenuDao.query(example);
		return sysRoleMenus;
	}

	@Override
	public List<SysRoleRoute> getRoute(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		List<SysRoleRoute> sysRoleRoutes = sysRoleRouteDao.query(example);
		return sysRoleRoutes;
	}

	@Override
	public List<SysRoleResource> getResource(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		List<SysRoleResource> sysRoleResources = sysRoleResourceDao
				.query(example);
		return sysRoleResources;
	}

	@Override
	@Transactional
	public void saveResourcePermission(ResourcePermission command) {
		int roleId = command.getRoleId();
		deleteByRole(roleId);
		Set<Integer> resourceIds = command.getResourceIds();
		if (CollectionUtils.isEmpty(resourceIds)) {
			return;
		}
		Set<Integer> unModifiableResources = Collections
				.unmodifiableSet(resourceIds);
		saveRoleRes(roleId, unModifiableResources);
		checkRole(command.getRoleId());
	}

	/**
	 * 保存资源权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @param resourceIds
	 *            资源的ID集合
	 */
	private void saveRoleRes(int roleId, Set<Integer> resourceIds) {
		List<SysRoleResource> sysRoleResources = new ArrayList<SysRoleResource>();
		for (Integer resourceId : resourceIds) {
			SysRoleResource sysRoleRes = new SysRoleResource();
			sysRoleRes.setRoleId(roleId);
			sysRoleRes.setResourceId(resourceId);
			sysRoleRes.setRoleResourceId(IDUtils.getNextId());
			sysRoleResources.add(sysRoleRes);
			SysResource sysRoute = sysResourceDao.get(resourceId);
			if (sysRoute == null) {
				throw ExceptionFactory.isNull("msg.error.resource.notexists");
			}
		}
		sysRoleResourceDao.insert(sysRoleResources);
		Set<Integer> routeIds = new HashSet<Integer>();
		for (Integer resourceId : resourceIds) {
			QueryExample example = QueryExample.newInstance();
			example.equalsTo("resourceId", resourceId);
			example.addField("routeId");
			routeIds.addAll(sysRouteResDao.querySingleColumn(example,
					Integer.class));
		}
		saveRoleRoute(roleId, routeIds);

		Set<Integer> menuIds = new HashSet<Integer>();
		for (Integer routeId : routeIds) {
			QueryExample example = QueryExample.newInstance();
			example.equalsTo("routeId", routeId);
			example.addField("menuId");
			menuIds.addAll(sysMenuRouteDao.querySingleColumn(example,
					Integer.class));
		}

		saveRoleMenu(roleId, menuIds);
	}

	/**
	 * 保存角色菜单关联
	 * 
	 * @param roleId
	 *            角色ID
	 * @param menuIds
	 *            菜单的ID集合
	 */
	private Set<Integer> saveRoleMenu(int roleId, Set<Integer> menuIds) {
		List<SysRoleMenu> sysRoleMenus = new ArrayList<SysRoleMenu>();
		Set<Integer> parentIds = new HashSet<Integer>();
		for (Integer menuId : menuIds) {
			sysRoleMenus.add(newSysRoleMenu(roleId, menuId));
			SysMenu sysMenu = checkSysMenu(menuId);
			if (sysMenu.getParentId() != null && sysMenu.getParentId() > 0) {
				parentIds.add(sysMenu.getParentId());
			}
		}

		for (Integer menuId : parentIds) {
			sysRoleMenus.add(newSysRoleMenu(roleId, menuId));
			checkSysMenu(menuId);
		}
		sysRoleMenuDao.insert(sysRoleMenus);
		parentIds.addAll(menuIds);
		return parentIds;
	}

	/**
	 * 检查菜单是否存在，如果菜单不存在，则抛出SystemException异常
	 * 
	 * @param menuId
	 *            菜单ID
	 * @return 菜单
	 */
	private SysMenu checkSysMenu(int menuId) {
		SysMenu sysMenu = sysMenuDao.get(menuId);
		if (sysMenu == null) {
			throw ExceptionFactory.isNull("msg.error.menu.notexists");
		}
		return sysMenu;
	}

	/**
	 * 创建角色菜单关联
	 * 
	 * @param roleId
	 *            角色ID
	 * @param menuId
	 *            菜单ID
	 * @return 角色菜单关联
	 */
	private SysRoleMenu newSysRoleMenu(int roleId, int menuId) {
		SysRoleMenu sysRoleRoute = new SysRoleMenu();
		sysRoleRoute.setRoleMenuId(IDUtils.getNextId());
		sysRoleRoute.setRoleId(roleId);
		sysRoleRoute.setMenuId(menuId);
		return sysRoleRoute;
	}

	/**
	 * 保存角色路由权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @param routeIds
	 *            路由的ID集合
	 */
	private void saveRoleRoute(int roleId, Set<Integer> routeIds) {
		List<SysRoleRoute> sysRoleRoutes = new ArrayList<SysRoleRoute>();
		for (Integer routeId : routeIds) {
			SysRoleRoute sysRoleRoute = new SysRoleRoute();
			sysRoleRoute.setRoleId(roleId);
			sysRoleRoute.setRouteId(routeId);
			sysRoleRoute.setRoleRouteId(IDUtils.getNextId());
			sysRoleRoutes.add(sysRoleRoute);
			SysRoute sysRoute = sysRouteDao.get(routeId);
			if (sysRoute == null) {
				throw ExceptionFactory.isNull("路由不存在");
			}
		}
		sysRoleRouteDao.insert(sysRoleRoutes);
	}

	/**
	 * 根据角色删除资源权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 返回删除的记录数
	 */
	private int deleteByRole(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		sysRoleRouteDao.delete(example);
		sysRoleMenuDao.delete(example);
		return sysRoleResourceDao.delete(example);
	}

	/**
	 * 检查角色是否存在，如果不存在，则抛出空的异常
	 * 
	 * @param roleId
	 *            角色ID
	 */
	private void checkRole(int roleId) {
		SysRole sysRole = sysRoleDao.get(roleId);
		if (sysRole == null) {
			throw ExceptionFactory.isNull("msg.error.role.notexists");
		}
	}

}
