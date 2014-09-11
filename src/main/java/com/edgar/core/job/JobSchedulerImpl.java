package com.edgar.core.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.edgar.core.util.ExceptionFactory;

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

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public void addJob(JobAdpater job, Scheduler scheduler) {
                Assert.notNull(job);
                Assert.hasText(job.getClazzName());
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