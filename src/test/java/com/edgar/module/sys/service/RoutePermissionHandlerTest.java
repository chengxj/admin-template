//package com.edgar.module.sys.service;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.anyListOf;
//import static org.mockito.Matchers.eq;
//import static org.mockito.Matchers.same;
//import static org.mockito.Mockito.inOrder;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.only;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.apache.commons.collections4.CollectionUtils;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InOrder;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import com.edgar.core.command.CommandResult;
//import com.edgar.core.exception.BusinessCode;
//import com.edgar.core.exception.SystemException;
//import com.edgar.core.repository.Criteria;
//import com.edgar.core.repository.CrudRepository;
//import com.edgar.core.repository.IDUtils;
//import com.edgar.core.repository.QueryExample;
//import com.edgar.core.repository.SqlOperator;
//import com.edgar.module.sys.repository.domain.SysRole;
//import com.edgar.module.sys.repository.domain.SysRoleRoute;
//import com.edgar.module.sys.repository.domain.SysRoute;
//import com.edgar.module.sys.repository.domain.SysRouteRes;
//import com.edgar.module.sys.service.ResourcePermission;
//import com.edgar.module.sys.service.RoutePermission;
//import com.edgar.module.sys.service.RoutePermissionHandler;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ QueryExample.class, IDUtils.class })
//public class RoutePermissionHandlerTest {
//
//        @Mock
//        private CrudRepository<Integer, SysRole> sysRoleDao;
//
//        @Mock
//        private CrudRepository<Integer, SysRoute> sysRouteDao;
//
//        @Mock
//        private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;
//
//        @Mock
//        private CrudRepository<Integer, SysRouteRes> sysRouteResDao;
//
//        private RoutePermissionHandler handler;
//
//        @Before
//        public void setUp() {
//                MockitoAnnotations.initMocks(this);
//                handler = new RoutePermissionHandler();
//                handler.setSysRoleRouteDao(sysRoleRouteDao);
//                handler.setSysRouteDao(sysRouteDao);
//                handler.setSysRouteResDao(sysRouteResDao);
//        }
//
//        @Test
//        public void testSaveRoleRouteNoPermission() {
//                RoutePermission command = new RoutePermission();
//                command.setRoleId(1);
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysRoleRouteDao.delete(same(example))).thenReturn(1);
//                CommandResult<Integer> result = handler.execute(command);
//                verify(sysRoleRouteDao, only()).delete(any(QueryExample.class));
//                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
//                                SqlOperator.EQUALS_TO, 1)));
//                Assert.assertEquals(1, result.getResult(), 0);
//                Assert.assertTrue(CollectionUtils.isEmpty(command.getRouteIds()));
//        }
//
//        @Test
//        public void testSaveRoleRouteOnePermissionNullRoute() {
//                RoutePermission command = new RoutePermission();
//                command.setRoleId(1);
//
//                Set<Integer> routeIds = new HashSet<Integer>();
//                routeIds.add(1);
//                command.setRouteIds(routeIds);
//
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysRoleRouteDao.delete(same(example))).thenReturn(1);
//                when(sysRouteDao.get(anyInt())).thenReturn(null);
//
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//
//                try {
//                        handler.execute(command);
//                } catch (SystemException e) {
//                        Assert.assertEquals(BusinessCode.NULL, e.getErrorCode());
//                        // Assert.assertTrue(CollectionUtils.isEmpty(command.getRouteIds()));
//                }
//                InOrder inOrder = inOrder(sysRoleRouteDao, sysRouteDao);
//                inOrder.verify(sysRoleRouteDao, times(1)).delete(any(QueryExample.class));
//                inOrder.verify(sysRouteDao, times(1)).get(anyInt());
//                inOrder.verify(sysRoleRouteDao, never()).insert(anyListOf(SysRoleRoute.class));
//                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
//                                SqlOperator.EQUALS_TO, 1)));
//                // Assert.assertTrue(CollectionUtils.isEmpty(command.getRouteIds()));
//        }
//
//        @Test
//        public void testSaveRoleRouteOnePermissionNoRes() {
//                RoutePermission command = new RoutePermission();
//                command.setRoleId(1);
//
//                Set<Integer> routeIds = new HashSet<Integer>();
//                routeIds.add(1);
//                command.setRouteIds(routeIds);
//
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysRoleRouteDao.delete(same(example))).thenReturn(1);
//                when(sysRouteDao.get(anyInt())).thenReturn(new SysRoute());
//                when(sysRouteResDao.querySingleColumn(any(QueryExample.class), eq(Integer.class)))
//                                .thenReturn(new ArrayList<Integer>());
//
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//
//                handler.execute(command);
//                InOrder inOrder = inOrder(sysRoleRouteDao, sysRouteDao, sysRouteResDao);
//                inOrder.verify(sysRoleRouteDao, times(1)).delete(any(QueryExample.class));
//                inOrder.verify(sysRouteDao, times(1)).get(anyInt());
//                inOrder.verify(sysRoleRouteDao, times(1)).insert(anyListOf(SysRoleRoute.class));
//                inOrder.verify(sysRouteResDao, never()).querySingleColumn(any(QueryExample.class),
//                                eq(Integer.class));
//                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
//                                SqlOperator.EQUALS_TO, 1)));
//        }
//
//        @Test
//        public void testSaveRoleRouteTwoPermissionTwoRes() {
//                RoutePermission command = new RoutePermission();
//                command.setRoleId(1);
//
//                Set<Integer> routeIds = new HashSet<Integer>();
//                routeIds.add(1);
//                routeIds.add(2);
//                command.setRouteIds(routeIds);
//                command.setCascadeRes(true);
//
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysRoleRouteDao.delete(same(example))).thenReturn(1);
//                when(sysRouteDao.get(anyInt())).thenReturn(new SysRoute());
//
//                List<Integer> resIds = new ArrayList<Integer>();
//                resIds.add(1);
//                resIds.add(2);
//                when(sysRouteResDao.querySingleColumn(any(QueryExample.class), eq(Integer.class)))
//                                .thenReturn(resIds);
//
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//
//                handler.execute(command);
//                InOrder inOrder = inOrder(sysRoleRouteDao, sysRouteDao, sysRouteResDao);
//                inOrder.verify(sysRoleRouteDao, times(1)).delete(any(QueryExample.class));
//                inOrder.verify(sysRouteDao, times(2)).get(anyInt());
//                inOrder.verify(sysRoleRouteDao, times(1)).insert(anyListOf(SysRoleRoute.class));
//                inOrder.verify(sysRouteResDao, times(2)).querySingleColumn(any(QueryExample.class),
//                                eq(Integer.class));
//                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
//                                SqlOperator.EQUALS_TO, 1)));
//                Assert.assertTrue(command.nextCommand() instanceof ResourcePermission);
//                
//                ResourcePermission resourcePermission = new ResourcePermission();
//                resourcePermission.setRoleId(1);
//                resourcePermission.setResourceIds(new HashSet<Integer>(resIds));
//                Assert.assertEquals(resourcePermission, command.nextCommand());
//        }
//
//}
