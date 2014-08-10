package com.edgar.module.sys.service;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.SysDict;

/**
 * 系统字典的业务逻辑类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Validated
public interface SysDictService {

        /**
         * 根据字典编码查询字典
         * 
         * @param dictCode
         *                字典编码
         * @return 字典
         */
        SysDict get(String dictCode);

        /**
         * 保存字典
         * 
         * @param sysDict
         *                字典
         * @return 如果保存成功，返回<code>1</code>
         */
        int save(SysDict sysDict);

        /**
         * 更新字典
         * 
         * @param sysDict
         *                字典
         * @return 如果保存成功，返回<code>1</code>
         */
        int update(SysDict sysDict);

        /**
         * 根据字典编码和时间戳删除字典
         * 
         * @param dictCode
         *                字典编码
         * @param updatedTime
         *                时间戳
         * @return 如果删除成功，返回<code>1</code>
         */
        int deleteWithLock(String dictCode, long updatedTime);

        /**
         * 分页查询字典
         * 
         * @param example
         *                查询条件
         * @param page
         *                当前页
         * @param pageSize
         *                每页显示数量
         * @return 字典的分页类
         */
        Pagination<SysDict> pagination(QueryExample example, int page, int pageSize);

        /**
         * 查询字典列表
         * 
         * @param example
         *                查询条件
         * @return 字典的集合类
         */
        List<SysDict> query(QueryExample example);

        /**
         * 检查字典编码是否存在
         * 
         * @param dictCode
         *                字典编码
         * @return 如果存在，则返回false，如果不存在，则返回true
         */
        boolean checkDictCode(String dictCode);

}
