package com.edgar.core.job;

import org.apache.commons.lang3.StringUtils;

public class JobAdpater {

        private String clazzName;

        private String cron;

        private String jobName;

        public String getClazzName() {
                return clazzName;
        }

        public void setClazzName(String clazzName) {
                this.clazzName = clazzName;
                this.jobName = StringUtils.substringAfterLast(clazzName, ".");
        }

        public String getCron() {
                return cron;
        }

        public void setCron(String cron) {
                this.cron = cron;
        }

        public String getJobName() {
                return jobName;
        }

}
