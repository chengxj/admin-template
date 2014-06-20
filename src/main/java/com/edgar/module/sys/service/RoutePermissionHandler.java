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

import com.edgar.core.command.CommandHandler;
import com.edgar.core.command.CommandResult;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.domain.SysRoute;
import com.edgar.module.sys.repository.domain.SysRouteRes;

@Service
public class RoutePermissionHandler implements CommandHandler<RoutePermission> {

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoute> sysRouteDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRouteRes> sysRouteResDao;

        @Override
        @Transactional
        public CommandResult<Integer> execute(RoutePermission command) {
                int roleId = command.getRoleId();
                int result = deleteByRole(roleId);
                Set<Integer> routeIds = command.getRouteIds();
                if (CollectionUtils.isEmpty(routeIds)) {
                        return CommandResult.newInstance(result);
                }
                Set<Integer> unModifiableRouteIds = Collections.unmodifiableSet(routeIds);
                saveRoleRoute(roleId, unModifiableRouteIds);

                if (command.isCascadeRes()) {
                        Set<Integer> resourceIds = getResourceIds(unModifiableRouteIds);
                        ResourcePermission resourcePermission = new ResourcePermission();
                        resourcePermission.setRoleId(command.getRoleId());
                        resourcePermission.setResourceIds(resourceIds);
                        command.setNextCommand(resourcePermission);
                }

                return CommandResult.newInstance(result);
        }

        /**
         * 根据路由查询路由资源关联
         * 
         * @param routeIds
         *                路由ID的集合
         * @return 资源的ID集合
         */
        private Set<Integer> getResourceIds(Set<Integer> routeIds) {
                Set<Integer> resourceIds = new HashSet<Integer>();
                for (Integer routeId : routeIds) {
                        QueryExample example = QueryExample.newInstance();
                        example.equalsTo("routeId", routeId);
                        example.addField("resourceId");
                        resourceIds.addAll(sysRouteResDao.querySingleColumn(example, Integer.class));
                }
                return resourceIds;
        }

        /**
         * 保存角色路由权限
         * 
         * @param roleId
         *                角色ID
         * @param routeIds
         *                路由的ID集合
         */
        private void saveRoleRoute(int roleId, Set<Integer> routeIds) {
                List<SysRoleRoute> sysRoleRoutes = new ArrayList<SysRoleRoute>();
                for (Integer routeId : routeIds) {
                        SysRoleRoute sysRoleRoute = new SysRoleRoute();
                        sysRoleRoute.setRoleId(roleId);
                        sysRoleRoute.setRouteId(routeId);
                        sysRoleRoute.setRoleRouteId(IDUtils.getNextId());
                        sysRoleRoutes.add(sysRoleRoute);
                        SysRoute sysRoute = sysRouteDao.get(routeId);
                        if (sysRoute == null) {
                                throw ExceptionFactory.isNull("路由不存在");
                        }
                }
                sysRoleRouteDao.insert(sysRoleRoutes);
        }

        /**
         * 根据角色删除路由权限
         * 
         * @param roleId
         *                角色ID
         * @return 返回删除的记录数
         */
        private int deleteByRole(int roleId) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("roleId", roleId);
                return sysRoleRouteDao.delete(example);
        }

}
