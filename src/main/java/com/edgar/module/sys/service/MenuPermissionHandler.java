package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Setter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.command.CommandHandler;
import com.edgar.core.command.CommandResult;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.domain.SysRoleMenu;

/**
 * 菜单授权命令的处理类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class MenuPermissionHandler implements CommandHandler<MenuPermission> {

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysMenu> sysMenuDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao;

        @Override
        @Transactional
        public CommandResult<Integer> execute(MenuPermission command) {
                Assert.notNull(command);
                int roleId = command.getRoleId();
                int result = deleteByRole(roleId);
                if (CollectionUtils.isEmpty(command.getMenuIds())) {
                        return CommandResult.newInstance(result);
                }
                Set<Integer> menuIds = saveRoleMenu(roleId,
                                Collections.unmodifiableSet(command.getMenuIds()));
                if (command.isCascadeRoute()) {
                        Set<Integer> routeIds = Collections.unmodifiableSet(getRouteIds(menuIds));
                        RoutePermission routePermission = new RoutePermission();
                        routePermission.setRoleId(command.getRoleId());
                        routePermission.setRouteIds(routeIds);
                        command.setNextCommand(routePermission);
                }
                return CommandResult.newInstance(result);
        }

        /**
         * 查询菜单的关联路由
         * 
         * @param menuIds
         *                菜单ID的集合
         * @return 路由ID的集合
         */
        private Set<Integer> getRouteIds(Set<Integer> menuIds) {
                Set<Integer> routeIds = new HashSet<Integer>();
                for (Integer menuId : menuIds) {
                        QueryExample example = QueryExample.newInstance();
                        example.equalsTo("menuId", menuId);
                        example.addField("routeId");
                        routeIds.addAll(sysMenuRouteDao.querySingleColumn(example, Integer.class));
                }
                return routeIds;
        }

        /**
         * 保存角色菜单关联
         * 
         * @param roleId
         *                角色ID
         * @param menuIds
         *                菜单的ID集合
         */
        private Set<Integer> saveRoleMenu(int roleId, Set<Integer> menuIds) {
                List<SysRoleMenu> sysRoleMenus = new ArrayList<SysRoleMenu>();
                Set<Integer> parentIds = new HashSet<Integer>();
                for (Integer menuId : menuIds) {
                        sysRoleMenus.add(newSysRoleMenu(roleId, menuId));
                        SysMenu sysMenu = checkSysMenu(menuId);
                        if (sysMenu.getParentId() != null && sysMenu.getParentId() > 0) {
                                parentIds.add(sysMenu.getParentId());
                        }
                }

                for (Integer menuId : parentIds) {
                        sysRoleMenus.add(newSysRoleMenu(roleId, menuId));
                        checkSysMenu(menuId);
                }
                sysRoleMenuDao.insert(sysRoleMenus);
                parentIds.addAll(menuIds);
                return parentIds;
        }

        /**
         * 创建角色菜单关联
         * 
         * @param roleId
         *                角色ID
         * @param menuId
         *                菜单ID
         * @return 角色菜单关联
         */
        private SysRoleMenu newSysRoleMenu(int roleId, int menuId) {
                SysRoleMenu sysRoleRoute = new SysRoleMenu();
                sysRoleRoute.setRoleMenuId(IDUtils.getNextId());
                sysRoleRoute.setRoleId(roleId);
                sysRoleRoute.setMenuId(menuId);
                return sysRoleRoute;
        }

        /**
         * 检查菜单是否存在，如果菜单不存在，则抛出SystemException异常
         * 
         * @param menuId
         *                菜单ID
         * @return 菜单
         */
        private SysMenu checkSysMenu(int menuId) {
                SysMenu sysMenu = sysMenuDao.get(menuId);
                if (sysMenu == null) {
                        throw ExceptionFactory.isNull("msg.error.menu.notexists");
                }
                return sysMenu;
        }

        /**
         * 根据角色删除角色菜单的关联
         * 
         * @param roleId
         *                角色ID
         * @return 返回删除的记录数
         */
        private int deleteByRole(int roleId) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("roleId", roleId);
                return sysRoleMenuDao.delete(example);
        }

}
