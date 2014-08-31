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

/**
 * Created by Administrator on 2014/8/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class DeleteByExampleTransactionTest {
    @Autowired
    private TestTableDao testTableDao;

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
    public void testDeleteByExample() {
        QueryExample example = QueryExample.newInstance();
        example.limit(1);
        List<TestTable> testTables = testTableDao.query(example);
        TestTable testTable = testTables.get(0);

        example.clear();
        example.equalsTo("testCode", testTable.getTestCode());
        example.equalsTo("updatedTime", testTable.getUpdatedTime());

        TransactionBuilder builder = new DeleteByExampleTransaction.Builder().dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        Long result = transaction.execute();
        Assert.assertEquals(1l, result, 0);
        testTable = testTableDao.get(testTable.getTestCode());
        Assert.assertNull(testTable);
    }

    @Transactional
    @Test
    public void testDeleteByExampleNotEquals() {

        QueryExample example = QueryExample.newInstance();
        example.notEqualsTo("testCode", "0001");
        TransactionBuilder builder = new DeleteByExampleTransaction.Builder().dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        Long result = transaction.execute();
        Assert.assertEquals(9l, result, 0);
    }
}
