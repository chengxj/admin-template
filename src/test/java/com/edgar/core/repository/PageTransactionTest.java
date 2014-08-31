package com.edgar.core.repository;

import com.edgar.core.repository.transaction.*;
import com.edgar.module.sys.repository.domain.TestTable;
import com.edgar.module.sys.repository.querydsl.QTestTable;
import com.mysema.query.sql.Configuration;
import net.sf.ehcache.CacheManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class PageTransactionTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DataSource dataSource;

    private Configuration configuration = Constants.CONFIGURATION;

    @Before
    public void setUp() {
        List<TestTable> testTables = new ArrayList<TestTable>();
        for (int i = 0; i < 10; i++) {
            TestTable testTable = new TestTable();
            testTable.setTestCode("000" + i);
            testTable.setDictName("000" + i);
            testTable.setParentCode("-1");
            testTable.setSorted(9999);
            testTables.add(testTable);
        }
        TransactionBuilder builder = new BatchInsertTransaction.Builder<TestTable>().domains(testTables).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable);
        Transaction transaction = builder.build();
        transaction.execute();
    }

    @After
    public void tearDown() {
        cacheManager.removeAllCaches();
    }

    @Transactional
    @Test
    public void testQuery() {
        QueryExample example = QueryExample.newInstance();
        example = QueryExample.newInstance();
        TransactionBuilderTemplate builder = new CountTransaction.Builder().dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        Long count = transaction.execute();
        Assert.assertEquals(10, count, 0);

        example.limit(5);
        builder.example(example);
        transaction = builder.build();
        count = transaction.execute();
        Assert.assertEquals(5, count, 0);
        example.equalsTo("testCode", "0001");
        builder.example(example);
        transaction = builder.build();
        count = transaction.execute();
        Assert.assertEquals(1, count, 0);

        example.clear();
        example.limit(10);
        example.greaterThan("test_code", "0001");
        example.asc("sorted");
        example.desc("testCode");
        builder.example(example);
        transaction = builder.build();
        count = transaction.execute();
        Assert.assertEquals(8, count, 0);
    }

    @Transactional
    @Test
    public void testPage() {
        QueryExample example = QueryExample.newInstance();

        TransactionBuilder builder = new PageTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).page(1).pageSize(10).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        Pagination<TestTable> testTables = transaction.execute();

        Assert.assertEquals(1, testTables.getPage());
        Assert.assertEquals((testTables.getTotalRecords() + testTables.getPageSize() - 1)
                / testTables.getPageSize(), testTables.getTotalPages());
        Assert.assertEquals(1, testTables.getPrevPage());
        Assert.assertEquals(1, testTables.getNextPage());

        builder = new PageTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).page(2).pageSize(10).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        transaction = builder.build();
        testTables = transaction.execute();
        Assert.assertEquals(10, testTables.getRecords().size());
        Assert.assertEquals(1, testTables.getPage());
        Assert.assertEquals((testTables.getTotalRecords() + testTables.getPageSize() - 1)
                / testTables.getPageSize(), testTables.getTotalPages());
        Assert.assertEquals(1, testTables.getPrevPage());
        Assert.assertEquals(1, testTables.getNextPage());

        builder = new PageTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).page(3).pageSize(5).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        transaction = builder.build();
        testTables = transaction.execute();
        Assert.assertEquals(2, testTables.getPage());
        Assert.assertEquals((testTables.getTotalRecords() + testTables.getPageSize() - 1)
                / testTables.getPageSize(), testTables.getTotalPages());
        Assert.assertEquals(1, testTables.getPrevPage());
        Assert.assertEquals(2, testTables.getNextPage());
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testGreatThanMaxPage() {
        QueryExample example = QueryExample.newInstance();
        example.setMaxNumOfRecords(1000);
        TransactionBuilder builder = new PageTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).page(200).pageSize(10).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        Pagination<TestTable> testTables = transaction.execute();
    }
}
