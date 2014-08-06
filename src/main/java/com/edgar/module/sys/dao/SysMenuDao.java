package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.querydsl.QSysMenu;
import com.mysema.query.sql.RelationalPathBase;

/**
 * 系统菜单的DAO
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Repository
public class SysMenuDao extends AbstractCrudRepositoryTemplate<Integer, SysMenu> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysMenu.sysMenu;
        }

        @Override
        public boolean cacheEnabled() {
                return true;
        }

}
