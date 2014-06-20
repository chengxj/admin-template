package com.edgar.core.repository;

import org.springframework.stereotype.Repository;

import com.edgar.core.repository.AbstractCrudRepositoryTemplate;
import com.edgar.module.sys.repository.domain.TestTable;
import com.edgar.module.sys.repository.querydsl.QTestTable;
import com.mysema.query.sql.RelationalPathBase;

@Repository
public class TestTableDao extends AbstractCrudRepositoryTemplate<String, TestTable> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QTestTable.testTable;
        }
        
        @Override
        public boolean cacheEnabled() {
                return true;
        }

}
