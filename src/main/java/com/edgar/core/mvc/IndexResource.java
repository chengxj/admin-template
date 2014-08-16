package com.edgar.core.mvc;

import java.util.*;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.shiro.*;
import com.edgar.core.util.Constants;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.service.SysRouteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysRoute;
import com.edgar.module.sys.repository.domain.SysUserProfile;
import com.edgar.module.sys.service.PermissionService;
import com.edgar.module.sys.service.SysMenuService;
import com.edgar.module.sys.service.SysUserService;
import com.edgar.module.sys.view.AngularRoute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 路由的rest接口
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Controller
@RequestMapping("/index")
public class IndexResource {

	@Autowired
	private SysMenuService sysMenuService;

    @Autowired
    private SysRouteService sysRouteService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 根据用户权限返回路由
	 * 
	 * @return 路由的集合
	 */
	private List<AngularRoute> getRoutes(Set<Integer> roleIds) {
        List<AngularRoute> angularRoutes = new ArrayList<AngularRoute>();
		List<SysRoute> routes = new ArrayList<SysRoute>();

		for (Integer roleId : roleIds) {
			routes.addAll(permissionService.getRoute(roleId));
		}

		for (SysRoute sysRoute : routes) {
			AngularRoute angularRoute = new AngularRoute();
			angularRoute.setUrl(sysRoute.getUrl());
			angularRoute.setName(sysRoute.getName());
			angularRoutes.add(angularRoute);
		}
		return angularRoutes;
	}

	/**
	 * 根据用户权限返回菜单
	 * 
	 * @return 菜单的集合
	 */
	@AuthHelper(value = "Query User Profile", isRoot = true, type = AuthType.AUTHC)
	@RequestMapping(method = RequestMethod.GET, value = "/user")
	@ResponseBody
	public Map<String, Object> getUserData() {
        StatelessUser user = (StatelessUser) RequestContextHolder.currentRequestAttributes().getAttribute(Constants.USER_KEY, RequestAttributes.SCOPE_REQUEST);
     Set<Integer> roleIds = new LinkedHashSet<Integer>();
        List<SysRole> roles = user.getRoles();
        for (SysRole sysRole : roles) {
            roleIds.add(sysRole.getRoleId());
        }
		if (roleIds.isEmpty()) {
			throw ExceptionFactory.isNull();
		}
		Map<String, Object> data = new HashMap<String, Object>();
//		LoginUser loginUser = LoginUserUtils.getLoginUser();
//		SysUserProfile profile = sysUserService.getProfile(loginUser
//				.getUserId());
//		loginUser.setProfile(profile);

		List<SysMenu> menus = new ArrayList<SysMenu>();
		for (Integer roleId : roleIds) {

			Set<Integer> menuIds = new HashSet<Integer>(
					permissionService.getMenu(roleId));
			for (Integer menuId : menuIds) {
				SysMenu menu = sysMenuService.get(menuId);
				menus.add(menu);
				if (StringUtils.isNotBlank(menu.getPermission())) {
                    user.addPermission(menu.getPermission());
				}
			}
		}
		data.put("menus", menus);
		data.put("user", user);
		data.put("routes", getRoutes(roleIds));
		return data;
	}

}
