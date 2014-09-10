package com.edgar.module.sys.service.impl;

import com.edgar.core.job.JobAdpater;
import com.edgar.core.job.JobScheduler;
import com.edgar.core.repository.BaseDao;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.SysJob;
import com.edgar.module.sys.service.SysJobService;
import com.edgar.module.sys.validator.SysJobUpdateValidator;
import com.edgar.module.sys.validator.SysJobValidator;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作业的业务逻辑实现
 *
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysJobServiceImpl implements SysJobService {
    @Autowired
    private BaseDao<Integer, SysJob> sysJobDao;

    @Autowired
    private JobScheduler jobScheduler;

    private final ValidatorStrategy validator = new SysJobValidator();

    private final ValidatorStrategy updateValidator = new SysJobUpdateValidator();

    @Override
    @Transactional
    public void save(SysJob sysJob) {
        Preconditions.checkNotNull(sysJob);
        sysJob.setJobId(IDUtils.getNextId());
        validator.validator(sysJob);
        sysJobDao.insert(sysJob);
        if (BooleanUtils.isTrue(sysJob.getEnabled())) {
            JobAdpater job = new JobAdpater();
            job.setClazzName(sysJob.getClazzName());
            job.setCron(sysJob.getCron());
            jobScheduler.addAndStartJob(job);
        }
    }

    @Override
    @Transactional
    public void update(SysJob sysJob) {
        Preconditions.checkNotNull(sysJob);
        updateValidator.validator(sysJob);
        sysJobDao.update(sysJob);
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
    public void deleteWithLock(int jobId, long updatedTime) {
        SysJob sysJob = get(jobId);
        sysJobDao.deleteByPkAndVersion(jobId, updatedTime);
        JobAdpater job = new JobAdpater();
        job.setClazzName(sysJob.getClazzName());
        job.setCron(sysJob.getCron());
        jobScheduler.deleteJob(job);
    }

    @Override
    public boolean checkClazzName(String clazzName) {
        Preconditions.checkNotNull(clazzName);
        QueryExample example = QueryExample.newInstance();
        example.equalsTo("clazzName", clazzName);
        List<SysJob> sysJobs = query(example);
        return sysJobs == null || sysJobs.isEmpty();
    }

    @Override
    public List<SysJob> findEnabledJob() {
        QueryExample example = QueryExample.newInstance();
        example.equalsTo("enabled", 1);
        return query(example);
    }

    public void setSysJobDao(BaseDao<Integer, SysJob> sysJobDao) {
        this.sysJobDao = sysJobDao;
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }
}
