package com.edgar.module.sys.service;

import com.edgar.core.exception.BusinessCode;
import com.edgar.core.exception.SystemException;
import com.edgar.core.repository.BaseDao;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.SysDict;
import com.edgar.module.sys.service.impl.SysDictServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class SysDictServiceTest {

    @Mock
    private BaseDao<String, SysDict> sysDictDao;

    private SysDictServiceImpl sysDictService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sysDictService = new SysDictServiceImpl();
        sysDictService.setSysDictDao(sysDictDao);
    }

    @Test
    public void testGet() {
        final SysDict sysDict = new SysDict();
        final String dictCode = "1";
        when(sysDictDao.get(anyString())).thenReturn(sysDict);
        SysDict result = sysDictService.get(dictCode);
        verify(sysDictDao).get(anyString());
        Assert.assertEquals(sysDict, result);
    }

    @Test(expected = SystemException.class)
    public void testSaveCodeTooLang() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("012345678901234567890123456789123");
        when(sysDictDao.insert(any(SysDict.class)));
        try {
            sysDictService.save(sysDict);
        } finally {
            verify(sysDictDao, never()).insert(any(SysDict.class));
        }
    }

    @Test(expected = SystemException.class)
    public void testSaveNullName() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("1");
        when(sysDictDao.insert(any(SysDict.class)));
        try {
            sysDictService.save(sysDict);
        } finally {
            verify(sysDictDao, never()).insert(any(SysDict.class));
        }
    }

    @Test(expected = SystemException.class)
    public void testSaveNameTooLong() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("1");
        sysDict.setDictName("012345678901234567890123456789123");
        when(sysDictDao.insert(any(SysDict.class)));
        try {
            sysDictService.save(sysDict);
        } finally {
            verify(sysDictDao, never()).insert(any(SysDict.class));
        }
    }

    @Test(expected = SystemException.class)
    public void testSaveCodeInvalid() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("1!@#$");
        sysDict.setDictName("012345678901234");
        when(sysDictDao.insert(any(SysDict.class)));
        try {
            sysDictService.save(sysDict);
        } finally {
            verify(sysDictDao, never()).insert(any(SysDict.class));
        }
    }

    @Test
    public void testSaveSuccess() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("1");
        sysDict.setDictName("1");
        when(sysDictDao.insert(any(SysDict.class)));
        sysDictService.save(sysDict);
        verify(sysDictDao).insert(any(SysDict.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNull() {
        final SysDict sysDict = null;
        when(sysDictDao.insert(any(SysDict.class)));
        sysDictService.save(sysDict);
        verify(sysDictDao, never()).get(anyString());
        verify(sysDictDao, never()).insert(sysDict);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveDictCodeNull() {
        final SysDict sysDict = new SysDict();
        when(sysDictDao.insert(any(SysDict.class)));
        sysDictService.save(sysDict);
        verify(sysDictDao, never()).get(anyString());
        verify(sysDictDao, never()).insert(sysDict);
    }

    @Test(expected = SystemException.class)
    public void testSaveDuplicate() {
        final SysDict sysDict = new SysDict();
        final String dictCode = "1";
        sysDict.setDictCode(dictCode);
        final SysDict getDict = new SysDict();
        getDict.setDictCode(dictCode);
        when(sysDictDao.insert(any(SysDict.class))).thenThrow(
                new DuplicateKeyException("duplicate"));
        sysDictService.save(sysDict);
        verify(sysDictDao).insert(sysDict);
        sysDictService.save(sysDict);
    }

    @Test
    public void testSaveChild() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("12");
        sysDict.setParentCode("1");
        sysDict.setDictName("1");
        when(sysDictDao.get("1")).thenReturn(new SysDict());
        when(sysDictDao.insert(any(SysDict.class)));
        sysDictService.save(sysDict);
        verify(sysDictDao, times(1)).get(anyString());
        verify(sysDictDao, times(1)).insert(sysDict);
    }

    @Test(expected = SystemException.class)
    public void testSaveChildNoParent() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("12");
        sysDict.setParentCode("1");
        when(sysDictDao.get("1")).thenReturn(null);
        when(sysDictDao.insert(any(SysDict.class)));
        sysDictService.save(sysDict);
        verify(sysDictDao, times(1)).get(anyString());
        verify(sysDictDao, never()).insert(sysDict);
    }

    @Test
    public void testUpdateSuccess() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("1");
        sysDict.setDictName("d");
        sysDict.setParentCode("1");
        when(sysDictDao.update(any(SysDict.class)));
        sysDictService.update(sysDict);
        verify(sysDictDao, times(1)).update(sysDict);
    }

    @Test(expected = SystemException.class)
    public void testUpdateExpired() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("1");
        sysDict.setDictName("d");
        final SystemException e = new SystemException(BusinessCode.EXPIRED, "数据过期");
        when(sysDictDao.update(any(SysDict.class))).thenThrow(e);
        sysDictService.update(sysDict);
        verify(sysDictDao, times(1)).update(sysDict);
    }

    @Test
    public void testDeleteByPkSuccess() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("1");
        sysDict.setDictName("d");
        sysDict.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        when(sysDictDao.deleteByPkAndVersion(anyString(), anyLong())).thenReturn(1l);
        when(sysDictDao.delete(any(QueryExample.class))).thenReturn(1l);
        sysDictService.deleteWithLock(sysDict.getDictCode(), sysDict
                .getUpdatedTime().getTime());
        verify(sysDictDao, times(1)).deleteByPkAndVersion(anyString(), anyLong());
        verify(sysDictDao, times(1)).delete(any(QueryExample.class));
    }

    @Test(expected = SystemException.class)
    public void testDeleteByPkExpired() {
        final SysDict sysDict = new SysDict();
        sysDict.setDictCode("1");
        sysDict.setDictName("d");
        sysDict.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        final SystemException e = new SystemException(BusinessCode.EXPIRED, "数据过期");
        when(sysDictDao.deleteByPkAndVersion(anyString(), anyLong())).thenThrow(e);
        when(sysDictDao.delete(any(QueryExample.class))).thenReturn(1l);
        sysDictService.deleteWithLock(sysDict.getDictCode(), sysDict
                .getUpdatedTime().getTime());
        verify(sysDictDao, times(1)).deleteByPkAndVersion(anyString(), anyLong());
        verify(sysDictDao, times(1)).delete(any(QueryExample.class));
    }

    @Test
    public void testPaination() {
        final List<SysDict> sysDicts = new ArrayList<SysDict>();
        sysDicts.add(new SysDict());
        sysDicts.add(new SysDict());
        final Pagination<SysDict> pagination = Pagination.newInstance(1, 10, 2, sysDicts);
        final QueryExample example = QueryExample.newInstance();
        when(sysDictDao.pagination(same(example), anyInt(), anyInt())).thenReturn(
                pagination);
        Pagination<SysDict> result = sysDictService.pagination(example, 1, 10);
        Assert.assertEquals(pagination, result);
        verify(sysDictDao, times(1)).pagination(same(example), anyInt(), anyInt());
    }

    @Test
    public void testQuery() {
        final List<SysDict> sysDicts = new ArrayList<SysDict>();
        sysDicts.add(new SysDict());
        sysDicts.add(new SysDict());
        final QueryExample example = QueryExample.newInstance();
        when(sysDictDao.query(same(example))).thenReturn(sysDicts);
        List<SysDict> result = sysDictService.query(example);
        Assert.assertEquals(sysDicts, result);
        verify(sysDictDao, times(1)).query(same(example));
    }

    @Test
    public void testCheckExistDictCode() {
        final SysDict sysDict = new SysDict();
        final String dictCode = "1";
        when(sysDictDao.get(same(dictCode))).thenReturn(sysDict);
        boolean result = sysDictService.checkDictCode(dictCode);
        Assert.assertFalse(result);

    }

    @Test
    public void testCheckNotExistDictCode() {
        final String dictCode = "1";
        when(sysDictDao.get(same(dictCode))).thenReturn(null);
        boolean result = sysDictService.checkDictCode(dictCode);
        Assert.assertTrue(result);
    }
}
