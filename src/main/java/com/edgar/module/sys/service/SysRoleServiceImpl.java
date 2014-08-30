package com.edgar.module.sys.service;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.*;
import com.edgar.module.sys.validator.SysRoleUpdateValidator;
import com.edgar.module.sys.validator.SysRoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色的业务逻辑实现
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

        @Autowired
        private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;

        @Autowired
        private CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao;

        @Autowired
        private CrudRepository<Integer, SysRoleRes> sysRoleResDao;

        @Autowired
        private CrudRepository<Integer, SysRole> sysRoleDao;

        @Autowired
        private CrudRepository<Integer, SysUserRole> sysUserRoleDao;

        private final ValidatorStrategy validator = new SysRoleValidator();

        private final ValidatorStrategy updateValidator = new SysRoleUpdateValidator();

        @Override
        @Transactional
        public int save(SysRole sysRole) {
                validator.validator(sysRole);
                sysRole.setIsRoot(false);
                sysRole.setRoleId(IDUtils.getNextId());
                return sysRoleDao.insert(sysRole);
        }

        @Override
        @Transactional
        public int update(SysRole sysRole) {
                updateValidator.validator(sysRole);
                return sysRoleDao.update(sysRole);
        }

        @Override
        public SysRole get(int roleId) {
                return sysRoleDao.get(roleId);
        }

        @Override
        public Pagination<SysRole> pagination(QueryExample example, int page, int pageSize) {
                example.equalsTo("isRoot", 0);
                return sysRoleDao.pagination(example, page, pageSize);
        }

        @Override
        public List<SysRole> query(QueryExample example) {
                example.equalsTo("isRoot", 0);
                return sysRoleDao.query(example);
        }

        @Override
        @Transactional
        public long deleteWithLock(int roleId, long updatedTime) {
                long result = sysRoleDao.deleteByPkAndVersion(roleId, updatedTime);
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("roleId", roleId);
                sysUserRoleDao.delete(example);
                sysRoleRouteDao.delete(example);
                sysRoleMenuDao.delete(example);
                sysRoleResDao.delete(example);
                return result;
        }

    public void setSysRoleRouteDao(CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao) {
        this.sysRoleRouteDao = sysRoleRouteDao;
    }

    public void setSysRoleMenuDao(CrudRepository<Integer, SysRoleMenu> sysRoleMenuDao) {
        this.sysRoleMenuDao = sysRoleMenuDao;
    }

    public void setSysRoleResDao(CrudRepository<Integer, SysRoleRes> sysRoleResDao) {
        this.sysRoleResDao = sysRoleResDao;
    }

    public void setSysRoleDao(CrudRepository<Integer, SysRole> sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    public void setSysUserRoleDao(CrudRepository<Integer, SysUserRole> sysUserRoleDao) {
        this.sysUserRoleDao = sysUserRoleDao;
    }
}
