package com.edgar.module.sys.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edgar.core.shiro.AuthHelper;
import com.edgar.module.sys.service.PermissionService;
import com.edgar.module.sys.service.ResourcePermission;
import com.edgar.module.sys.service.SysMenuVo;

/**
 * 角色授权的rest
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/permission")
public class SysPermissionResource {

	@Autowired
	private PermissionService permissionService;


	/**
	 * 保存资源授权
	 * 
	 * @param command
	 *            资源授权的对象
	 * @return 如果授权成功，返回1
	 */
	@AuthHelper("Save Resource Permisison")
	@RequestMapping(value = "/resource", method = RequestMethod.POST)
	@ResponseBody
	public int saveResourcePermission(@RequestBody ResourcePermission command) {
		permissionService.saveResourcePermission(command);
		return 1;
	}

	/**
	 * 查询角色的资源授权
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 角色资源集合
	 */
	@AuthHelper("Query Resource Permisison")
	@RequestMapping(value = "/menu/{roleId}", method = RequestMethod.GET)
	@ResponseBody
	public List<SysMenuVo> getMenus(@PathVariable("roleId") int roleId) {
		return permissionService.getMenus(roleId);
	}
}
