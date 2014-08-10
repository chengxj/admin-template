package com.edgar.module.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.repository.domain.SysRoleRes;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.domain.SysUserRole;
import com.edgar.module.sys.validator.SysRoleUpdateValidator;
import com.edgar.module.sys.validator.SysRoleValidator;

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

        // private boolean validRoleId(int roleId) {
        // Set<Integer> roleIds = LoginUserUtils.getRoleIds();
        // if (roleIds.contains(roleId)) {
        // throw ExceptionFactory.forbidden("不允许修改自己的角色");
        // }
        // return true;
        // }

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
        public int deleteWithLock(int roleId, long updatedTime) {
                int result = sysRoleDao.deleteByPkAndVersion(roleId, updatedTime);
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
