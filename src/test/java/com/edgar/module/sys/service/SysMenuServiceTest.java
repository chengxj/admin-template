package com.edgar.module.sys.service;

import com.edgar.core.repository.BaseDao;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysMenuRoute;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.service.impl.SysMenuServiceImpl;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ IDUtils.class, QueryExample.class })
public class SysMenuServiceTest {
        @Mock
        private BaseDao<Integer, SysMenu> sysMenuDao;

        @Mock
        private BaseDao<Integer, SysMenuRoute> sysMenuRouteDao;

        @Mock
        private BaseDao<Integer, SysRoleMenu> sysRoleMenuDao;

        private SysMenuServiceImpl sysMenuService;

        @Before
        public void setUp() {
                MockitoAnnotations.initMocks(this);
                sysMenuService = new SysMenuServiceImpl();
                sysMenuService.setSysMenuDao(sysMenuDao);
                sysMenuService.setSysMenuRouteDao(sysMenuRouteDao);
                sysMenuService.setSysRoleMenuDao(sysRoleMenuDao);
        }

        @Test
        public void testGet() {
                SysMenu sysMenu = new SysMenu();
                sysMenu.setMenuName("测试地址");
                when(sysMenuDao.get(anyInt())).thenReturn(sysMenu);
                SysMenu result = sysMenuService.get(1);
                Assert.assertEquals(ToStringBuilder.reflectionToString(sysMenu,
                                ToStringStyle.SHORT_PREFIX_STYLE), ToStringBuilder
                                .reflectionToString(result, ToStringStyle.SHORT_PREFIX_STYLE));
                verify(sysMenuDao, only()).get(anyInt());
        }

//        @Test
//        public void testDelete() {
//                when(sysMenuDao.delete(any(QueryExample.class))).thenReturn(1);
//                when(sysMenuDao.deleteByPkAndVersion(anyInt(), anyLong())).thenReturn(1);
//                when(sysMenuRouteDao.delete(any(QueryExample.class))).thenReturn(1);
//                when(sysRoleMenuDao.delete(any(QueryExample.class))).thenReturn(1);
//
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//
//                int result = sysMenuService.deleteWithLock(1, 1L);
//                Assert.assertEquals(result, 1);
//
//                InOrder inOrder = inOrder(sysMenuDao);
//                inOrder.verify(sysMenuDao, times(1)).delete(any(QueryExample.class));
//                inOrder.verify(sysMenuDao, times(1)).deleteByPkAndVersion(anyInt(), anyLong());
//                verify(sysMenuRouteDao, only()).delete(any(QueryExample.class));
//                verify(sysRoleMenuDao, only()).delete(any(QueryExample.class));
//                verifyStatic(only());
//                QueryExample.newInstance();
//                Assert.assertTrue(example.getCriterias().contains(
//                                new Criteria("menuId", SqlOperator.EQ, 1)));
//        }
//
//        @Test
//        public void testQuery() {
//                final List<SysMenu> sysMenus = new ArrayList<SysMenu>();
//                sysMenus.add(new SysMenu());
//                sysMenus.add(new SysMenu());
//                final QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysMenuDao.query(any(QueryExample.class))).thenReturn(sysMenus);
//                List<SysMenu> result = sysMenuService.query(example);
//                Assert.assertEquals(sysMenus, result);
//                Assert.assertTrue(example.containCriteria(new Criteria("isRoot",
//                                SqlOperator.EQ, 0)));
//                verify(sysMenuDao, times(1)).query(any(QueryExample.class));
//        }
//
//        @Test
//        public void testSaveNull() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                try {
//                        sysMenuService.save(sysMenu);
//                } catch (SystemException e) {
//                        Map<String, Object> map = e.getPropertyMap();
//                        Assert.assertTrue(map.containsKey("menuName"));
//                }
//                verify(sysMenuDao, never()).insert(same(sysMenu));
//                verifyStatic(never());
//        }
//
//        @Test
//        public void testSaveLong() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuName("012345678901234567890123456789123");
//                try {
//                        sysMenuService.save(sysMenu);
//                } catch (SystemException e) {
//                        Map<String, Object> map = e.getPropertyMap();
//                        Assert.assertTrue(map.containsKey("menuName"));
//                }
//                verify(sysMenuDao, never()).insert(same(sysMenu));
//                verifyStatic(never());
//
//        }
//
//        @Test
//        public void testSavePattern() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuName("123");
//                sysMenu.setMenuPath("123");
//                try {
//                        sysMenuService.save(sysMenu);
//                } catch (SystemException e) {
//                        Map<String, Object> map = e.getPropertyMap();
//                        Assert.assertTrue(map.containsKey("menuPath"));
//                }
//                verify(sysMenuDao, never()).insert(same(sysMenu));
//                verifyStatic(never());
//
//        }
//
//        @Test
//        public void testSave() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuName("123");
//                sysMenu.setMenuPath("/123");
//                sysMenuService.save(sysMenu);
//                verify(sysMenuDao, only()).insert(same(sysMenu));
//                verify(sysMenuDao, never()).get(1);
//                verifyStatic(only());
//                IDUtils.getNextId();
//
//                Assert.assertFalse(sysMenu.getIsRoot());
//                Assert.assertEquals(-1, sysMenu.getParentId(), 0);
//        }
//
//        @Test
//        public void testSave2() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuName("123");
//                sysMenu.setMenuPath("/123");
//                sysMenu.setParentId(0);
//                sysMenuService.save(sysMenu);
//                verify(sysMenuDao, only()).insert(same(sysMenu));
//                verify(sysMenuDao, never()).get(1);
//                verifyStatic(only());
//                IDUtils.getNextId();
//
//                Assert.assertFalse(sysMenu.getIsRoot());
//                Assert.assertEquals(-1, sysMenu.getParentId(), 0);
//        }
//
//        @Test
//        public void testSaveChild() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuName("123");
//                sysMenu.setMenuPath("/123");
//                sysMenu.setParentId(1);
//                when(sysMenuDao.insert(any(SysMenu.class))).thenReturn(1);
//                when(sysMenuDao.get(anyInt())).thenReturn(new SysMenu());
//                sysMenuService.save(sysMenu);
//
//                InOrder inOrder = inOrder(sysMenuDao);
//                inOrder.verify(sysMenuDao, times(1)).insert(same(sysMenu));
//                inOrder.verify(sysMenuDao, times(1)).get(1);
//                verifyStatic(only());
//                IDUtils.getNextId();
//
//                Assert.assertFalse(sysMenu.getIsRoot());
//                Assert.assertNotEquals(-1, sysMenu.getParentId(), 0);
//        }
//
//        @Test
//        public void testSaveChildError() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuName("123");
//                sysMenu.setMenuPath("/123");
//                sysMenu.setParentId(1);
//                when(sysMenuDao.insert(any(SysMenu.class))).thenReturn(1);
//                when(sysMenuDao.get(anyInt())).thenReturn(null);
//                try {
//                        sysMenuService.save(sysMenu);
//                } catch (SystemException e) {
//                        Assert.assertEquals(BusinessCode.EXPIRED, e.getErrorCode());
//                }
//
//                InOrder inOrder = inOrder(sysMenuDao);
//                inOrder.verify(sysMenuDao, times(1)).insert(same(sysMenu));
//                inOrder.verify(sysMenuDao, times(1)).get(1);
//                verifyStatic(only());
//                IDUtils.getNextId();
//
//                Assert.assertFalse(sysMenu.getIsRoot());
//                Assert.assertNotEquals(-1, sysMenu.getParentId(), 0);
//        }
//
//        @Test
//        public void testUpdateNull() {
//                SysMenu sysMenu = new SysMenu();
//                try {
//                        sysMenuService.update(sysMenu);
//                } catch (SystemException e) {
//                        Map<String, Object> map = e.getPropertyMap();
//                        Assert.assertTrue(map.containsKey("menuId"));
//                }
//                verify(sysMenuDao, never()).update(same(sysMenu));
//                verifyStatic(never());
//        }
//
//        @Test
//        public void testUpdateLong() {
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuId(1);
//                sysMenu.setMenuName("012345678901234567890123456789123");
//                try {
//                        sysMenuService.update(sysMenu);
//                } catch (SystemException e) {
//                        Map<String, Object> map = e.getPropertyMap();
//                        Assert.assertTrue(map.containsKey("menuName"));
//                }
//                verify(sysMenuDao, never()).update(same(sysMenu));
//                verifyStatic(never());
//
//        }
//
//        @Test
//        public void testUpdatePattern() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuPath("123");
//                try {
//                        sysMenuService.update(sysMenu);
//                } catch (SystemException e) {
//                        Map<String, Object> map = e.getPropertyMap();
//                        Assert.assertTrue(map.containsKey("menuPath"));
//                }
//                verify(sysMenuDao, never()).update(same(sysMenu));
//                verifyStatic(never());
//
//        }
//
//        @Test
//        public void testUpdate() {
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                SysMenu sysMenu = new SysMenu();
//                sysMenu.setMenuId(1);
//                sysMenu.setMenuName("123");
//                sysMenu.setMenuPath("/123");
//                sysMenuService.update(sysMenu);
//                verify(sysMenuDao, only()).update(same(sysMenu));
//                verify(sysMenuDao, never()).get(1);
//                verifyStatic(never());
//        }
//
//        @Test
//        public void testGetRoute() {
//                QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysMenuRouteDao.query(any(QueryExample.class))).thenReturn(
//                                new ArrayList<SysMenuRoute>());
//                sysMenuService.getRoute(1);
//                verify(sysMenuRouteDao, only()).query(any(QueryExample.class));
//                verifyStatic(only());
//                QueryExample.newInstance();
//                Assert.assertTrue(example.containCriteria(new Criteria("menuId",
//                                SqlOperator.EQ, 1)));
//        }

//        @Test
//        public void testSaveRouteResNoRes() {
//                QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                when(sysMenuRouteDao.delete(any(QueryExample.class))).thenReturn(1);
//                MenuRouteCommand command = new MenuRouteCommand();
//                command.setMenuId(1);
//                sysMenuService.saveMenuRoute(command);
//                verify(sysMenuRouteDao, only()).delete(any(QueryExample.class));
//                verify(sysMenuRouteDao, never()).insert(anyListOf(SysMenuRoute.class));
//                verify(sysMenuDao, never()).get(anyInt());
//                verifyStatic(only());
//                QueryExample.newInstance();
//                Assert.assertTrue(example.containCriteria(new Criteria("menuId",
//                                SqlOperator.EQ, 1)));
//        }
//
//        @Test
//        public void testSaveRouteResOneRes() {
//                QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                when(sysMenuRouteDao.delete(any(QueryExample.class))).thenReturn(1);
//                when(sysMenuDao.get(anyInt())).thenReturn(new SysMenu());
//                MenuRouteCommand command = new MenuRouteCommand();
//                command.setMenuId(1);
//                Set<Integer> routeIds = new HashSet<Integer>();
//                routeIds.add(1);
//                command.setRouteIds(routeIds);
//                sysMenuService.saveMenuRoute(command);
//                InOrder inOrder = inOrder(sysMenuRouteDao);
//                inOrder.verify(sysMenuRouteDao, times(1)).delete(any(QueryExample.class));
//                inOrder.verify(sysMenuRouteDao, times(1)).insert(anyListOf(SysMenuRoute.class));
//                verify(sysMenuDao, only()).get(anyInt());
//                verifyStatic(only());
//                QueryExample.newInstance();
//                verifyStatic(only());
//                IDUtils.getNextId();
//                Assert.assertTrue(example.containCriteria(new Criteria("menuId",
//                                SqlOperator.EQ, 1)));
//        }
//
//        @Test
//        public void testSaveRouteResTwoRes() {
//                QueryExample example = QueryExample.newInstance();
//                mockStatic(QueryExample.class);
//                when(QueryExample.newInstance()).thenReturn(example);
//                mockStatic(IDUtils.class);
//                when(IDUtils.getNextId()).thenReturn(1);
//                when(sysMenuRouteDao.delete(any(QueryExample.class))).thenReturn(1);
//                when(sysMenuDao.get(anyInt())).thenReturn(null);
//                MenuRouteCommand command = new MenuRouteCommand();
//                command.setMenuId(1);
//                Set<Integer> routeIds = new HashSet<Integer>();
//                routeIds.add(1);
//                command.setRouteIds(routeIds);
//                try {
//                        sysMenuService.saveMenuRoute(command);
//                } catch (SystemException e) {
//                        Assert.assertEquals(BusinessCode.EXPIRED, e.getErrorCode());
//                }
//                finally {
//                        InOrder inOrder = inOrder(sysMenuRouteDao);
//                        inOrder.verify(sysMenuRouteDao, times(1)).delete(any(QueryExample.class));
//                        inOrder.verify(sysMenuRouteDao, times(1)).insert(
//                                        anyListOf(SysMenuRoute.class));
//                        verify(sysMenuDao, only()).get(anyInt());
//                        verifyStatic(only());
//                        QueryExample.newInstance();
//                        verifyStatic(only());
//                        IDUtils.getNextId();
//                        Assert.assertTrue(example.containCriteria(new Criteria("menuId",
//                                        SqlOperator.EQ, 1)));
//                }
//        }

}
