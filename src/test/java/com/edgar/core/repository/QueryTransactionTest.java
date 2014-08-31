package com.edgar.core.repository;

import com.edgar.core.repository.transaction.BatchInsertTransaction;
import com.edgar.core.repository.transaction.QueryTransaction;
import com.edgar.core.repository.transaction.Transaction;
import com.edgar.core.repository.transaction.TransactionBuilder;
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
import org.springframework.jdbc.core.SingleColumnRowMapper;
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
public class QueryTransactionTest {

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
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());

        example.limit(10);
        builder.example(example);
        transaction = builder.build();
        testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
        example.equalsTo("testCode", "0001");
        builder.example(example);
        transaction = builder.build();
        testTables = transaction.execute();
        Assert.assertEquals(1, testTables.size());

        example.clear();
        example.limit(10);
        example.greaterThan("test_code", "0001");
        example.asc("sorted");
        example.desc("testCode");
        builder.example(example);
        transaction = builder.build();
        testTables = transaction.execute();
        Assert.assertEquals(8, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryReturnField() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.addField("testCode");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
        for (TestTable TestTable : testTables) {
            Assert.assertNotNull(TestTable.getTestCode());
            Assert.assertNull(TestTable.getDictName());
        }
        example.clear();
        example.limit(10);
        example.addField("testCodeew");
        builder.example(example);
        transaction = builder.build();
        testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
        for (TestTable TestTable : testTables) {
            Assert.assertNotNull(TestTable.getTestCode());
            Assert.assertNotNull(TestTable.getDictName());
        }
    }

    @Transactional
    @Test
    public void testQueryNotEquals() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notEqualsTo("testCode", "0001");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(9, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryGreatThan() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.greaterThan("testCode", "0001");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(8, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryGreatThanAndEqulas() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.greaterThanOrEqualTo("testCode", "0001");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(9, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryLessThan() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.lessThan("testCode", "0004");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(4, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryLessThanAndEqulas() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.lessThanOrEqualTo("testCode", "0004");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(5, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryLike() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.like("testCode", "000%");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotLike() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notLike("testCode", "0000001%");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryBeginWith() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.beginWtih("testCode", "000");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotBeginWith() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notBeginWith("testCode", "000");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(0, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryContain() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.contain("testCode", "00");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotContain() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notContain("testCode", "00");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(0, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryIsNull() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.isNull("testCode");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(0, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryIsNotNull() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.isNotNull("testCode");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryBetween() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.between("testCode", "0001", "0002");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(2, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotBetween() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notBetween("testCode", "0001", "0002");
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(8, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryIn() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        List<Object> in = new ArrayList<Object>();
        in.add("0001");
        in.add("0002");
        example.in("testCode", in);
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(2, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotIn() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        List<Object> in = new ArrayList<Object>();
        in.add("0001");
        in.add("0002");
        example.notIn("testCode", in);
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(8, testTables.size());
    }

    @Transactional
    @Test
    public void testQuerySingleColumn() {
        QueryExample example = QueryExample.newInstance();
        example.equalsTo("testCode", "0001");
        example.addField("testCode");

        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).rowMapper(new SingleColumnRowMapper(String.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();

        List<String> testTables = transaction.execute();
        Assert.assertEquals(1, testTables.size());
        example.addField("dictName");
        builder.example(example);
        transaction = builder.build();
        try {
            testTables = transaction.execute();;
            Assert.assertEquals(1, testTables.size());
        } catch (Exception e) {

        }
    }
}
