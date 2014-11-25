package com.edgar.dao;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.dml.SQLInsertClause;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao.xml"})
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class DaoManagerTest {
    @Autowired
    private DataSource dataSource;

    @Transactional
    @Test
    public void testInsert() {
        DaoManager daoManager = new DaoManager(dataSource);
        SysUser sysUser = new SysUser();
        sysUser.setUsername("test");
        sysUser.setPassword("password");
        sysUser.setUserId((new Random()).nextInt(10000));
        sysUser.setFullName("Test User");
        int result = daoManager.insert(sysUser);
        Assert.assertEquals(1, result);
    }

    @Transactional
    @Test
    public void testInsertThenGetKey() {
        DaoManager daoManager = new DaoManager(dataSource);
        TestTable testTable = new TestTable();
        testTable.setName("testTable");
        Number result = daoManager.insertAndGetKey(testTable, "id");
        System.out.println(result);
    }

    @Transactional
    @Test
    public void testDelete() {
        DaoManager daoManager = new DaoManager(dataSource);
        TestTable testTable = new TestTable();
        testTable.setName("testTable");
        Number key = daoManager.insertAndGetKey(testTable, "id");
        int result = daoManager.delete("TestTable", "id", key);
        Assert.assertEquals(1, result);
    }

//    @Transactional
    @Test
    public void testUpdateWithNull() {
        DaoManager daoManager = new DaoManager(dataSource);
        TestTable testTable = new TestTable();
        testTable.setName("testTable");
        Number key = daoManager.insertAndGetKey(testTable, "id");
        System.out.println(key);
        testTable.setId(key.intValue());
        testTable.setName(null);
        int result = daoManager.update(testTable, "id", key, true);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testUpdateWithoutNull() {
        DaoManager daoManager = new DaoManager(dataSource);
        TestTable testTable = new TestTable();
        testTable.setName("testTable");
        Number key = daoManager.insertAndGetKey(testTable, "id");
        System.out.println(key);
        testTable.setId(key.intValue());
        testTable.setName(null);
        int result = daoManager.update(testTable, "id", key, false);
        Assert.assertEquals(1, result);
    }
}
