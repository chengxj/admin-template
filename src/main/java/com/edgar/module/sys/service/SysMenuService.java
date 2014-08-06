package com.edgar.module.sys.service;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.edgar.core.exception.SystemException;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRes;
import com.edgar.module.sys.repository.domain.SysMenuRoute;

/**
 * 菜单的业务逻辑接口
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Validated
public interface SysMenuService {

	/**
	 * 根据菜单ID查询菜单
	 * 
	 * @param menuId
	 *            菜单ID
	 * @return 菜单
	 */
	SysMenu get(@Min(1) int menuId);

	/**
	 * 新增菜单
	 * 
	 * @param sysMenu
	 *            菜单
	 * @return 如果保存成功，返回<code>1</code>，保存失败，返回<code>0</code>或者抛出
	 *         {@link SystemException}
	 */
	int save(@NotNull SysMenuVo sysMenu);

	/**
	 * 修改菜单
	 * 
	 * @param sysMenu
	 *            菜单
	 * @return 如果保存成功，返回<code>1</code>，保存失败，返回<code>0</code>或者抛出
	 *         {@link SystemException}
	 */
	int update(@NotNull SysMenuVo sysMenu);

	/**
	 * 事件菜单ID和时间戳删除菜单
	 * 
	 * @param menuId
	 *            菜单ID
	 * @param updatedTime
	 *            时间戳
	 * @return 如果删除成功，返回<code>1</code>，保存失败，返回<code>0</code>或者抛出
	 *         {@link SystemException}
	 */
	int deleteWithLock(@Min(1) int menuId, @NotNull long updatedTime);

	/**
	 * 查询菜单的列表
	 * 
	 * @param example
	 *            查询条件
	 * @return 菜单的集合类
	 */
	@NotNull
	List<SysMenuVo> query(@NotNull QueryExample example);

	/**
	 * 根据菜单查询关联的路由
	 * 
	 * @param menuId
	 *            菜单ID
	 * @return 菜单路由的集合
	 */
	List<SysMenuRoute> getRoute(int menuId);

	/**
	 * 根据菜单查询关联的资源
	 * 
	 * @param menuId
	 *            菜单ID
	 * @return 菜单资源的集合
	 */
	List<SysMenuRes> getResource(int menuId);

	/**
	 * 检查权限字符串是否存在
	 * 
	 * @param permisson
	 *            权限字符串
	 * @return 如果存在，返回false
	 */
	boolean checkPermisson(String permisson);

}
