package com.edgar.core.repository;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.Test2Table;
import com.edgar.module.sys.repository.querydsl.QTest2Table;
import com.mysema.query.sql.RelationalPathBase;

@Repository
public class Test2TableDao extends AbstractCrudRepositoryTemplate<Test2TablePk, Test2Table> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QTest2Table.test2Table;
        }

}
