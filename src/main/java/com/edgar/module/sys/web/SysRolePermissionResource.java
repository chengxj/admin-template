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
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.service.MenuPermission;
import com.edgar.module.sys.service.PermissionService;
import com.edgar.module.sys.service.ResourcePermission;
import com.edgar.module.sys.service.RoutePermission;

/**
 * 角色授权的rest
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/permission")
public class SysRolePermissionResource {

        @Autowired
        private PermissionService permissionService;

        /**
         * 保存路由授权
         * 
         * @param command
         *                路由授权的对象
         * @return 如果授权成功，返回1
         */
        @AuthHelper(value="Save Route Permisison", isRoot=true)
        @RequestMapping(value = "/route", method = RequestMethod.POST)
        @ResponseBody
        public int saveRoutePermission(@RequestBody RoutePermission command) {
                permissionService.saveRoutePermission(command);
                return 1;
        }

        /**
         * 保存资源授权
         * 
         * @param command
         *                资源授权的对象
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
         * 保存菜单授权
         * 
         * @param command
         *                菜单授权的对象
         * @return 如果授权成功，返回1
         */
        @AuthHelper("Save Menu Permisison")
        @RequestMapping(value = "/menu", method = RequestMethod.POST)
        @ResponseBody
        public int saveMenuPermission(@RequestBody MenuPermission command) {
                permissionService.saveMenuPermission(command);
                return 1;
        }

        /**
         * 查询角色的菜单授权
         * 
         * @param roleId
         *                角色ID
         * @return 角色菜单集合
         */
        @AuthHelper("Query Menu Permisison")
        @RequestMapping(value = "/menu/{roleId}", method = RequestMethod.GET)
        @ResponseBody
        public List<SysRoleMenu> getMenuPermission(@PathVariable("roleId") int roleId) {
                return permissionService.getMenu(roleId);
        }

        /**
         * 查询角色的路由授权
         * 
         * @param roleId
         *                角色ID
         * @return 角色路由集合
         */
        @AuthHelper(value="Query Route Permisison", isRoot=true)
        @RequestMapping(value = "/route/{roleId}", method = RequestMethod.GET)
        @ResponseBody
        public List<SysRoleRoute> getRoutePermission(@PathVariable("roleId") int roleId) {
                return permissionService.getRoute(roleId);
        }

        /**
         * 查询角色的资源授权
         * 
         * @param roleId
         *                角色ID
         * @return 角色资源集合
         */
        @AuthHelper("Query Resource Permisison")
        @RequestMapping(value = "/resource/{roleId}", method = RequestMethod.GET)
        @ResponseBody
        public List<com.edgar.module.sys.repository.domain.SysRoleResource> getResourcePermission(
                        @PathVariable("roleId") int roleId) {
                return permissionService.getResource(roleId);
        }
}
