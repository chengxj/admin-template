package com.edgar.module.sys.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.edgar.core.command.CommandResult;
import com.edgar.core.exception.BusinessCode;
import com.edgar.core.exception.SystemException;
import com.edgar.core.repository.Criteria;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.repository.SqlOperator;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.service.MenuPermission;
import com.edgar.module.sys.service.MenuPermissionHandler;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ QueryExample.class, IDUtils.class })
public class MenuPermissionHandlerTest {
        @Mock
        private CrudRepository<Integer, SysRole> sysRoleDao;

        @Mock
        private CrudRepository<Integer, SysMenu> sysMenuDao;

        @Mock
        private CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao;

        @Mock
        private CrudRepository<Integer, SysMenuRoute> sysMenuRouteDao;

        private MenuPermissionHandler handler;

        @Before
        public void setUp() {
                MockitoAnnotations.initMocks(this);
                handler = new MenuPermissionHandler();
                handler.setSysRoleMenuDao(sysRoleMenuDao);
                handler.setSysMenuDao(sysMenuDao);
                handler.setSysMenuRouteDao(sysMenuRouteDao);
        }

        @Test
        public void testSaveRoleMenuNoPermission() {
                MenuPermission command = new MenuPermission();
                command.setRoleId(1);
                final QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRoleMenuDao.delete(same(example))).thenReturn(1);
                CommandResult<Integer> result = handler.execute(command);
                verify(sysRoleMenuDao, only()).delete(any(QueryExample.class));
                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
                                SqlOperator.EQUALS_TO, 1)));
                Assert.assertEquals(1, result.getResult(), 0);
        }

        @Test
        public void testSaveRoleMenuOnePermissionNullMenu() {
                MenuPermission command = new MenuPermission();
                command.setRoleId(1);
                Set<Integer> menuIds = new HashSet<Integer>();
                menuIds.add(1);
                command.setMenuIds(menuIds);
                final QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRoleMenuDao.delete(same(example))).thenReturn(1);
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                try {
                        handler.execute(command);
                } catch (SystemException e) {
                        Assert.assertEquals(BusinessCode.NULL, e.getErrorCode());
                }
                finally {
                        InOrder inOrder = inOrder(sysRoleMenuDao);
                        inOrder.verify(sysRoleMenuDao, times(1)).delete(any(QueryExample.class));
                        Assert.assertTrue(example.containCriteria(new Criteria("roleId",
                                        SqlOperator.EQUALS_TO, 1)));
                        inOrder.verify(sysRoleMenuDao, never())
                                        .insert(anyListOf(SysRoleMenu.class));
                }
        }

        @Test
        public void testSaveRoleMenuOnePermission() {
                MenuPermission command = new MenuPermission();
                command.setRoleId(1);
                Set<Integer> menuIds = new HashSet<Integer>();
                menuIds.add(1);
                command.setMenuIds(menuIds);
                final QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRoleMenuDao.delete(same(example))).thenReturn(1);
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);

                SysMenu sysMenu = new SysMenu();
                sysMenu.setMenuId(1);
                sysMenu.setParentId(-1);
                when(sysMenuDao.get(anyInt())).thenReturn(sysMenu);

                CommandResult<Integer> result = handler.execute(command);
                InOrder inOrder = inOrder(sysRoleMenuDao, sysMenuRouteDao);
                inOrder.verify(sysRoleMenuDao, times(1)).delete(any(QueryExample.class));
                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
                                SqlOperator.EQUALS_TO, 1)));
                inOrder.verify(sysRoleMenuDao, times(1)).insert(anyListOf(SysRoleMenu.class));
                Assert.assertEquals(1, result.getResult(), 0);
                inOrder.verify(sysMenuRouteDao, never()).querySingleColumn(
                                any(QueryExample.class), eq(Integer.class));

                Assert.assertNull(command.getNextCommand());
        }

        @Test
        public void testSaveRoleMenuOnePermissionWithParent() {
                MenuPermission command = new MenuPermission();
                command.setCascadeRoute(true);
                command.setRoleId(1);
                Set<Integer> menuIds = new HashSet<Integer>();
                menuIds.add(1);
                command.setMenuIds(menuIds);
                final QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRoleMenuDao.delete(same(example))).thenReturn(1);
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);

                SysMenu sysMenu = new SysMenu();
                sysMenu.setMenuId(1);
                sysMenu.setParentId(2);
                when(sysMenuDao.get(anyInt())).thenReturn(sysMenu);
                
                List<Integer> routeIds = new ArrayList<Integer>();
                routeIds.add(1);
                routeIds.add(2);
                routeIds.add(3);
                routeIds.add(4);
                when(sysMenuRouteDao.querySingleColumn(any(QueryExample.class), eq(Integer.class)))
                                .thenReturn(routeIds);

                CommandResult<Integer> result = handler.execute(command);
                InOrder inOrder = inOrder(sysRoleMenuDao, sysMenuDao, sysMenuRouteDao);
                inOrder.verify(sysRoleMenuDao, times(1)).delete(any(QueryExample.class));
                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
                                SqlOperator.EQUALS_TO, 1)));
                inOrder.verify(sysMenuDao, times(2)).get(anyInt());
                inOrder.verify(sysRoleMenuDao, times(1)).insert(anyListOf(SysRoleMenu.class));
                Assert.assertEquals(1, result.getResult(), 0);
                inOrder.verify(sysMenuRouteDao, atLeastOnce()).querySingleColumn(
                                any(QueryExample.class), eq(Integer.class));
        }

        @Test
        public void testSaveRoleMenuTowPermissionWithOneParent() {
                MenuPermission command = new MenuPermission();
                command.setRoleId(1);
                Set<Integer> menuIds = new HashSet<Integer>();
                menuIds.add(1);
                menuIds.add(3);
                command.setMenuIds(menuIds);
                final QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRoleMenuDao.delete(same(example))).thenReturn(1);
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);

                SysMenu sysMenu = new SysMenu();
                sysMenu.setMenuId(1);
                sysMenu.setParentId(2);
                when(sysMenuDao.get(anyInt())).thenReturn(sysMenu);
                
                List<Integer> routeIds = new ArrayList<Integer>();
                when(sysMenuRouteDao.querySingleColumn(any(QueryExample.class), eq(Integer.class)))
                                .thenReturn(routeIds);

                CommandResult<Integer> result = handler.execute(command);
                InOrder inOrder = inOrder(sysRoleMenuDao, sysMenuDao);
                inOrder.verify(sysRoleMenuDao, times(1)).delete(any(QueryExample.class));
                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
                                SqlOperator.EQUALS_TO, 1)));
                inOrder.verify(sysMenuDao, times(3)).get(anyInt());
                inOrder.verify(sysRoleMenuDao, times(1)).insert(anyListOf(SysRoleMenu.class));
                Assert.assertEquals(1, result.getResult(), 0);
        }

        @Test
        public void testSaveRoleMenuTowPermissionWithTwoParent() {
                MenuPermission command = new MenuPermission();
                command.setRoleId(1);
                Set<Integer> menuIds = new HashSet<Integer>();
                menuIds.add(1);
                menuIds.add(3);
                command.setMenuIds(menuIds);
                final QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRoleMenuDao.delete(same(example))).thenReturn(1);
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);

                SysMenu sysMenu = new SysMenu();
                sysMenu.setMenuId(1);
                sysMenu.setParentId(2);
                when(sysMenuDao.get(1)).thenReturn(sysMenu);

                sysMenu = new SysMenu();
                sysMenu.setMenuId(2);
                when(sysMenuDao.get(2)).thenReturn(sysMenu);

                sysMenu = new SysMenu();
                sysMenu.setMenuId(3);
                sysMenu.setParentId(4);
                when(sysMenuDao.get(3)).thenReturn(sysMenu);

                sysMenu = new SysMenu();
                sysMenu.setMenuId(4);
                sysMenu.setParentId(-1);
                when(sysMenuDao.get(4)).thenReturn(sysMenu);

                CommandResult<Integer> result = handler.execute(command);
                InOrder inOrder = inOrder(sysRoleMenuDao, sysMenuDao);
                inOrder.verify(sysRoleMenuDao, times(1)).delete(any(QueryExample.class));
                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
                                SqlOperator.EQUALS_TO, 1)));
                inOrder.verify(sysMenuDao, times(4)).get(anyInt());
                inOrder.verify(sysRoleMenuDao, times(1)).insert(anyListOf(SysRoleMenu.class));
                Assert.assertEquals(1, result.getResult(), 0);
        }
}
