package com.edgar.module.sys.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.I18nMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 国际化的业务逻辑实现类
 *
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class I18nMessageServiceImpl implements I18nMessageService {
    @Autowired
    private CrudRepository<Integer, I18nMessage> i18nMessageDao;

    @Override
    public int save(I18nMessage i18n) {
        i18n.setI18nId(IDUtils.getNextId());
        return i18nMessageDao.insert(i18n);
    }

    @Override
    public int update(I18nMessage i18n) {
        return i18nMessageDao.update(i18n);
    }

    @Override
    public I18nMessage get(int i18nId) {
        return i18nMessageDao.get(i18nId);
    }

    @Override
    public Pagination<I18nMessage> pagination(QueryExample example, int page, int pageSize) {
        example.asc("i18nKey");
        return i18nMessageDao.pagination(example, page, pageSize);
    }

    @Override
    public List<I18nMessage> query(QueryExample example) {
        return i18nMessageDao.query(example);
    }

    @Override
    public int deleteWithLock(int i18nId, long updatedTime) {
        return i18nMessageDao.deleteByPkAndVersion(i18nId, updatedTime);
    }

    @Override
    public boolean checkKey(String i18nKey) {
        Assert.notNull(i18nKey);
        QueryExample example = QueryExample.newInstance();
        example.equalsTo("i18nKey", i18nKey);
        List<I18nMessage> i18ns = query(example);
        if (i18ns == null || i18ns.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void saveToJsonFile() throws IOException {
        QueryExample example = QueryExample.newInstance();
        example.asc("i18nKey");
        List<I18nMessage> i18ns = query(example);
        String webRoot = System.getProperty("web.root");
        File enFile = new File(webRoot + File.separator + "app" + File.separator + "i18n" + File.separator + "locale-en.json");
        File zhCnFile = new File(webRoot + File.separator + "app" + File.separator + "i18n" + File.separator + "locale-zh_CN.json");
        File zhTwFile = new File(webRoot + File.separator + "app" + File.separator + "i18n" + File.separator + "locale-zh_TW.json");
        Map<String, String> enLocale = new HashMap<String, String>(i18ns.size());
        Map<String, String> zhCnLocale = new HashMap<String, String>(i18ns.size());
        Map<String, String> zhTwLocale = new HashMap<String, String>(i18ns.size());
        for (I18nMessage i18n : i18ns) {
            enLocale.put(i18n.getI18nKey(), i18n.getI18nValueEn());

            zhCnLocale.put(i18n.getI18nKey(), i18n.getI18nValueZhCn());
            zhTwLocale.put(i18n.getI18nKey(), i18n.getI18nValueZhTw());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(enFile, enLocale);
        mapper.writeValue(zhCnFile, zhCnLocale);
        mapper.writeValue(zhTwFile, zhTwLocale);
    }

    public void setI18nMessageDao(CrudRepository<Integer, I18nMessage> i18nMessageDao) {
        this.i18nMessageDao = i18nMessageDao;
    }
}
