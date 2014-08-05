package com.edgar.core.shiro;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysRoleRes;
import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.repository.domain.SysUserRole;
import com.edgar.module.sys.service.PermissionService;
import com.edgar.module.sys.service.SysResourceService;
import com.edgar.module.sys.service.SysRoleService;
import com.edgar.module.sys.service.SysUserService;

/**
 * 自定义的Shiro Realm
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class CustomSecurityRealm extends JdbcRealm {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomSecurityRealm.class);

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysResourceService sysResourceService;

	@Autowired
	private PermissionService permissionService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();

		if (username == null) {
			throw new AccountException("username cannot be null");
		}

		AuthenticationInfo info = null;
		LoginUser loginUser = getUser(username);

		if (loginUser == null || loginUser.getPassword() == null) {
			throw new UnknownAccountException("username  [" + username
					+ "] not exists");
		}
		info = new SimpleAuthenticationInfo(loginUser, loginUser.getPassword(),
				ByteSource.Util.bytes(username + loginUser.getSalt()),
				getName());

		return info;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException(
					"PrincipalCollection method argument cannot be null.");
		}

		LoginUser loginUser = (LoginUser) getAvailablePrincipal(principals);

		Set<String> roleNames = new LinkedHashSet<String>();
		Set<String> permissions = new LinkedHashSet<String>();
		List<SysRole> roles = loginUser.getRoles();
		loginUser.setRoles(roles);
		for (SysRole sysRole : roles) {
			roleNames.add(sysRole.getRoleName());
		}
		permissions = getPermissions(loginUser.getUserId(), roles);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;

	}

	/**
	 * 根据用户密码，获取用户
	 * 
	 * @param username
	 *            用户名
	 * @return 登录用户
	 */
	protected LoginUser getUser(String username) {
		logger.debug("user login : {}", username);
		List<SysUser> sysUsers = sysUserService.queryByUsername(username);
		if (sysUsers.isEmpty()) {
			throw new UnknownAccountException("username cannot be null");
		}
		if (sysUsers.size() > 1) {
			throw new AuthenticationException(" usernmae [" + username
					+ "] must be unique");
		}
		SysUser sysUser = sysUsers.get(0);
		LoginUser loginUser = new LoginUser();
		BeanUtils.copyProperties(sysUser, loginUser);
		List<SysRole> roles = getRolesForUser(loginUser.getUserId());
		loginUser.setRoles(roles);
		return loginUser;

	}

	/**
	 * 根据用户ID获取用户角色
	 * 
	 * @param userId
	 *            用户ID
	 * @return 角色的集合
	 */
	protected List<SysRole> getRolesForUser(int userId) {

		List<SysUserRole> sysUserRoles = sysUserService.getRoles(userId);
		List<SysRole> roles = new ArrayList<SysRole>();
		for (SysUserRole sysUserRole : sysUserRoles) {
			roles.add(sysRoleService.get(sysUserRole.getRoleId()));
		}
		return roles;
	}

	/**
	 * 获取用户的授权
	 * 
	 * @param userId
	 *            用户ID
	 * @param roles
	 *            角色列表
	 * @return 授权的集合
	 */
	protected Set<String> getPermissions(int userId, List<SysRole> roles) {
		Set<String> permissions = new LinkedHashSet<String>();
		for (SysRole role : roles) {
			List<SysRoleRes> sysRoleRes = permissionService
					.getResource(role.getRoleId());
			for (SysRoleRes sysRoleResource : sysRoleRes) {
				permissions.add(sysResourceService.get(
						sysRoleResource.getResourceId()).getPermission());
			}
		}

		return permissions;
	}

}