package com.edgar.module.sys.dao;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.I18nMessage;
import com.edgar.module.sys.repository.querydsl.QI18nMessage;
import com.mysema.query.sql.RelationalPathBase;

@Repository
public class I18nMessageDao extends AbstractCrudRepositoryTemplate<Integer, I18nMessage> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QI18nMessage.i18nMessage;
        }

}
