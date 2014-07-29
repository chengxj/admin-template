package com.edgar.module.sys.service;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.repository.domain.SysRoleResource;
import com.edgar.module.sys.repository.domain.SysRoleRoute;

@Validated
public interface PermissionService {

	/**
	 * 根据角色查询角色授权的菜单
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 角色菜单集合
	 */
	List<SysRoleMenu> getMenu(@Min(1) int roleId);

	/**
	 * 根据角色查询角色授权的路由
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 角色路由集合
	 */
	List<SysRoleRoute> getRoute(@Min(1) int roleId);

	/**
	 * 根据角色查询角色授权的资源
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 角色资源集合
	 */
	List<SysRoleResource> getResource(int roleId);


	/**
	 * 保存资源权限
	 * 
	 * @param command
	 *            ResourcePermission
	 */
	void saveResourcePermission(@NotNull ResourcePermission command);

	/**
	 * 查询角色权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 权限的VO表
	 */
	List<SysMenuVo> getMenus(int roleId);

}
