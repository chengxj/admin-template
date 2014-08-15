package com.edgar.module.sys.service;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.SysRoute;

/**
 * 路由的业务逻辑接口
 *
 * @author Edgar Zhang
 * @version 1.0
 */
@Validated
public interface SysRouteService {

    /**
     * 查询所有的路由，不包括系统路由
     *
     * @return 路由的集合
     */
    @NotNull
    List<SysRoute> findAll();

    /**
     * 新增路由
     *
     * @param sysRoute 路由
     * @return 如果保存成功，返回<code>1</code>
     */
    int save(@NotNull SysRoute sysRoute);

    /**
     * 修改路由
     *
     * @param sysRoute 路由
     * @return 如果保存成功，返回<code>1</code>
     */
    int update(@NotNull SysRoute sysRoute);

    /**
     * 根据路由ID查询路由
     *
     * @param routeId 路由ID
     * @return 路由
     */
    SysRoute get(int routeId);

    /**
     * 分页查询路由
     *
     * @param example  查询条件
     * @param page     当前页
     * @param pageSize 每页记录数
     * @return 路由的分页类
     */
    @NotNull
    Pagination<SysRoute> pagination(@NotNull QueryExample example, @Min(1) int page,
                                    @Min(1) int pageSize);

    /**
     * 根据路由ID和时间戳删除路由
     *
     * @param routeId     路由ID
     * @param updatedTime 时间戳
     * @return 如果删除成功，返回<code>1</code>
     */
    int deleteWithLock(int routeId, @Min(0) long updatedTime);

    /**
     * 查询所有路由，包括系统路由
     *
     * @return 路由的集合
     */
    List<SysRoute> findAllWithRoot();
}
