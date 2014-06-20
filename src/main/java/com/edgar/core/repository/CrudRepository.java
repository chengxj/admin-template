package com.edgar.core.repository;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * DAO的接口
 * 
 * @author Edgar Zhang
 * 
 * @version 1.0
 * 
 * @param <PK>
 *                主键
 * @param <T>
 *                实体类
 */
@Validated
public interface CrudRepository<PK, T> {

        /**
         * 根据查询条件查询列表
         * 
         * @param example
         *                查询条件
         * @return 实体类的列表
         */
        List<T> query(@NotNull QueryExample example);

        /**
         * 根据查询条件查询单个字段，返回字段的列表
         * 
         * @param example
         *                查询条件
         * @return 字段的列表
         */
        <E> List<E> querySingleColumn(@NotNull QueryExample example, Class<E> elementType);

        /**
         * 根据查询条件分页查询列表
         * 
         * @param example
         *                查询条件
         * @param page
         *                页码
         * @param pageSize
         *                每页显示的数量
         * @return 实体类的分页类
         */
        Pagination<T> pagination(@NotNull QueryExample example, @Min(1) int page,
                        @Min(1) int pageSize);

        /**
         * 根据实体类向数据库中新增一条记录
         * 
         * @param domain
         *                实体类
         * @return 如果插入成功，返回1;如果插入失败，返回0
         */
        int insert(@NotNull T domain);

        /**
         * 向数据库中批量新增记录
         * 
         * @param domains
         *                实体类的集合
         * @return 如果插入成功，返回1;如果插入失败，返回0
         */
        int[] insert(@NotEmpty List<T> domains);

        /**
         * 根据主键查询记录
         * 
         * @param pk
         *                主键
         * @param fields
         *                返回的字段，可以对应实体类的属性
         * @return 实体类
         */
        T get(@NotNull PK pk, @NotEmpty List<String> fields);

        /**
         * 根据主键查询记录
         * 
         * @param pk
         *                主键
         * @return 实体类
         */
        T get(@NotNull PK pk);

        // /**
        // * 根据主键锁住行
        // *
        // * @param pk
        // * 主键
        // */
        // void lock(@NotNull PK pk);

        /**
         * 根据条件更新记录，凡是不等于null的属性都会被更新
         * 
         * @param domain
         *                实体类
         * @param example
         *                查询条件
         * @return 如果更新成功，返回1;如果更新失败，返回0
         */
        int update(@NotNull T domain, @NotNull QueryExample example);

        /**
         * 根据主键更新记录
         * 
         * @param domain
         *                实体类
         * @return 如果更新成功，返回1;如果更新失败，返回0
         */
        int update(@NotNull T domain);

        /**
         * 根据主键和版本号更新记录
         * 
         * @param domain
         *                实体类
         * @return 如果更新成功，返回1;如果更新失败，返回0
         */
        int updateByVersion(@NotNull T domain);

        /**
         * 根据主键删除记录
         * 
         * @param pk
         *                主键
         * @return 如果删除成功，返回1;如果删除失败，返回0
         */
        int deleteByPk(@NotNull PK pk);

        /**
         * 根据主键和时间戳删除记录
         * 
         * @param pk
         *                主键
         * @param updatedTime
         *                时间戳
         * @return 如果删除成功，返回1;如果删除失败，返回0
         */
        int deleteByPkAndVersion(@NotNull PK pk, @Min(0) long updatedTime);

        /**
         * 如果条件删除记录
         * 
         * @param example
         *                查询条件
         * @return 返回成功删除的记录数
         */
        int delete(@NotNull QueryExample example);

        /**
         * 根据查询条件查询列表
         * 
         * @param example
         *                查询条件
         * @param extendQuery
         *                扩展查询接口，如果需要除基本查询外的其他查询，如join，exists，min等，可以通过这个接口中的实现
         * @return 实体类的列表
         */
        List<T> query(@NotNull QueryExample example, @NotNull ExtendQuery extendQuery);

        /**
         * 根据查询条件分页查询列表
         * 
         * @param example
         *                查询条件
         * @param page
         *                页码
         * @param pageSize
         *                每页显示的数量
         * @param extendQuery
         *                扩展查询接口，如果需要除基本查询外的其他查询，如join，exists，min等，可以通过这个接口中的实现
         * @return 实体类的分页类
         */
        Pagination<T> pagination(@NotNull QueryExample example, @Min(1) int page,
                        @Min(1) int pageSize, @NotNull ExtendQuery extendQuery);

        /**
         * 根据查询条件，查询出单条记录
         * 
         * @param example
         *                查询记录
         * @return 实体类
         */
        T uniqueResult(@NotNull QueryExample example);

}