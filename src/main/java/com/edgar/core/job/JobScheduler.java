package com.edgar.core.job;

import org.quartz.Scheduler;

/**
 * 作业调度的接口
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public interface JobScheduler {

        /**
         * 新增并启动一个作业
         * 
         * @param job
         *                作业对象
         */
        void addAndStartJob(JobAdpater job);

        /**
         * 删除一个作业
         * 
         * @param job
         *                作业对象
         */
        void deleteJob(JobAdpater job);

        /**
         * 新增一个作业
         * 
         * @param job
         *                作业对象
         * @param scheduler
         *                调度类
         */
        void addJob(JobAdpater job, Scheduler scheduler);

        /**
         * 更新一个作业
         * 
         * @param job
         */
        void updateJob(JobAdpater job);

}