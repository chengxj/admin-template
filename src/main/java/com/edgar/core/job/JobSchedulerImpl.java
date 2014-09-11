package com.edgar.core.job;

import com.edgar.core.util.ExceptionFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 作业的调度类
 *
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class JobSchedulerImpl implements JobScheduler {
    private final Logger logger = LoggerFactory.getLogger(JobSchedulerImpl.class);

    @Override
    public void addAndStartJob(JobAdpater job) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            addJob(job, scheduler);
            scheduler.start();
            logger.info("add and start job:{}", job.getClazzName());
        } catch (SchedulerException e) {
            logger.error("add job failed:{}", job.getClazzName(), e);
            throw ExceptionFactory.job();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addJob(JobAdpater job, Scheduler scheduler) {
        Preconditions.checkNotNull(job);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(job.getClazzName()));
        try {
            Class clazz = this.getClass().getClassLoader()
                    .loadClass(job.getClazzName());
            JobDetail jobDetail = JobBuilder.newJob(clazz)
                    .withIdentity(job.getJobName()).build();
            CronTrigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(job.getJobName() + "Trigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule(job
                            .getCron())).build();
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("add job:{}", job.getClazzName());
        } catch (ClassNotFoundException e) {
            logger.error("{} not found", job.getClazzName(), e);
            throw ExceptionFactory.job();
        } catch (SchedulerException e) {
            logger.error("add job failed:{}", job.getClazzName(), e);
            throw ExceptionFactory.job();
        }
    }

    @Override
    public void deleteJob(JobAdpater job) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = new JobKey(job.getJobName());
            scheduler.deleteJob(jobKey);
            logger.info("delete job:{}", job.getClazzName());
        } catch (SchedulerException e) {
            logger.error("delete job failed:{}", job.getClazzName(), e);
            throw ExceptionFactory.job();
        }
    }

    @Override
    public void updateJob(JobAdpater job) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = new JobKey(job.getJobName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                addAndStartJob(job);
            } else {
                TriggerKey triggerKey = new TriggerKey(job.getJobName() + "Trigger");
                CronTrigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(job.getJobName() + "Trigger")
                        .withSchedule(CronScheduleBuilder.cronSchedule(job
                                .getCron())).build();
                scheduler.rescheduleJob(triggerKey, trigger);
                logger.error("update job:{}", job.getClazzName());
            }

        } catch (SchedulerException e) {
            logger.error("update job failed:{}", job.getClazzName(), e);
            throw ExceptionFactory.job();
        }
    }

}