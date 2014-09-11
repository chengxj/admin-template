package com.edgar.core.job;

import com.edgar.core.exception.SystemException;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

public class JobSchedulerTest {

    private JobSchedulerImpl jobScheduler;

    @Before
    public void setUp() {
        jobScheduler = new JobSchedulerImpl();
    }

    @Test
    public void testAddAndStart() throws InterruptedException {
        JobAdpater job = new JobAdpater();
        job.setClazzName("com.edgar.core.job.DayJob");
        job.setCron("0/1 * * * * ? *");
        jobScheduler.addAndStartJob(job);

        TimeUnit.SECONDS.sleep(10);
        jobScheduler.deleteJob(job);
    }

    @Test(expected = SystemException.class)
    public void testAddAndStartClassNotFound() throws InterruptedException {
        JobAdpater job = new JobAdpater();
        job.setClazzName("com.edgar.core.job.DayJob2");
        job.setCron("0/1 * * * * ? *");
        jobScheduler.addAndStartJob(job);
    }

    @Test
    public void testUpdateJob() throws InterruptedException {
        JobAdpater job = new JobAdpater();
        job.setClazzName("com.edgar.core.job.DayJob");
        job.setCron("0/1 * * * * ? *");
        jobScheduler.addAndStartJob(job);
        TimeUnit.SECONDS.sleep(5);
        job.setCron("0/2 * * * * ? *");
        jobScheduler.updateJob(job);
        TimeUnit.SECONDS.sleep(5);
        jobScheduler.deleteJob(job);
    }

    @Test
    public void testUpdateJobForInsert() throws InterruptedException {
        JobAdpater job = new JobAdpater();
        job.setClazzName("com.edgar.core.job.DayJob");
        job.setCron("0/1 * * * * ? *");
        job.setCron("0/2 * * * * ? *");
        jobScheduler.updateJob(job);
        TimeUnit.SECONDS.sleep(5);
        jobScheduler.deleteJob(job);
    }

    @Test
    public void testAdd() throws InterruptedException, SchedulerException {
        JobAdpater job = new JobAdpater();
        job.setClazzName("com.edgar.core.job.DayJob");
        job.setCron("0/1 * * * * ? *");
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        jobScheduler.addJob(job, scheduler);
        TimeUnit.SECONDS.sleep(10);
        jobScheduler.deleteJob(job);
    }
}
