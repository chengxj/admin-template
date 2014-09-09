package com.edgar.module.sys.dao;

import com.edgar.core.repository.AbstractDaoTemplate;
import org.springframework.stereotype.Repository;

import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.repository.querydsl.QSysUser;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 系统用户的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysUserDao extends AbstractDaoTemplate<Integer, SysUser> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysUser.sysUser;
        }

}
