package com.edgar.module.sys.dao;

import com.edgar.core.repository.AbstractDaoTemplate;
import org.springframework.stereotype.Repository;

import com.edgar.module.sys.repository.domain.SysUserProfile;
import com.edgar.module.sys.repository.querydsl.QSysUserProfile;
import com.mysema.query.sql.RelationalPathBase;

@Repository
public class SysUserProfileDao extends AbstractDaoTemplate<Integer, SysUserProfile> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysUserProfile.sysUserProfile;
        }

}
