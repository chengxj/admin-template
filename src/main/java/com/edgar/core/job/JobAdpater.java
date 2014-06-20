package com.edgar.core.job;

import org.apache.commons.lang.StringUtils;

public class JobAdpater {

        private String clazzName;

        private String cron;

        private String jobName;

        public String getClazzName() {
                return clazzName;
        }

        public void setClazzName(String clazzName) {
                this.clazzName = clazzName;
                String jName = StringUtils.substringAfterLast(clazzName, ".");
                this.jobName = jName;
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
