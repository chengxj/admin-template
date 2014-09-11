package com.edgar.module.sys.dao;

import com.edgar.core.repository.AbstractDaoTemplate;
import org.springframework.stereotype.Repository;

import com.edgar.module.sys.repository.domain.I18nMessage;
import com.edgar.module.sys.repository.querydsl.QI18nMessage;
import com.mysema.query.sql.RelationalPathBase;

@Repository
public class I18nMessageDao extends AbstractDaoTemplate<Integer, I18nMessage> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QI18nMessage.i18nMessage;
        }

}
