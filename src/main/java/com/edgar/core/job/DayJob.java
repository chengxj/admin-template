package com.edgar.core.job;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DayJob implements Job {

        private static final Logger LOGGER = LoggerFactory.getLogger(DayJob.class);

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
                DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                LOGGER.info(Thread.currentThread().getId() + ":" + new DateTime().toString(format));
        }

}
