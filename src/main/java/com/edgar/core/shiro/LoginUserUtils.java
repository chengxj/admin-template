package com.edgar.core.shiro;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.edgar.module.sys.repository.domain.SysRole;

/**
 * 登录用户的工具类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class LoginUserUtils {

//	/**
//	 * 获取登录用户
//	 *
//	 * @return 登录用户
//	 */
//	public static LoginUser getLoginUser() {
//		Subject subject = SecurityUtils.getSubject();
//		LoginUser loginUser = (LoginUser) subject.getPrincipal();
//		return loginUser;
//	}
//
//	/**
//	 * 获取登录用户的角色ID集合
//	 *
//	 * @return 角色ID的集合
//	 */
//	public static Set<Integer> getRoleIds() {
//		LoginUser loginUser = getLoginUser();
//		Set<Integer> roleIds = new LinkedHashSet<Integer>();
//		List<SysRole> roles = loginUser.getRoles();
//		for (SysRole sysRole : roles) {
//			roleIds.add(sysRole.getRoleId());
//		}
//		return roleIds;
//	}

}
