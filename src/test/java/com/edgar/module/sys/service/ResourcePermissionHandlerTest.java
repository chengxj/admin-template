//package com.edgar.module.sys.service;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.anyListOf;
//import static org.mockito.Matchers.same;
//import static org.mockito.Mockito.inOrder;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.only;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InOrder;
//import org.mockito.Mock;
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
//import com.edgar.module.sys.repository.domain.SysResource;
//import com.edgar.module.sys.repository.domain.SysRole;
//import com.edgar.module.sys.repository.domain.SysRoleResource;
//import com.edgar.module.sys.service.ResourcePermission;
//import com.edgar.module.sys.service.ResourcePermissionHandler;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ QueryExample.class, IDUtils.class })
//public class ResourcePermissionHandlerTest {
//        @Mock
//        private CrudRepository<Integer, SysRole> sysRoleDao;
//
//        @Mock
//        private CrudRepository<Integer, SysResource> sysResourceDao;
//
//        @Mock
//        private CrudRepository<Integer, SysRoleResource> sysRoleResourceDao;
//
//        private ResourcePermissionHandler handler;
//
//        @Before
//        public void setUp() {
//                handler = new ResourcePermissionHandler();
//                handler.setSysRoleResourceDao(sysRoleResourceDao);
//                handler.setSysResourceDao(sysResourceDao);
//        }
//
//        @Test
//        public void testSaveRoleResNoPermission() {
//                ResourcePermission command = new ResourcePermission();
//                command.setRoleId(1);
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysRoleResourceDao.delete(same(example))).thenReturn(1);
//                CommandResult<Integer> result = handler.execute(command);
//                verify(sysRoleResourceDao, only()).delete(any(QueryExample.class));
//                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
//                                SqlOperator.EQUALS_TO, 1)));
//                Assert.assertEquals(1, result.getResult(), 0);
//        }
//        
//        @Test
//        public void testSaveRoleRouteOnePermissionNullRes() {
//                ResourcePermission command = new ResourcePermission();
//                command.setRoleId(1);
//
//                Set<Integer> resourceIds = new HashSet<Integer>();
//                resourceIds.add(1);
//                command.setResourceIds(resourceIds);
//
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysRoleResourceDao.delete(same(example))).thenReturn(1);
//                when(sysResourceDao.get(anyInt())).thenReturn(null);
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                try {
//                        handler.execute(command);
//                } catch (SystemException e) {
//                        Assert.assertEquals(BusinessCode.NULL, e.getErrorCode());
//                }
//                InOrder inOrder = inOrder(sysRoleResourceDao, sysResourceDao);
//                inOrder.verify(sysRoleResourceDao, times(1)).delete(any(QueryExample.class));
//                inOrder.verify(sysResourceDao, times(1)).get(anyInt());
//                inOrder.verify(sysRoleResourceDao, never()).insert(anyListOf(SysRoleResource.class));
//                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
//                                SqlOperator.EQUALS_TO, 1)));
//        }
//        
//        @Test
//        public void testSaveRoleRouteOnePermission() {
//                ResourcePermission command = new ResourcePermission();
//                command.setRoleId(1);
//
//                Set<Integer> resourceIds = new HashSet<Integer>();
//                resourceIds.add(1);
//                command.setResourceIds(resourceIds);
//
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysRoleResourceDao.delete(same(example))).thenReturn(1);
//                when(sysResourceDao.get(anyInt())).thenReturn(new SysResource());
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                try {
//                        handler.execute(command);
//                } catch (SystemException e) {
//                        Assert.assertEquals(BusinessCode.NULL, e.getErrorCode());
//                }
//                InOrder inOrder = inOrder(sysRoleResourceDao, sysResourceDao);
//                inOrder.verify(sysRoleResourceDao, times(1)).delete(any(QueryExample.class));
//                inOrder.verify(sysResourceDao, times(1)).get(anyInt());
//                inOrder.verify(sysRoleResourceDao, times(1)).insert(anyListOf(SysRoleResource.class));
//                Assert.assertTrue(example.containCriteria(new Criteria("roleId",
//                                SqlOperator.EQUALS_TO, 1)));
//        }
//}
