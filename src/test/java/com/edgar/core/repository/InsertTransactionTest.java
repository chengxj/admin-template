package com.edgar.core.repository;

import com.edgar.core.repository.transaction.*;
import com.edgar.module.sys.repository.domain.TestTable;
import com.edgar.module.sys.repository.querydsl.QTestTable;
import com.mysema.query.sql.Configuration;
import org.junit.Assert;
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

/**
 * Created by Administrator on 2014/8/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class InsertTransactionTest {

    @Autowired
    private DataSource dataSource;

    private Configuration configuration = Constants.CONFIGURATION;

    @Transactional
    @Test
    public void testInsert() {
        QueryExample example = QueryExample.newInstance();
        example = QueryExample.newInstance();
        TransactionBuilder builder = new QueryTransaction.Builder<TestTable>().rowMapper(BeanPropertyRowMapper.newInstance(TestTable.class)).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable).example(example);
        Transaction transaction = builder.build();
        List<TestTable> testTables = transaction.execute();
        Assert.assertEquals(0, testTables.size());

        testTables = new ArrayList<TestTable>();
        for (int i = 0; i < 10; i++) {
            TestTable testTable = new TestTable();
            testTable.setTestCode("000" + i);
            testTable.setDictName("000" + i);
            testTable.setParentCode("-1");
            testTable.setSorted(9999);
            TransactionBuilder insertbuilder = new InsertTransaction.Builder<TestTable>().domain(testTable).dataSource(dataSource).configuration(configuration).pathBase(QTestTable.testTable);
            transaction = insertbuilder.build();
            Long result = transaction.execute();
            Assert.assertEquals(1l, result, 0);
        }

        transaction = builder.build();
        testTables = transaction.execute();
        Assert.assertEquals(10, testTables.size());
    }
}
