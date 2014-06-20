package com.edgar.module.sys.service;

import java.util.List;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edgar.core.command.CommandBus;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.repository.domain.SysRoleResource;
import com.edgar.module.sys.repository.domain.SysRoleRoute;

@Service
public class PermissionServiceImpl implements PermissionService {
        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoleResource> sysRoleResourceDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRole> sysRoleDao;

        @Autowired
        @Setter
        private CommandBus commandBus;

        @Override
        public List<SysRoleMenu> getMenu(int roleId) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("roleId", roleId);
                List<SysRoleMenu> sysRoleMenus = sysRoleMenuDao.query(example);
                return sysRoleMenus;
        }

        @Override
        public List<SysRoleRoute> getRoute(int roleId) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("roleId", roleId);
                List<SysRoleRoute> sysRoleRoutes = sysRoleRouteDao.query(example);
                return sysRoleRoutes;
        }

        @Override
        public List<SysRoleResource> getResource(int roleId) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("roleId", roleId);
                List<SysRoleResource> sysRoleResources = sysRoleResourceDao.query(example);
                return sysRoleResources;
        }

        @Override
        @Transactional
        public void saveMenuPermission(MenuPermission command) {
                command.setCascadeRoute(true);
                commandBus.executeCommand(command);
                checkRole(command.getRoleId());
        }

        @Override
        @Transactional
        public void saveRoutePermission(RoutePermission command) {
                command.setCascadeRes(true);
                commandBus.executeCommand(command);

                checkRole(command.getRoleId());
        }

        @Override
        @Transactional
        public void saveResourcePermission(ResourcePermission command) {
                commandBus.executeCommand(command);
                checkRole(command.getRoleId());
        }

        /**
         * 检查角色是否存在，如果不存在，则抛出空的异常
         * 
         * @param roleId
         *                角色ID
         */
        private void checkRole(int roleId) {
                SysRole sysRole = sysRoleDao.get(roleId);
                if (sysRole == null) {
                        throw ExceptionFactory.isNull("msg.error.role.notexists");
                }
        }

}
