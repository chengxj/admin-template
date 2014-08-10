package com.edgar.module.sys.service;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.job.JobAdpater;
import com.edgar.core.job.JobScheduler;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.SysJob;
import com.edgar.module.sys.validator.SysJobUpdateValidator;
import com.edgar.module.sys.validator.SysJobValidator;

/**
 * 作业的业务逻辑实现
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysJobServiceImpl implements SysJobService {
        @Autowired
        private CrudRepository<Integer, SysJob> sysJobDao;

        @Autowired
        private JobScheduler jobScheduler;
        
        private ValidatorStrategy validator = new SysJobValidator();
        
        private ValidatorStrategy updateValidator = new SysJobUpdateValidator();

        @Override
        @Transactional
        public int save(SysJob sysJob) {
                Assert.notNull(sysJob);
                sysJob.setJobId(IDUtils.getNextId());
                validator.validator(sysJob);
                int result = sysJobDao.insert(sysJob);
                if (BooleanUtils.isTrue(sysJob.getEnabled())) {
                        JobAdpater job = new JobAdpater();
                        job.setClazzName(sysJob.getClazzName());
                        job.setCron(sysJob.getCron());
                        jobScheduler.addAndStartJob(job);
                }
                return result;
        }

        @Override
        @Transactional
        public int update(SysJob sysJob) {
                Assert.notNull(sysJob);
                updateValidator.validator(sysJob);
                int result = sysJobDao.update(sysJob);
                if (BooleanUtils.isTrue(sysJob.getEnabled())) {
                        JobAdpater job = new JobAdpater();
                        job.setClazzName(sysJob.getClazzName());
                        job.setCron(sysJob.getCron());
                        jobScheduler.updateJob(job);
                } else {
                        JobAdpater job = new JobAdpater();
                        job.setClazzName(sysJob.getClazzName());
                        job.setCron(sysJob.getCron());
                        jobScheduler.deleteJob(job);
                }
                return result;
        }

        @Override
        public SysJob get(int jobId) {
                return sysJobDao.get(jobId);
        }

        @Override
        public Pagination<SysJob> pagination(QueryExample example, int page, int pageSize) {
                return sysJobDao.pagination(example, page, pageSize);
        }

        @Override
        public List<SysJob> query(QueryExample example) {
                return sysJobDao.query(example);
        }

        @Override
        @Transactional
        public int deleteWithLock(int jobId, long updatedTime) {
                SysJob sysJob = get(jobId);
                int result = sysJobDao.deleteByPkAndVersion(jobId, updatedTime);
                JobAdpater job = new JobAdpater();
                job.setClazzName(sysJob.getClazzName());
                job.setCron(sysJob.getCron());
                jobScheduler.deleteJob(job);
                return result;
        }

        @Override
        public boolean checkClazzName(String clazzName) {
                Assert.notNull(clazzName);
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("clazzName", clazzName);
                List<SysJob> sysJobs = query(example);
                if (sysJobs == null || sysJobs.isEmpty()) {
                        return true;
                }
                return false;
        }

        @Override
        public List<SysJob> findEnabledJob() {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("enabled", 1);
                return query(example);
        }

    public void setSysJobDao(CrudRepository<Integer, SysJob> sysJobDao) {
        this.sysJobDao = sysJobDao;
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }
}
