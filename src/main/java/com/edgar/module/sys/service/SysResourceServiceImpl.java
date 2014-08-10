package com.edgar.module.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.GlobalUtils;
import com.edgar.module.sys.repository.domain.SysResource;

/**
 * 资源的业务逻辑实现类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {

        @Autowired
        private CrudRepository<Integer, SysResource> sysResourceDao;

        @Override
        public List<SysResource> findAll() {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("isRoot", 0);
                example.equalsTo("authType", GlobalUtils.AUTH_TYPE_REST);
                example.asc("url");
                return sysResourceDao.query(example);
        }


        @Override
        public Pagination<SysResource> pagination(QueryExample example, int page, int pageSize) {
                example.asc("url");
                example.equalsTo("isRoot", 0);
                example.equalsTo("authType", GlobalUtils.AUTH_TYPE_REST);
                return sysResourceDao.pagination(example, page, pageSize);
        }

        @Override
        public SysResource get(int resourceId) {
                return sysResourceDao.get(resourceId);
        }

    public void setSysResourceDao(CrudRepository<Integer, SysResource> sysResourceDao) {
        this.sysResourceDao = sysResourceDao;
    }
}
