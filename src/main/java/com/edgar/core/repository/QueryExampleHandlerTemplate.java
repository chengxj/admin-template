package com.edgar.core.repository;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.Path;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * Created by Administrator on 2014/8/26.
 */
public abstract class QueryExampleHandlerTemplate implements QueryExampleHandler {
    protected RelationalPathBase<?> pathBase;

    protected QueryExample example;

    public QueryExampleHandlerTemplate(RelationalPathBase<?> pathBase, QueryExample example) {
        this.pathBase = pathBase;
        this.example = example;
    }
    /**
     * 字符串转换，将alarm_user_code转换为alarmUserCode
     *
     * @param source
     *            需要转换的字符串
     * @return 转换后的字符串
     */
    protected String humpName(final String source) {
        Assert.hasLength(source);
        if (StringUtils.contains(source, "_")) {
            String lowerSource = source.toLowerCase();
            String[] words = lowerSource.split("_");
            StringBuilder result = new StringBuilder();
            result.append(words[0]);
            int length = words.length;
            for (int i = 1; i < length; i++) {
                result.append(StringUtils.capitalize(words[i]));
            }
            return result.toString();
        }
        return source;
    }

    /**
     * 字符串转换，将alarmUserCode转换为alarm_user_code
     *
     * @param source
     *            需要转换的字符串
     * @return 转换后的字符串
     */
    protected String underscoreName(String source) {
        Assert.hasLength(source);
        StringBuilder result = new StringBuilder();
        result.append(source.substring(0, 1).toLowerCase());
        for (int i = 1; i < source.length(); i++) {
            String s = source.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    /**
     * 检查字段是否是数据库的字段
     *
     * @param path
     *            path
     * @param field
     *            字段名
     * @return 是，返回true,不是，返回false
     */
    protected boolean checkColumn(Path<?> path, String field) {
        return path.getMetadata().getName().equals(field) || path.getMetadata().getName().equals(humpName(field));
    }
}
