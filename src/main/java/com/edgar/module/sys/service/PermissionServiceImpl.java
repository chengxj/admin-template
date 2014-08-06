package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Setter;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRes;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.domain.SysResource;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.repository.domain.SysRoleRes;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.domain.SysRoute;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao;

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
	private CrudRepository<Integer, SysMenuRes> sysMenuResDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRoleRes> sysRoleResDao;

	// @Override
	// public List<SysRoleMenu> getMenus(int roleId) {
	// QueryExample example = QueryExample.newInstance();
	// example.equalsTo("roleId", roleId);
	// example.addField("resourceId");
	// Set<Integer> roleResourceIds = new HashSet<Integer>(
	// sysRoleResDao.querySingleColumn(example, Integer.class));
	//
	// example.clear();
	// example.equalsTo("isRoot", 0);
	// example.asc("sorted");
	// // example.notEqualsTo("parentId", -1);
	// List<SysMenu> sysMenus = sysMenuDao.query(example);
	// List<SysMenuVo> sysMenuVos = new ArrayList<SysMenuVo>();
	//
	// for (SysMenu sysMenu : sysMenus) {
	// SysMenuVo sysMenuVo = new SysMenuVo();
	// BeanUtils.copyProperties(sysMenu, sysMenuVo);
	//
	// example.clear();
	// example.equalsTo("menuId", sysMenu.getMenuId());
	// example.addField("routeId");
	// List<Integer> routeIds = sysMenuRouteDao.querySingleColumn(example,
	// Integer.class);
	// Set<Integer> resourceIds = new HashSet<Integer>();
	// for (Integer routeId : routeIds) {
	// example.clear();
	// example.equalsTo("routeId", routeId);
	// example.addField("resourceId");
	//
	// // resourceIds.addAll(sysRouteResDao.querySingleColumn(example,
	// // Integer.class));
	//
	// }
	// for (Integer resourceId : resourceIds) {
	// SysResource resource = sysResourceDao.get(resourceId);
	// SysResourceVo resourceVo = new SysResourceVo();
	// BeanUtils.copyProperties(resource, resourceVo);
	// resourceVo.setChecked(roleResourceIds.contains(resourceId));
	// sysMenuVo.addResource(resourceVo);
	// }
	// sysMenuVos.add(sysMenuVo);
	// }
	// return sysMenuVos;
	// }

	@Override
	public List<Integer> getMenu(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		example.addField("menuId");
		return sysRoleMenuDao.querySingleColumn(example,
				Integer.class);
	}

	@Override
	public List<SysRoute> getRoute(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		example.addField("menuId");
		List<Integer> menuIds = sysRoleMenuDao.querySingleColumn(example,
				Integer.class);
		Set<Integer> routeIds = new HashSet<Integer>();
		for (Integer menuId : menuIds) {
			example.clear();
			example.equalsTo("menuId", menuId);
			example.addField("routeId");
			routeIds.addAll(sysMenuRouteDao.querySingleColumn(example,
					Integer.class));
		}
		example.clear();
		example.equalsTo("roleId", roleId);
		example.addField("routeId");
		routeIds.addAll(sysRoleRouteDao.querySingleColumn(example,
				Integer.class));
		List<SysRoute> sysRoutes = new ArrayList<SysRoute>();
		for (Integer routeId : routeIds) {
			sysRoutes.add(sysRouteDao.get(routeId));
		}
		return sysRoutes;
	}

	@Override
	public List<SysResource> getResource(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		example.addField("menuId");
		List<Integer> menuIds = sysRoleMenuDao.querySingleColumn(example,
				Integer.class);
		Set<Integer> resourceIds = new HashSet<Integer>();
		for (Integer menuId : menuIds) {
			example.clear();
			example.equalsTo("menuId", menuId);
			example.addField("resourceId");
			resourceIds.addAll(sysMenuResDao.querySingleColumn(example,
					Integer.class));
		}
		example.clear();
		example.equalsTo("roleId", roleId);
		example.addField("resourceId");
		resourceIds.addAll(sysRoleResDao.querySingleColumn(example,
				Integer.class));
		List<SysResource> sysResources = new ArrayList<SysResource>();
		for (Integer resourceId : resourceIds) {
			sysResources.add(sysResourceDao.get(resourceId));
		}
		return sysResources;
	}

	@Override
	@Transactional
	public void savePermission(PermissionCommand command) {
		int roleId = command.getRoleId();
		deleteByRole(roleId);
		Set<Integer> permIds = command.getPermissionIds();
		if (CollectionUtils.isEmpty(permIds)) {
			return;
		}
		Set<Integer> unModifiablePermIds = Collections.unmodifiableSet(permIds);
		saveRoleMenu(roleId, unModifiablePermIds);
		checkRole(command.getRoleId());
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
	 * 根据角色删除资源权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 返回删除的记录数
	 */
	private int deleteByRole(int roleId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleId", roleId);
		return sysRoleMenuDao.delete(example);
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
