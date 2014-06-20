package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.validator.SysMenuUpdateValidator;
import com.edgar.module.sys.validator.SysMenuValidator;

/**
 * 系统菜单的业务逻辑实现
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
        @Autowired
        @Setter
        private CrudRepository<Integer, SysMenu> sysMenuDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao;

        private ValidatorStrategy validator = new SysMenuValidator();

        private ValidatorStrategy updateValidator = new SysMenuUpdateValidator();

        @Override
        public SysMenu get(int menuId) {
                Assert.notNull(menuId);
                return sysMenuDao.get(menuId);
        }

        @Override
        @Transactional
        public int save(SysMenu sysMenu) {
                Assert.notNull(sysMenu);
                sysMenu.setIsRoot(false);
                if (sysMenu.getParentId() == null || sysMenu.getParentId() == 0) {
                        sysMenu.setParentId(-1);
                }
                validator.validator(sysMenu);
                sysMenu.setMenuId(IDUtils.getNextId());
                int result = sysMenuDao.insert(sysMenu);
                if (sysMenu.getParentId() != -1) {
                        SysMenu parent = get(sysMenu.getParentId());
                        if (parent == null) {
                                throw ExceptionFactory.isNull("msg.error.menu.notexists");
                        }
                }
                return result;
        }

        @Override
        @Transactional
        public int update(SysMenu sysMenu) {
                updateValidator.validator(sysMenu);
                return sysMenuDao.update(sysMenu);
        }

        @Override
        @Transactional
        public int deleteWithLock(int menuId, long updatedTime) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("parentId", menuId);
                sysMenuDao.delete(example);
                int result = sysMenuDao.deleteByPkAndVersion(menuId, updatedTime);
                example.clear();
                example.equalsTo("menuId", menuId);
                sysRoleMenuDao.delete(example);
                sysMenuRouteDao.delete(example);
                return result;
        }

        @Override
        public List<SysMenu> query(QueryExample example) {
                example.asc("sorted");
                example.equalsTo("isRoot", 0);
                return sysMenuDao.query(example);
        }

        @Override
        public List<SysMenuRoute> getRoute(int menuId) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("menuId", menuId);
                return sysMenuRouteDao.query(example);
        }

        @Override
        @Transactional
        public void saveMenuRoute(MenuRouteCommand command) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("menuId", command.getMenuId());
                sysMenuRouteDao.delete(example);
                List<SysMenuRoute> sysMenuRoutes = new ArrayList<SysMenuRoute>();
                if (command.getRouteIds() == null) {
                        return;
                }
                for (Integer routeId : command.getRouteIds()) {
                        SysMenuRoute sysMenuRoute = new SysMenuRoute();
                        sysMenuRoute.setMenuRouteId(IDUtils.getNextId());
                        sysMenuRoute.setMenuId(command.getMenuId());
                        sysMenuRoute.setRouteId(routeId);
                        sysMenuRoutes.add(sysMenuRoute);
                }
                sysMenuRouteDao.insert(sysMenuRoutes);
                SysMenu sysMenu = sysMenuDao.get(command.getMenuId());
                if (sysMenu == null) {
                        throw ExceptionFactory.isNull("msg.error.menu.notexists");
                }
        }
}
