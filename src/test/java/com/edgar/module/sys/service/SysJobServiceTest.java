package com.edgar.module.sys.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.quartz.Scheduler;

import com.edgar.core.exception.SystemException;
import com.edgar.core.job.JobAdpater;
import com.edgar.core.job.JobScheduler;
import com.edgar.core.repository.Criteria;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.repository.SqlOperator;
import com.edgar.module.sys.repository.domain.SysJob;
import com.edgar.module.sys.service.SysJobServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ IDUtils.class, QueryExample.class })
public class SysJobServiceTest {
        @Mock
        private CrudRepository<Integer, SysJob> sysJobDao;

        @Mock
        private JobScheduler jobScheduler;

        private SysJobServiceImpl sysJobService;

        @Before
        public void setUp() {
                MockitoAnnotations.initMocks(this);
                sysJobService = new SysJobServiceImpl();
                sysJobService.setSysJobDao(sysJobDao);
                sysJobService.setJobScheduler(jobScheduler);
        }

        @Test
        public void testGet() {
                SysJob sysJob = new SysJob();
                sysJob.setClazzName("com.edgar.job.DayJob");
                sysJob.setCron("0/1 * * * * ? *");
                sysJob.setEnabled(true);
                sysJob.setJobName("测试作业");
                when(sysJobDao.get(anyInt())).thenReturn(sysJob);
                SysJob result = sysJobService.get(1);
                Assert.assertEquals(ToStringBuilder.reflectionToString(sysJob,
                                ToStringStyle.SHORT_PREFIX_STYLE), ToStringBuilder
                                .reflectionToString(result, ToStringStyle.SHORT_PREFIX_STYLE));
                verify(sysJobDao, only()).get(anyInt());
        }

        @Test
        public void testDelete() {
                SysJob sysJob = new SysJob();
                sysJob.setClazzName("com.edgar.job.DayJob");
                sysJob.setCron("0/1 * * * * ? *");
                sysJob.setEnabled(true);
                sysJob.setJobName("测试作业");
                when(sysJobDao.get(anyInt())).thenReturn(sysJob);
                when(sysJobDao.deleteByPkAndVersion(anyInt(), anyLong())).thenReturn(1l);
                doAnswer(new Answer<Object>() {

                        @Override
                        public Object answer(InvocationOnMock invocation) throws Throwable {
                                Object[] args = invocation.getArguments();
                                return "called with arguments: " + args;
                        }
                }).when(jobScheduler).deleteJob(any(JobAdpater.class));

                long result = sysJobService.deleteWithLock(1, 1L);
                Assert.assertEquals(result, 1);

                InOrder inOrder = inOrder(sysJobDao);
                inOrder.verify(sysJobDao, times(1)).get(anyInt());
                inOrder.verify(sysJobDao, times(1)).deleteByPkAndVersion(anyInt(), anyLong());
                verify(jobScheduler, only()).deleteJob(any(JobAdpater.class));
        }

        @Test
        public void testPaination() {
                final List<SysJob> sysJobs = new ArrayList<SysJob>();
                sysJobs.add(new SysJob());
                sysJobs.add(new SysJob());
                final Pagination<SysJob> pagination = Pagination.newInstance(1, 10, 2, sysJobs);
                final QueryExample example = QueryExample.newInstance();
                when(sysJobDao.pagination(same(example), anyInt(), anyInt()))
                                .thenReturn(pagination);
                Pagination<SysJob> result = sysJobService.pagination(example, 1, 10);
                Assert.assertEquals(pagination, result);
                Assert.assertTrue(example.getCriterias().isEmpty());
                verify(sysJobDao, times(1)).pagination(same(example), anyInt(), anyInt());
        }

        @Test
        public void testQuery() {
                final List<SysJob> sysJobs = new ArrayList<SysJob>();
                sysJobs.add(new SysJob());
                sysJobs.add(new SysJob());
                final QueryExample example = QueryExample.newInstance();
                when(sysJobDao.query(same(example))).thenReturn(sysJobs);
                List<SysJob> result = sysJobService.query(example);
                Assert.assertEquals(sysJobs, result);
                Assert.assertTrue(example.getCriterias().isEmpty());
                verify(sysJobDao, times(1)).query(any(QueryExample.class));
        }

        @Test
        public void testFindEnabledJob() {
                final List<SysJob> sysJobs = new ArrayList<SysJob>();
                sysJobs.add(new SysJob());
                sysJobs.add(new SysJob());
                final QueryExample example = QueryExample.newInstance();
                when(sysJobDao.query(same(example))).thenReturn(sysJobs);
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);

                List<SysJob> result = sysJobService.findEnabledJob();
                Assert.assertEquals(sysJobs, result);
                Assert.assertTrue(example.containCriteria(new Criteria("enabled",
                                SqlOperator.EQUALS_TO, 1)));
                verify(sysJobDao, times(1)).query(any(QueryExample.class));
        }

        @Test
        public void testCheckClazzNameOk() {
                final List<SysJob> sysJobs = new ArrayList<SysJob>();
                sysJobs.add(new SysJob());
                final QueryExample example = QueryExample.newInstance();
                when(sysJobDao.query(any(QueryExample.class))).thenReturn(sysJobs);
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);

                boolean result = sysJobService.checkClazzName("com.edgar.job.DayJob");
                Assert.assertFalse(result);
                Assert.assertTrue(example.containCriteria(new Criteria("clazzName",
                                SqlOperator.EQUALS_TO, "com.edgar.job.DayJob")));
                verify(sysJobDao, times(1)).query(any(QueryExample.class));
        }

        @Test
        public void testCheckClazzNameError() {
                final List<SysJob> sysJobs = new ArrayList<SysJob>();
                final QueryExample example = QueryExample.newInstance();
                when(sysJobDao.query(any(QueryExample.class))).thenReturn(sysJobs);
                mockStatic(QueryExample.class);
                when(QueryExample.newInstance()).thenReturn(example);

                boolean result = sysJobService.checkClazzName("com.edgar.job.DayJob");
                Assert.assertTrue(result);
                Assert.assertTrue(example.containCriteria(new Criteria("clazzName",
                                SqlOperator.EQUALS_TO, "com.edgar.job.DayJob")));
                verify(sysJobDao, times(1)).query(any(QueryExample.class));
        }

        @Test
        public void testSaveNull() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysJob sysJob = new SysJob();
                try {
                        sysJobService.save(sysJob);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("jobName"));
                        Assert.assertTrue(map.containsKey("clazzName"));
                        Assert.assertTrue(map.containsKey("cron"));
                        Assert.assertFalse(map.containsKey("jobId"));
                }
                verify(sysJobDao, never()).insert(same(sysJob));
                verifyStatic(never());

        }

        @Test
        public void testSaveLong() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysJob sysJob = new SysJob();
                sysJob.setJobName("012345678901234567890123456789123");
                sysJob.setCron("012345678901234567890123456789123");
                sysJob.setClazzName("01234567890123456789012345678901234567890123456789012345678901234");
                try {
                        sysJobService.save(sysJob);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("jobName"));
                        Assert.assertTrue(map.containsKey("clazzName"));
                        Assert.assertTrue(map.containsKey("cron"));
                        Assert.assertFalse(map.containsKey("jobId"));
                }
                verify(sysJobDao, never()).insert(same(sysJob));
                verifyStatic(never());

        }

        @Test
        public void testSavePattern() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysJob sysJob = new SysJob();
                sysJob.setJobName("123");
                sysJob.setClazzName("com2");
                sysJob.setCron("1");
                try {
                        sysJobService.save(sysJob);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertFalse(map.containsKey("jobName"));
                        Assert.assertTrue(map.containsKey("clazzName"));
                        Assert.assertFalse(map.containsKey("cron"));
                        Assert.assertFalse(map.containsKey("jobId"));
                }
                verify(sysJobDao, never()).insert(same(sysJob));
                verifyStatic(never());

        }

        @Test
        public void testSaveEnabled() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysJob sysJob = new SysJob();
                sysJob.setClazzName("com.edgar.job.DayJob");
                sysJob.setCron("0/1 * * * * ? *");
                sysJob.setEnabled(true);
                sysJob.setJobName("测试作业");
                sysJobService.save(sysJob);
                verify(sysJobDao, only()).insert(same(sysJob));
                verifyStatic(only());
                IDUtils.getNextId();
                verify(jobScheduler, only()).addAndStartJob(any(JobAdpater.class));
                verify(jobScheduler, never()).addJob(any(JobAdpater.class), any(Scheduler.class));

        }

        @Test
        public void testSaveDisabled() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysJob sysJob = new SysJob();
                sysJob.setClazzName("com.edgar.job.DayJob");
                sysJob.setCron("0/1 * * * * ? *");
                sysJob.setEnabled(false);
                sysJob.setJobName("测试作业");
                sysJobService.save(sysJob);
                verify(sysJobDao, only()).insert(same(sysJob));
                verifyStatic(only());
                IDUtils.getNextId();
                verify(jobScheduler, never()).addAndStartJob(any(JobAdpater.class));
                verify(jobScheduler, never()).addJob(any(JobAdpater.class), any(Scheduler.class));

        }

        @Test
        public void testUpdateNull() {
                SysJob sysJob = new SysJob();
                try {
                        sysJobService.update(sysJob);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertFalse(map.containsKey("jobName"));
                        Assert.assertFalse(map.containsKey("clazzName"));
                        Assert.assertFalse(map.containsKey("cron"));
                        Assert.assertTrue(map.containsKey("jobId"));
                }
                verify(sysJobDao, never()).update(same(sysJob));

        }

        @Test
        public void testUpdateLong() {
                SysJob sysJob = new SysJob();
                sysJob.setJobId(1);
                sysJob.setJobName("012345678901234567890123456789123");
                sysJob.setCron("012345678901234567890123456789123");
                sysJob.setClazzName("1");
                try {
                        sysJobService.update(sysJob);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertTrue(map.containsKey("jobName"));
                        Assert.assertTrue(map.containsKey("clazzName"));
                        Assert.assertTrue(map.containsKey("cron"));
                        Assert.assertFalse(map.containsKey("jobId"));
                }
                verify(sysJobDao, never()).update(same(sysJob));

        }

        @Test
        public void testUpdateEnabled() {
                mockStatic(IDUtils.class);
                when(IDUtils.getNextId()).thenReturn(1);
                SysJob sysJob = new SysJob();
                sysJob.setJobId(1);
                sysJob.setEnabled(true);
                sysJob.setJobName("测试作业");
                try {
                        sysJobService.update(sysJob);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertFalse(map.containsKey("jobName"));
                        Assert.assertFalse(map.containsKey("clazzName"));
                        Assert.assertFalse(map.containsKey("cron"));
                        Assert.assertFalse(map.containsKey("jobId"));
                }
                verify(sysJobDao, only()).update(same(sysJob));
                verify(jobScheduler, only()).updateJob(any(JobAdpater.class));
        }

        @Test
        public void testUpdateDisabled() {
                SysJob sysJob = new SysJob();
                sysJob.setJobId(1);
                sysJob.setCron("0/1 * * * * ? *");
                sysJob.setEnabled(false);
                sysJob.setJobName("测试作业");
                try {
                        sysJobService.update(sysJob);
                } catch (SystemException e) {
                        Map<String, Object> map = e.getPropertyMap();
                        Assert.assertFalse(map.containsKey("jobName"));
                        Assert.assertFalse(map.containsKey("clazzName"));
                        Assert.assertFalse(map.containsKey("cron"));
                        Assert.assertFalse(map.containsKey("jobId"));
                }
                verify(sysJobDao, only()).update(same(sysJob));
                verify(jobScheduler, never()).updateJob(any(JobAdpater.class));
                verify(jobScheduler, only()).deleteJob(any(JobAdpater.class));

        }
}
