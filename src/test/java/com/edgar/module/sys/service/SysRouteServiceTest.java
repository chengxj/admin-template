package com.edgar.module.sys.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.inOrder;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.edgar.core.exception.BusinessCode;
import com.edgar.core.exception.SystemException;
import com.edgar.core.repository.Criteria;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.repository.SqlOperator;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.domain.SysRoute;
import com.edgar.module.sys.repository.domain.SysRouteRes;
import com.edgar.module.sys.service.RouteResCommand;
import com.edgar.module.sys.service.SysRouteServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ IDUtils.class, QueryExample.class })
public class SysRouteServiceTest {
        @Mock
        private CrudRepository<Integer, SysRoute> sysRouteDao;

        @Mock
        private CrudRepository<Integer, SysRouteRes> sysRouteResDao;

        @Mock
        private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;

        private SysRouteServiceImpl sysRouteService;

        @Before
        public void setUp() {
                MockitoAnnotations.initMocks(this);
                sysRouteService = new SysRouteServiceImpl();
                sysRouteService.setSysRouteDao(sysRouteDao);
                sysRouteService.setSysRoleRouteDao(sysRoleRouteDao);
                sysRouteService.setSysRouteResDao(sysRouteResDao);
        }

        @Test
        public void testGet() {
                SysRoute sysRoute = new SysRoute();
                sysRoute.setName("测试路由");
                when(sysRouteDao.get(anyInt())).thenReturn(sysRoute);
                SysRoute result = sysRouteService.get(1);
                Assert.assertEquals(ToStringBuilder.reflectionToString(sysRoute,
                                ToStringStyle.SHORT_PREFIX_STYLE), ToStringBuilder
                                .reflectionToString(result, ToStringStyle.SHORT_PREFIX_STYLE));
                verify(sysRouteDao, only()).get(anyInt());
        }

        @Test
        public void testDelete() {
                when(sysRouteDao.deleteByPkAndVersion(anyInt(), anyLong())).thenReturn(1);
                when(sysRoleRouteDao.delete(any(QueryExample.class))).thenReturn(1);
                when(sysRouteResDao.delete(any(QueryExample.class))).thenReturn(1);

                final QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);

                int result = sysRouteService.deleteWithLock(1, 1L);
                Assert.assertEquals(result, 1);
                verify(sysRouteDao, only()).deleteByPkAndVersion(anyInt(), anyLong());
                verify(sysRoleRouteDao, only()).delete(any(QueryExample.class));
                verify(sysRouteResDao, only()).delete(any(QueryExample.class));
                verifyStatic(only());
                QueryExample.newInstance();
                Assert.assertTrue(example.getCriterias().contains(
                                new Criteria("routeId", SqlOperator.EQUALS_TO, 1)));
        }

        @Test
        public void testPaination() {
                final List<SysRoute> sysRoutes = new ArrayList<SysRoute>();
                sysRoutes.add(new SysRoute());
                sysRoutes.add(new SysRoute());
                final Pagination<SysRoute> pagination = Pagination.newInstance(1, 10, 2, sysRoutes);
                final QueryExample example = QueryExample.newInstance();
                when(sysRouteDao.pagination(same(example), anyInt(), anyInt())).thenReturn(
                                pagination);
                Pagination<SysRoute> result = sysRouteService.pagination(example, 1, 10);
                Assert.assertEquals(pagination, result);
                Assert.assertTrue(example.containCriteria(new Criteria("isRoot",
                                SqlOperator.EQUALS_TO, 0)));
                verify(sysRouteDao, times(1)).pagination(same(example), anyInt(), anyInt());
        }

        @Test
        public void testFindAll() {
                final List<SysRoute> sysRoutes = new ArrayList<SysRoute>();
                sysRoutes.add(new SysRoute());
                sysRoutes.add(new SysRoute());
                final QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRouteDao.query(any(QueryExample.class))).thenReturn(sysRoutes);
                List<SysRoute> result = sysRouteService.findAll();
                Assert.assertEquals(sysRoutes, result);
                Assert.assertTrue(example.containCriteria(new Criteria("isRoot",
                                SqlOperator.EQUALS_TO, 0)));
                verify(sysRouteDao, times(1)).query(any(QueryExample.class));
        }

        @Test
        public void testSaveNull() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysRoute sysRoute = new SysRoute();
                try {
                        sysRouteService.save(sysRoute);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("name"));
                        Assert.assertTrue(map.containsKey("url"));
                }
                verify(sysRouteDao, never()).insert(same(sysRoute));
                verifyStatic(never());

        }

        @Test
        public void testSaveLong() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysRoute sysRoute = new SysRoute();
                sysRoute.setUrl("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
                sysRoute.setName("012345678901234567890123456789123");
                try {
                        sysRouteService.save(sysRoute);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("name"));
                        Assert.assertTrue(map.containsKey("url"));
                }
                verify(sysRouteDao, never()).insert(same(sysRoute));
                verifyStatic(never());

        }

        @Test
        public void testSavePattern() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysRoute sysRoute = new SysRoute();
                sysRoute.setUrl("123");
                sysRoute.setName("1233");
                try {
                        sysRouteService.save(sysRoute);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("url"));
                }
                verify(sysRouteDao, never()).insert(same(sysRoute));
                verifyStatic(never());

        }

        @Test
        public void testSave() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysRoute sysRoute = new SysRoute();
                sysRoute.setUrl("/123");
                sysRoute.setName("1233");
                sysRouteService.save(sysRoute);
                verify(sysRouteDao, only()).insert(same(sysRoute));
                verifyStatic(only());
                IDUtils.getNextId();
        }

        @Test
        public void testUpdateNull() {
                SysRoute sysRoute = new SysRoute();
                try {
                        sysRouteService.update(sysRoute);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("routeId"));
                        Assert.assertFalse(map.containsKey("name"));
                        Assert.assertFalse(map.containsKey("url"));
                }
                verify(sysRouteDao, never()).update(same(sysRoute));

        }

        @Test
        public void testUpdateLong() {
                SysRoute sysRoute = new SysRoute();
                sysRoute.setUrl("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
                sysRoute.setName("012345678901234567890123456789123");
                try {
                        sysRouteService.update(sysRoute);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("routeId"));
                        Assert.assertTrue(map.containsKey("name"));
                        Assert.assertTrue(map.containsKey("url"));
                }
                verify(sysRouteDao, never()).update(same(sysRoute));

        }

        @Test
        public void testUpdatePattern() {
                SysRoute sysRoute = new SysRoute();
                sysRoute.setRouteId(1);
                sysRoute.setUrl("123");
                try {
                        sysRouteService.update(sysRoute);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("url"));
                }
                verify(sysRouteDao, never()).update(same(sysRoute));

        }

        @Test
        public void testUpdate() {
                SysRoute sysRoute = new SysRoute();
                sysRoute.setRouteId(1);
                sysRoute.setUrl("/123");
                sysRoute.setName("1233");
                sysRouteService.update(sysRoute);
                verify(sysRouteDao, only()).update(same(sysRoute));
        }

        @Test
        public void testGetRes() {
                QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRouteResDao.query(any(QueryExample.class))).thenReturn(
                                new ArrayList<SysRouteRes>());
                sysRouteService.getResource(1);
                verify(sysRouteResDao, only()).query(any(QueryExample.class));
                verifyStatic(only());
                QueryExample.newInstance();
                Assert.assertTrue(example.containCriteria(new Criteria("routeId",
                                SqlOperator.EQUALS_TO, 1)));
        }

        @Test
        public void testSaveRouteResNoRes() {
                QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRouteResDao.delete(any(QueryExample.class))).thenReturn(1);
                RouteResCommand command = new RouteResCommand();
                command.setRouteId(1);
                sysRouteService.saveRouteRes(command);
                verify(sysRouteResDao, only()).delete(any(QueryExample.class));
                verify(sysRouteResDao, never()).insert(anyListOf(SysRouteRes.class));
                verifyStatic(only());
                QueryExample.newInstance();
                Assert.assertTrue(example.containCriteria(new Criteria("routeId",
                                SqlOperator.EQUALS_TO, 1)));
        }

        @Test
        public void testSaveRouteResOneRes() {
                QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRouteResDao.delete(any(QueryExample.class))).thenReturn(1);
                when(sysRouteDao.get(anyInt())).thenReturn(null);
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                RouteResCommand command = new RouteResCommand();
                command.setRouteId(1);
                Set<Integer> resourceIds = new HashSet<Integer>();
                resourceIds.add(1);
                command.setResourceIds(resourceIds);
                try {
                        sysRouteService.saveRouteRes(command);
                } catch (SystemException e) {
                        Assert.assertEquals(BusinessCode.EXPIRED, e.getErrorCode());
                } finally {
                        InOrder inOrder = inOrder(sysRouteResDao);
                        inOrder.verify(sysRouteResDao, times(1)).delete(any(QueryExample.class));
                        inOrder.verify(sysRouteResDao, times(1)).insert(anyListOf(SysRouteRes.class));
                        verify(sysRouteDao, only()).get(1);
                        verifyStatic(only());
                        QueryExample.newInstance();
                        verifyStatic(only());
                        IDUtils.getNextId();
                        Assert.assertTrue(example.containCriteria(new Criteria("routeId",
                                        SqlOperator.EQUALS_TO, 1)));
                        
                }
        }
        
        @Test
        public void testSaveRouteResTwoRes() {
                QueryExample example = QueryExample.newInstance();
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);
                when(sysRouteResDao.delete(any(QueryExample.class))).thenReturn(1);
                when(sysRouteDao.get(anyInt())).thenReturn(new SysRoute());
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                RouteResCommand command = new RouteResCommand();
                command.setRouteId(1);
                Set<Integer> resourceIds = new HashSet<Integer>();
                resourceIds.add(1);
                command.setResourceIds(resourceIds);
                try {
                        sysRouteService.saveRouteRes(command);
                } finally {
                        InOrder inOrder = inOrder(sysRouteResDao);
                        inOrder.verify(sysRouteResDao, times(1)).delete(any(QueryExample.class));
                        inOrder.verify(sysRouteResDao, times(1)).insert(anyListOf(SysRouteRes.class));
                        verify(sysRouteDao, only()).get(1);
                        verifyStatic(only());
                        QueryExample.newInstance();
                        verifyStatic(only());
                        IDUtils.getNextId();
                        Assert.assertTrue(example.containCriteria(new Criteria("routeId",
                                        SqlOperator.EQUALS_TO, 1)));
                        
                }
        }
}
