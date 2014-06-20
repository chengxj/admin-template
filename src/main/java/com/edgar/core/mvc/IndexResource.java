package com.edgar.core.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edgar.core.shiro.AuthHelper;
import com.edgar.core.shiro.AuthType;
import com.edgar.core.shiro.LoginUser;
import com.edgar.core.shiro.LoginUserUtils;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.domain.SysRoute;
import com.edgar.module.sys.repository.domain.SysUserProfile;
import com.edgar.module.sys.service.PermissionService;
import com.edgar.module.sys.service.SysMenuService;
import com.edgar.module.sys.service.SysRoleService;
import com.edgar.module.sys.service.SysRouteService;
import com.edgar.module.sys.service.SysUserService;
import com.edgar.module.sys.view.AngularRoute;

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
        private SysRouteService sysRouteService;

        @Autowired
        private SysMenuService sysMenuService;

        @Autowired
        private SysRoleService sysRoleService;
        
        @Autowired
        private PermissionService permissionService;
        
        @Autowired
        private SysUserService sysUserService;

        
        /**
         * 根据用户权限返回路由
         * 
         * @return 路由的集合
         */
        @AuthHelper(value = "Query Routes", isRoot = true, type = AuthType.AUTHC)
        @RequestMapping(method = RequestMethod.GET, value = "/route")
        @ResponseBody
        public List<AngularRoute> getRoutes() {
                List<AngularRoute> angularRoutes = new ArrayList<AngularRoute>();
                Set<Integer> roleIds = LoginUserUtils.getRoleIds();

                Map<Integer, SysRoute> routeMap = new HashMap<Integer, SysRoute>();
                for (Integer roleId : roleIds) {
                        List<SysRoleRoute> sysRoleRoutes = permissionService.getRoute(roleId);
                        for (SysRoleRoute sysRoleRoute : sysRoleRoutes) {
                                if (!routeMap.containsKey(sysRoleRoute.getRouteId())) {
                                        routeMap.put(sysRoleRoute.getRouteId(), sysRouteService
                                                        .get(sysRoleRoute.getRouteId()));
                                }
                        }
                }

                List<SysRoute> routes = new ArrayList<SysRoute>(routeMap.values());
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
                Set<Integer> roleIds = LoginUserUtils.getRoleIds();
                if (roleIds.isEmpty()) {
                        throw ExceptionFactory.isNull("msg.user.unauthorized");
                }
                Map<String, Object> data = new HashMap<String, Object>();
                LoginUser loginUser = LoginUserUtils.getLoginUser();
                LoginUserUtils.getPermission(loginUser);
                SysUserProfile profile = sysUserService.getProfile(loginUser.getUserId());
                loginUser.setProfile(profile);
                data.put("user", loginUser);
                data.put("routes", getRoutes());

                Map<Integer, SysMenu> menuMap = new HashMap<Integer, SysMenu>();
                for (Integer roleId : roleIds) {
                        List<SysRoleMenu> sysRoleMenus = permissionService.getMenu(roleId);
                        for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
                                // TODO 此处有性能还可以优惠
                                if (!menuMap.containsKey(sysRoleMenu.getMenuId())) {
                                        menuMap.put(sysRoleMenu.getMenuId(),
                                                        sysMenuService.get(sysRoleMenu.getMenuId()));
                                }
                        }
                }
                List<SysMenu> menus = new ArrayList<SysMenu>(menuMap.values());
                data.put("menus", menus);
                return data;
        }

}
