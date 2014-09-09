package com.edgar.module.sys.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.edgar.core.exception.SystemException;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.repository.domain.SysUserRole;
import com.edgar.module.sys.service.impl.SysUserServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest(IDUtils.class)
public class SysUserServiceTest {
        @Mock
        private CrudRepository<Integer, SysUser> sysUserDao;

        @Mock
        private CrudRepository<Integer, SysUserRole> sysUserRoleDao;

        private SysUserServiceImpl sysUserService;

        @Before
        public void setUp() {
                MockitoAnnotations.initMocks(this);
                sysUserService = new SysUserServiceImpl();
                sysUserService.setSysUserDao(sysUserDao);
                sysUserService.setSysUserRoleDao(sysUserRoleDao);
        }

        @Test
        public void testGet() {
                SysUser sysUser = new SysUser();
                sysUser.setUsername("Edgar");
                sysUser.setPassword("Edgar");
                when(sysUserDao.get(anyInt())).thenReturn(sysUser);
                SysUser result = sysUserService.get(1);
                verify(sysUserDao, only()).get(1);
                Assert.assertNull(result.getPassword());
        }

        @Test
        public void testPaination() {
                final List<SysUser> sysUsers = new ArrayList<SysUser>();
                sysUsers.add(new SysUser());
                sysUsers.add(new SysUser());
                final Pagination<SysUser> pagination = Pagination.newInstance(1, 10, 2, sysUsers);
                final QueryExample example = QueryExample.newInstance();
                when(sysUserDao.pagination(same(example), anyInt(), anyInt())).thenReturn(
                                pagination);
                Pagination<SysUser> result = sysUserService.pagination(example, 1, 10);
                Assert.assertEquals(pagination, result);
                verify(sysUserDao, times(1)).pagination(same(example), anyInt(), anyInt());
        }

        @Test
        public void testCheckUsername() {
                final List<SysUser> sysUsers = new ArrayList<SysUser>();
                when(sysUserDao.query(any(QueryExample.class))).thenReturn(sysUsers);
                boolean result = sysUserService.checkUsername("Edgar");
                Assert.assertTrue(result);
                verify(sysUserDao, only()).query(any(QueryExample.class));
                sysUsers.add(new SysUser());
                result = sysUserService.checkUsername("Edgar");
                Assert.assertFalse(result);
        }

        @Test
        public void testGetRoles() {
                final List<SysUserRole> sysUserRoles = new ArrayList<SysUserRole>();
                sysUserRoles.add(new SysUserRole());
                when(sysUserRoleDao.query(any(QueryExample.class))).thenReturn(sysUserRoles);
                List<SysUserRole> results = sysUserService.getRoles(1);
                Assert.assertEquals(sysUserRoles, results);
                verify(sysUserRoleDao, only()).query(any(QueryExample.class));
        }

        @Test
        public void tesetDeleteByVersion() {
                when(sysUserDao.deleteByPkAndVersion(anyInt(), anyLong())).thenReturn(1l);
                when(sysUserRoleDao.delete(any(QueryExample.class))).thenReturn(1l);
            sysUserService.deleteWithLock(1, 1L);

                InOrder inOrder = inOrder(sysUserRoleDao, sysUserDao);
                inOrder.verify(sysUserRoleDao, times(1)).delete(any(QueryExample.class));
                inOrder.verify(sysUserDao, times(1)).deleteByPkAndVersion(1, 1L);
        }

        @Test
        public void testSaveUserNull() {
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("username", "username不能为null");
                map.put("fullName", "fullName不能为null");
                map.put("password", "password不能为null");
                try {
                        sysUserService.save(sysUser);
                } catch (SystemException e) {
                        Map<String, Object> propertyMap = e.getPropertyMap();
                        Assert.assertEquals(map, propertyMap);
                }
                finally {
                        verify(sysUserDao, never()).insert(any(SysUser.class));
                        verify(sysUserRoleDao, never()).insert(any(SysUserRole.class));
                }

        }

        @Test
        public void testSaveUserLength() {
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                sysUser.setUsername("z012345678901234567890123456789123012345678901234567890123456789123");
                sysUser.setPassword("012345678901234567890123456789123");
                sysUser.setFullName("012345678901234567890123456789123");
                sysUser.setEmail("012345678901234567890123456789123012345678901234567890123456789123");
                try {
                        sysUserService.save(sysUser);
                } catch (SystemException e) {
                        Map<String, Object> propertyMap = e.getPropertyMap();
                        Assert.assertTrue(propertyMap.containsKey("username"));
                        Assert.assertTrue(propertyMap.containsKey("fullName"));
                        Assert.assertTrue(propertyMap.containsKey("password"));
                        Assert.assertTrue(propertyMap.containsKey("email"));
                }
                finally {
                        verify(sysUserDao, never()).insert(any(SysUser.class));
                        verify(sysUserRoleDao, never()).insert(any(SysUserRole.class));
                }

        }

        @Test
        public void testSaveUserPattern() {
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                sysUser.setUsername("123");
                sysUser.setPassword("2322434");
                sysUser.setFullName("#$#$#$#$");
                sysUser.setEmail("$#$#@ddd.");
                try {
                        sysUserService.save(sysUser);
                } catch (SystemException e) {
                        Map<String, Object> propertyMap = e.getPropertyMap();
                        Assert.assertTrue(propertyMap.containsKey("username"));
                        Assert.assertFalse(propertyMap.containsKey("fullName"));
                        Assert.assertFalse(propertyMap.containsKey("password"));
                        Assert.assertTrue(propertyMap.containsKey("email"));
                }
                finally {
                        verify(sysUserDao, never()).insert(any(SysUser.class));
                        verify(sysUserRoleDao, never()).insert(any(SysUserRole.class));
                }

        }

        @Test
        public void testSaveUserNoRole() {
                PowerMockito.mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                when(sysUserDao.insert(any(SysUser.class)));
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                sysUser.setUsername("z123");
                sysUser.setPassword("2322434");
                sysUser.setFullName("#$#$#$#$");
                sysUser.setEmail("$#$#@ddd");
                String password = sysUser.getPassword();
                sysUserService.save(sysUser);
                Assert.assertNotEquals(password, sysUser.getPassword());
                verify(sysUserDao, only()).insert(any(SysUser.class));
                verify(sysUserRoleDao, never()).insert(any(SysUserRole.class));
                PowerMockito.verifyStatic(only());
                IDUtils.getNextId();
        }

        @Test
        public void testSaveUserOneRole() {
                PowerMockito.mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                when(sysUserDao.insert(any(SysUser.class)));
                when(sysUserRoleDao.insert(anyListOf(SysUserRole.class)));
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                sysUser.setRoleIds("1");
                sysUser.setUsername("z123");
                sysUser.setPassword("2322434");
                sysUser.setFullName("#$#$#$#$");
                sysUser.setEmail("$#$#@ddd");
                sysUserService.save(sysUser);
                verify(sysUserDao, only()).insert(any(SysUser.class));
                verify(sysUserRoleDao, only()).insert(anyListOf(SysUserRole.class));
                PowerMockito.verifyStatic(times(2));
                IDUtils.getNextId();
        }

        @Test
        public void testSaveUserTwoRole() {
                PowerMockito.mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                when(sysUserDao.insert(any(SysUser.class)));
                when(sysUserRoleDao.insert(anyListOf(SysUserRole.class)));
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                sysUser.setRoleIds("1,2");
                sysUser.setUsername("z123");
                sysUser.setPassword("2322434");
                sysUser.setFullName("#$#$#$#$");
                sysUser.setEmail("$#$#@ddd");
                sysUserService.save(sysUser);
                verify(sysUserDao, only()).insert(any(SysUser.class));
                verify(sysUserRoleDao, only()).insert(anyListOf(SysUserRole.class));
                PowerMockito.verifyStatic(times(3));
                IDUtils.getNextId();
        }

        @Test
        public void testUpdateUserNull() {
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                try {
                        sysUserService.update(sysUser);
                } catch (SystemException e) {
                        Map<String, Object> propertyMap = e.getPropertyMap();
                        Assert.assertTrue(propertyMap.containsKey("userId"));
                        Assert.assertFalse(propertyMap.containsKey("username"));
                        Assert.assertFalse(propertyMap.containsKey("fullName"));
                        Assert.assertFalse(propertyMap.containsKey("password"));
                        Assert.assertFalse(propertyMap.containsKey("email"));
                }
                verify(sysUserDao, never()).update(any(SysUser.class));
                verify(sysUserRoleDao, never()).delete(any(QueryExample.class));
                verify(sysUserRoleDao, never()).insert(anyListOf(SysUserRole.class));

        }

        @Test
        public void testUpdateUserLength() {
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                sysUser.setUserId(1);
                sysUser.setUsername("z012345678901234567890123456789123012345678901234567890123456789123");
                sysUser.setPassword("012345678901234567890123456789123");
                sysUser.setFullName("012345678901234567890123456789123");
                sysUser.setEmail("012345678901234567890123456789123012345678901234567890123456789123");
                try {
                        sysUserService.update(sysUser);
                } catch (SystemException e) {
                        Map<String, Object> propertyMap = e.getPropertyMap();
                        Assert.assertTrue(propertyMap.containsKey("username"));
                        Assert.assertTrue(propertyMap.containsKey("fullName"));
                        Assert.assertTrue(propertyMap.containsKey("password"));
                        Assert.assertTrue(propertyMap.containsKey("email"));
                }
                verify(sysUserDao, never()).update(any(SysUser.class));
                verify(sysUserRoleDao, never()).delete(any(QueryExample.class));
                verify(sysUserRoleDao, never()).insert(anyListOf(SysUserRole.class));

        }

        @Test
        public void testUpdateUser() {
                when(sysUserDao.update(any(SysUser.class))).thenReturn(1L);
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                sysUser.setUserId(1);
                sysUser.setUsername("z123");
                sysUser.setPassword("2322434");
                sysUser.setFullName("#$#$#$#$");
                sysUser.setEmail("$#$#@ddd");
                sysUserService.update(sysUser);
                verify(sysUserDao, only()).update(any(SysUser.class));
                verify(sysUserRoleDao, only()).delete(any(QueryExample.class));
                verify(sysUserRoleDao, never()).insert(anyListOf(SysUserRole.class));
        }

        @Test
        public void testUpdateUserOneRole() {
                PowerMockito.mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                when(sysUserDao.update(any(SysUser.class))).thenReturn(1L);
                when(sysUserRoleDao.delete(any(QueryExample.class))).thenReturn(1l);
                when(sysUserRoleDao.insert(anyListOf(SysUserRole.class)));
                SysUserRoleCommand sysUser = new SysUserRoleCommand();
                sysUser.setUserId(1);
                sysUser.setUsername("z123");
//                sysUser.setPassword("2322434");
                sysUser.setFullName("#$#$#$#$");
                sysUser.setEmail("$#$#@ddd");
                sysUser.setRoleIds("1");
                sysUserService.update(sysUser);
                InOrder inOrder = inOrder(sysUserDao, sysUserRoleDao);
                inOrder.verify(sysUserDao, times(1)).update(any(SysUser.class));
                inOrder.verify(sysUserRoleDao, times(1)).delete(any(QueryExample.class));
                inOrder.verify(sysUserRoleDao, times(1)).insert(anyListOf(SysUserRole.class));
                PowerMockito.verifyStatic(times(1));
                IDUtils.getNextId();
        }
}
