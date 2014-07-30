package com.edgar.module.sys.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edgar.core.mvc.ToQueryExample;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.shiro.AuthHelper;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.service.MenuRouteCommand;
import com.edgar.module.sys.service.SysMenuService;

/**
 * 菜单的rest
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/menu")
public class SysMenuResource {

        @Autowired
        private SysMenuService sysMenuService;

        /**
         * 保存菜单
         * 
         * @param sysMenu
         *                菜单
         * @return 保存成功返回1，失败返回0
         */
        @AuthHelper(value = "Create Menu", isRoot = true)
        @RequestMapping(method = RequestMethod.POST)
        @ResponseBody
        public int save(@RequestBody SysMenu sysMenu) {
                return sysMenuService.save(sysMenu);
        }

        /**
         * 更新菜单
         * 
         * @param menuId
         *                菜单ID
         * @param sysMenu
         *                菜单
         * @return 保存成功，返回1，保存失败，返回0
         */
        @AuthHelper(value = "Update Menu", isRoot = true)
        @RequestMapping(method = RequestMethod.PUT, value = "/{menuId}")
        @ResponseBody
        public int update(@PathVariable("menuId") int menuId, @RequestBody SysMenu sysMenu) {
                sysMenu.setMenuId(menuId);
                return sysMenuService.update(sysMenu);
        }

        /**
         * 根据菜单ID查询菜单
         * 
         * @param menuId
         *                菜单ID
         * @return 菜单
         */
        @AuthHelper(value = "View Menu", isRoot = true)
        @RequestMapping(method = RequestMethod.GET, value = "/{menuId}")
        @ResponseBody
        public SysMenu get(@PathVariable("menuId") int menuId) {
                return sysMenuService.get(menuId);
        }

        /**
         * 查询菜单
         * 
         * @param example
         *                查询条件
         * @return 菜单的分页类
         */
        @AuthHelper(value = "Query Menu")
        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
        public List<SysMenu> query(@ToQueryExample(maxNumOfRecords = 1000) QueryExample example) {
                return sysMenuService.query(example);
        }

        /**
         * 根据菜单ID和时间戳删除菜单
         * 
         * @param menuId
         *                菜单ID
         * @param updatedTime
         *                时间戳
         * @return 如果删除成功，返回1
         */
        @AuthHelper(value = "Delete Menu", isRoot = true)
        @RequestMapping(method = RequestMethod.DELETE, value = "/{menuId}")
        @ResponseBody
        public int delete(@PathVariable("menuId") int menuId,
                        @RequestParam("updatedTime") long updatedTime) {
                return sysMenuService.deleteWithLock(menuId, updatedTime);
        }

        /**
         * 查询与菜单关联的路由
         * 
         * @return 资源的集合
         */
        @AuthHelper(value = "Query relation of menu and route", isRoot = true)
        @RequestMapping(method = RequestMethod.GET, value = "/route/{menuId}")
        @ResponseBody
        public List<SysMenuRoute> getRoute(@PathVariable("menuId") int menuId) {
                return sysMenuService.getRoute(menuId);
        }

        /**
         * 保存菜单与路由的关联
         * 
         * @param command
         *                资源授权的对象
         * @return 如果授权成功，返回1
         */
        @AuthHelper(value = "Ralate menu and route", isRoot = true)
        @RequestMapping(value = "/route", method = RequestMethod.POST)
        @ResponseBody
        public int saveResourcePermission(@RequestBody MenuRouteCommand command) {
                sysMenuService.saveMenuRoute(command);
                return 1;
        }
}
