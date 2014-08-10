package com.edgar.module.sys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.init.DictoryLoader;
import com.edgar.module.sys.repository.domain.SysDict;
import com.edgar.module.sys.validator.SysDictValidator;
import com.edgar.module.sys.view.Dictory;

/**
 * 系统字典的业务逻辑类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysDictServiceImpl implements SysDictService {

        @Autowired
        private CrudRepository<String, SysDict> sysDictDao;

        private ValidatorStrategy validator = new SysDictValidator();

        @Override
        public SysDict get(String dictCode) {
                Assert.notNull(dictCode);
                return sysDictDao.get(dictCode);
        }

        @Override
        @Transactional
        public int save(SysDict sysDict) {
                Assert.notNull(sysDict);
                Assert.hasText(sysDict.getDictCode());
                if (sysDict.getSorted() == null) {
                        sysDict.setSorted(9999);
                }
                if (StringUtils.isBlank(sysDict.getParentCode())) {
                        sysDict.setParentCode("-1");
                }
                validator.validator(sysDict);
                int result = sysDictDao.insert(sysDict);
                if (!"-1".equals(sysDict.getParentCode())) {
                        SysDict parentDict = get(sysDict.getParentCode());
                        if (parentDict == null) {
                                throw ExceptionFactory.isNull();
                        }
                }
                saveOrUpdateDict(sysDict);
                return result;
        }

        @Override
        @Transactional
        public int update(SysDict sysDict) {
                validator.validator(sysDict);
                int result = sysDictDao.update(sysDict);
                saveOrUpdateDict(sysDict);
                return result;
        }

        /*
         * 先删除字典，在删除字典子项会引发INNODB死锁，原因还未找到
         */
        @Override
        @Transactional
        public int deleteWithLock(String dictCode, long updatedTime) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("parentCode", dictCode);
                sysDictDao.delete(example);
                int result = sysDictDao.deleteByPkAndVersion(dictCode, updatedTime);
                SysDict sysDict = new SysDict();
                sysDict.setDictCode(dictCode);
                deleteDict(sysDict);
                return result;
        }

        @Override
        public Pagination<SysDict> pagination(QueryExample example, int page, int pageSize) {
                return sysDictDao.pagination(example, page, pageSize);
        }

        @Override
        public List<SysDict> query(QueryExample example) {
                return sysDictDao.query(example);
        }

        @Override
        public boolean checkDictCode(String dictCode) {
                Assert.notNull(dictCode);
                SysDict sysDict = sysDictDao.get(dictCode);
                if (sysDict != null) {
                        return false;
                }
                return true;
        }

        /**
         * 更新或修改字典
         * 
         * @param sysDict
         *                字典
         */
        private void saveOrUpdateDict(SysDict sysDict) {
                Map<String, Dictory> map = DictoryLoader.getDictMap();
                if ("-1".equals(sysDict.getParentCode())) {
                        Dictory dictory = map.get(sysDict.getDictCode());
                        if (dictory != null) {
                                dictory.setSysDict(sysDict);
                        } else {
                                dictory = new Dictory();
                                dictory.setSysDict(sysDict);
                                map.put(sysDict.getDictCode(), dictory);
                        }
                } else {
                        Dictory dictory = map.get(sysDict.getParentCode());
                        if (dictory != null) {
                                dictory.put(sysDict.getDictCode(), sysDict);
                        }
                }
        }

        /**
         * 删除字典
         * 
         * @param sysDict
         *                字典
         */
        private void deleteDict(SysDict sysDict) {
                Map<String, Dictory> map = DictoryLoader.getDictMap();
                String dictCode = sysDict.getDictCode();
                map.remove(dictCode);
                Collection<Dictory> dictories = map.values();
                for (Dictory dictory : dictories) {
                        Collection<SysDict> sysDicts = dictory.values();
                        if (sysDicts == null) {
                                continue;
                        }
                        for (SysDict child : sysDicts) {
                                if (dictCode.equals(child.getDictCode())) {
                                        sysDicts.remove(child);
                                        return;
                                }
                        }
                }
        }

    public void setSysDictDao(CrudRepository<String, SysDict> sysDictDao) {
        this.sysDictDao = sysDictDao;
    }
}
