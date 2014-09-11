package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractDaoTemplate;
import com.edgar.module.sys.repository.domain.SysRoleMenu;
import com.edgar.module.sys.repository.querydsl.QSysRoleMenu;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 角色菜单的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysRoleMenuDao extends AbstractDaoTemplate<Integer, SysRoleMenu> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysRoleMenu.sysRoleMenu;
        }

}
