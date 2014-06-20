package com.edgar.core.repository;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.edgar.core.repository.IDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
                TransactionalTestExecutionListener.class })
public class IDUtilsTest {

        @Test
        public void testGetId() throws InterruptedException {
                ExecutorService executorService = Executors.newCachedThreadPool();
                int num = 200;
                final CountDownLatch latch = new CountDownLatch(num);
                final CyclicBarrier barrier = new CyclicBarrier(num);
                for (int i = 0; i < num; i++) {
                        final int v = i;
                        executorService.execute(new Runnable() {

                                @Override
                                public void run() {
                                        Thread.currentThread().setName("t" + v);
                                        try {
                                                barrier.await();
                                        } catch (InterruptedException e) {
                                                e.printStackTrace();
                                        } catch (BrokenBarrierException e) {
                                                e.printStackTrace();
                                        }
                                        IDUtils.getNextId();
                                        latch.countDown();
                                }
                        });
                }
                latch.await();
        }
}
