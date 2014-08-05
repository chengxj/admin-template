package com.edgar.core.shiro;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ServiceLookup;
import com.edgar.module.sys.repository.domain.SysResource;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysRoleRes;

/**
 * 登录用户的工具类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class LoginUserUtils {

	/**
	 * 获取登录用户
	 * 
	 * @return 登录用户
	 */
	public static final LoginUser getLoginUser() {
		Subject subject = SecurityUtils.getSubject();
		LoginUser loginUser = (LoginUser) subject.getPrincipal();
		return loginUser;
	}

	/**
	 * 获取登录用户的角色ID集合
	 * 
	 * @return 角色ID的集合
	 */
	public static final Set<Integer> getRoleIds() {
		LoginUser loginUser = getLoginUser();
		Set<Integer> roleIds = new LinkedHashSet<Integer>();
		List<SysRole> roles = loginUser.getRoles();
		for (SysRole sysRole : roles) {
			roleIds.add(sysRole.getRoleId());
		}
		return roleIds;
	}

	/**
	 * 获取登录用户用户的授权
	 * 
	 * @param loginUser
	 *            登录用户
	 */
	@SuppressWarnings("unchecked")
	public static final void getPermission(LoginUser loginUser) {
		List<SysRole> roles = loginUser.getRoles();
		Set<String> permissions = new LinkedHashSet<String>();
		CrudRepository<Integer, SysRoleRes> sysRoleResDao = ServiceLookup
				.getBean("sysRoleResDao", CrudRepository.class);
		CrudRepository<Integer, SysResource> sysResourceDao = ServiceLookup
				.getBean("sysResourceDao", CrudRepository.class);
		for (SysRole role : roles) {
			QueryExample example = QueryExample.newInstance();
			example.equalsTo("roleId", role.getRoleId());
			List<SysRoleRes> SysRoleReses = sysRoleResDao.query(example);
			for (SysRoleRes sysRoleRes : SysRoleReses) {
				permissions.add(sysResourceDao.get(sysRoleRes.getResourceId())
						.getPermission());
			}
		}
		loginUser.setPermissions(permissions);
	}
}
