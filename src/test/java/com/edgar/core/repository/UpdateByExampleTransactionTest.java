package com.edgar.core.repository;

import com.edgar.core.exception.SystemException;
import com.edgar.module.sys.repository.domain.Test2Table;
import com.edgar.module.sys.repository.domain.TestTable;
import com.edgar.module.sys.repository.querydsl.QTestTable;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLListener;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class UpdateByExampleTransactionTest {

    @Autowired
    private TestTableDao testTableDao;

    @Autowired
    private CrudRepository<Test2TablePk, Test2Table> test2TableDao;

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
        testTableDao.insert(testTables);

        List<Test2Table> test2Tables = new ArrayList<Test2Table>();
        for (int i = 0; i < 10; i++) {
            Test2Table testTable = new Test2Table();
            testTable.setTestCode2("000" + i);
            testTable.setTestId(testTable.getTestCode2());
            testTable.setDictName("000" + i);
            testTable.setParentCode("-1");
            testTable.setSorted(9999);
            test2Tables.add(testTable);
        }
        test2TableDao.insert(test2Tables);

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
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(10, testTables.size());
        example.limit(10);
        testTables = testTableDao.query(example);
        Assert.assertEquals(10, testTables.size());
        example.equalsTo("testCode", "0001");
        testTables = testTableDao.query(example);
        Assert.assertEquals(1, testTables.size());
        example.clear();
        example.limit(10);
        example.greaterThan("test_code", "0001");
        example.asc("sorted");
        example.desc("testCode");
        testTables = testTableDao.query(example);
        Assert.assertEquals(8, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryReturnField() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.addField("testCode");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(10, testTables.size());
        for (TestTable TestTable : testTables) {
            Assert.assertNotNull(TestTable.getTestCode());
            Assert.assertNull(TestTable.getDictName());
        }
        example.clear();
        example.limit(10);
        example.addField("testCodeew");
        testTables = testTableDao.query(example);
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
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(9, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryGreatThan() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.greaterThan("testCode", "0001");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(8, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryGreatThanAndEqulas() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.greaterThanOrEqualTo("testCode", "0001");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(9, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryLessThan() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.lessThan("testCode", "0004");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(4, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryLessThanAndEqulas() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.lessThanOrEqualTo("testCode", "0004");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(5, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryLike() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.like("testCode", "000%");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotLike() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notLike("testCode", "0000001%");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryBeginWith() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.beginWtih("testCode", "000");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotBeginWith() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notBeginWith("testCode", "000");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(0, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryContain() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.contain("testCode", "00");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotContain() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notContain("testCode", "00");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(0, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryIsNull() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.isNull("testCode");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(0, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryIsNotNull() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.isNotNull("testCode");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(10, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryBetween() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.between("testCode", "0001", "0002");
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(2, testTables.size());
    }

    @Transactional
    @Test
    public void testQueryNotBetween() {
        QueryExample example = QueryExample.newInstance();
        example.limit(10);
        example.notBetween("testCode", "0001", "0002");
        List<TestTable> testTables = testTableDao.query(example);
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
        List<TestTable> testTables = testTableDao.query(example);
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
        List<TestTable> testTables = testTableDao.query(example);
        Assert.assertEquals(8, testTables.size());
    }

    @Transactional
    @Test
    public void testQuerySingleColumn() {
        QueryExample example = QueryExample.newInstance();
        example.equalsTo("testCode", "0001");
        example.addField("testCode");
        List<String> testTables = testTableDao.querySingleColumn(example, String.class);
        Assert.assertEquals(1, testTables.size());
        example.addField("dictName");
        try {
            testTables = testTableDao.querySingleColumn(example, String.class);
            Assert.assertEquals(1, testTables.size());
        } catch (Exception e) {

        }
    }

    @Transactional
    @Test
    public void testUpdateByBeginWithExample() {
        QueryExample example = QueryExample.newInstance();
        example.beginWtih("testCode", "00");
        List<TestTable> testTables = testTableDao.query(example);
        TestTable TestTable = new TestTable();
        TestTable.setParentCode("1234");
        Assert.assertEquals(testTables.size(), testTableDao.update(TestTable, example), 0);
        testTables = testTableDao.query(example);
        for (TestTable dict : testTables) {
            Assert.assertEquals("1234", dict.getParentCode());
        }
    }

    @Transactional
    @Test
    public void testNoUpdateByExample() {
        QueryExample example = QueryExample.newInstance();
        example.beginWtih("testCode", "00000000000");
        TestTable TestTable = new TestTable();
        TestTable.setParentCode("1234");
        Assert.assertEquals(0, testTableDao.update(TestTable, example), 0);
    }

    @Transactional
    @Test
    public void testUpdateByPrimaryKey() {
        QueryExample example = QueryExample.newInstance();
        example.limit(1);
        List<TestTable> testTables = testTableDao.query(example);
        TestTable TestTable = testTables.get(0);
        TestTable.setParentCode("-1");
        Assert.assertEquals(1, testTableDao.update(TestTable));
        TestTable newTestTable = testTableDao.get(TestTable.getTestCode());
        Assert.assertNotNull(newTestTable.getTestCode());
        // Assert.assertNotEquals(newTestTable.getUpdatedTime(),
        // TestTable.getUpdatedTime());
    }

    @Transactional
    @Test(expected = SystemException.class)
    public void testUpdateByVersion() {
        QueryExample example = QueryExample.newInstance();
        example.limit(1);
        List<TestTable> testTables = testTableDao.query(example);
        TestTable TestTable = testTables.get(0);
        TestTable.setParentCode("-1");
        TestTable.setUpdatedTime(new Timestamp(1000));
        testTableDao.updateByVersion(TestTable);
    }

    @Transactional
    @Test
    public void testUpdateByExample() {
        QueryExample example = QueryExample.newInstance();
        example.limit(1);
        List<TestTable> testTables = testTableDao.query(example);
        TestTable testTable = testTables.get(0);

        TestTable domain = new TestTable();
        domain.setDictName("zzz");;
        domain.setTestCode("zzz");
        example.clear();
        example.equalsTo("testCode", testTable.getTestCode());
        example.equalsTo("updatedTime", testTable.getUpdatedTime());

        TransactionBuilder builder = new UpdateByExampleTransaction.UpdateByExampleTransactionBuilder<TestTable>().domain(domain).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        Long result = transaction.execute();
        Assert.assertEquals(1l, result, 0);
        testTable = testTableDao.get(testTable.getTestCode());
        Assert.assertNotNull(testTable.getParentCode());
        Assert.assertNotEquals(testTable.getTestCode(), domain.getTestCode());
        Assert.assertEquals(testTable.getDictName(), domain.getDictName());
    }

    @Test
    @Transactional
    public void testUpdateByExampleWithNullBindings() {
        QueryExample example = QueryExample.newInstance();
        example.limit(1);
        List<TestTable> testTables = testTableDao.query(example);
        TestTable testTable = testTables.get(0);

        TestTable domain = new TestTable();
        domain.setDictName("zzz");;
        domain.setTestCode("zzz");
        domain.setParentCode("-1");
        example.clear();
        example.equalsTo("testCode", testTable.getTestCode());
        example.equalsTo("updatedTime", testTable.getUpdatedTime());
        TransactionBuilder builder = new UpdateByExampleTransaction.UpdateByExampleTransactionBuilder<TestTable>().domain(domain).withNullBindings(true).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        Long result = transaction.execute();
        Assert.assertEquals(1l, result, 0);
        testTable = testTableDao.get(testTable.getTestCode());
        Assert.assertNotNull(testTable.getParentCode());
        Assert.assertNull(testTable.getSorted());
        Assert.assertNotEquals(testTable.getTestCode(), domain.getTestCode());
        Assert.assertEquals(testTable.getDictName(), domain.getDictName());
    }

}
