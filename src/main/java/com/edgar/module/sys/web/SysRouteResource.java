package com.edgar.module.sys.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edgar.core.mvc.ToQueryExample;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.shiro.AuthHelper;
import com.edgar.module.sys.repository.domain.SysRoute;
import com.edgar.module.sys.repository.domain.SysRouteRes;
import com.edgar.module.sys.service.RouteResCommand;
import com.edgar.module.sys.service.SysRouteService;

/**
 * 路由的rest
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/route")
public class SysRouteResource {

        @Autowired
        private SysRouteService sysRouteService;

        /**
         * 保存路由
         * 
         * @param sysRoute
         *                路由
         * @return 保存成功返回1，失败返回0
         */
        @AuthHelper(value = "Create Route", isRoot = true)
        @RequestMapping(method = RequestMethod.POST)
        @ResponseBody
        public int save(@RequestBody SysRoute sysRoute) {
                return sysRouteService.save(sysRoute);
        }

        /**
         * 更新路由
         * 
         * @param routeId
         *                路由ID
         * @param sysRoute
         *                路由
         * @return 保存成功，返回1，保存失败，返回0
         */
        @AuthHelper(value = "Update Route", isRoot = true)
        @RequestMapping(method = RequestMethod.PUT, value = "/{routeId}")
        @ResponseBody
        public int update(@PathVariable("routeId") int routeId, @RequestBody SysRoute sysRoute) {
                sysRoute.setRouteId(routeId);
                return sysRouteService.update(sysRoute);
        }

        /**
         * 根据路由ID查询路由
         * 
         * @param routeId
         *                路由ID
         * @return 路由
         */
        @AuthHelper(value = "View Route", isRoot = true)
        @RequestMapping(method = RequestMethod.GET, value = "/{routeId}")
        @ResponseBody
        public SysRoute get(@PathVariable("routeId") int routeId) {
                return sysRouteService.get(routeId);
        }

        /**
         * 分页查询路由
         * 
         * @param page
         *                当前页，默认为1
         * @param pageSize
         *                每页显示数量，默认为10
         * @param example
         *                查询条件
         * @return 路由的分页类
         */
        @AuthHelper(value = "Paging Query Route", isRoot = true)
        @RequestMapping(method = RequestMethod.GET, value = "/pagination")
        @ResponseBody
        public Pagination<SysRoute> pagination(
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                        @ToQueryExample QueryExample example) {
                return sysRouteService.pagination(example, page, pageSize);
        }

        /**
         * 根据路由ID和时间戳查询路由
         * 
         * @param routeId
         *                路由ID
         * @param updatedTime
         *                时间戳
         * @return 如果删除成功，返回1
         */
        @AuthHelper(value = "Delete Route", isRoot = true)
        @RequestMapping(method = RequestMethod.DELETE, value = "/{routeId}")
        @ResponseBody
        public int delete(@PathVariable("routeId") int routeId,
                        @RequestParam("updatedTime") long updatedTime) {
                return sysRouteService.deleteWithLock(routeId, updatedTime);
        }

        /**
         * 查询所有的路由
         * 
         * @return 路由的集合
         */
        @AuthHelper(value = "Query Route", isRoot=true)
        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
        public List<SysRoute> findAll() {
                return sysRouteService.findAll();
        }

        /**
         * 查询与路由关联的资源
         * 
         * @return 资源的集合
         */
        @AuthHelper(value = "Query relation of resource and route", isRoot = true)
        @RequestMapping(method = RequestMethod.GET, value = "/resource/{routeId}")
        @ResponseBody
        public List<SysRouteRes> getResource(@PathVariable("routeId") int routeId) {
                return sysRouteService.getResource(routeId);
        }

        /**
         * 保存路由与资源的关联
         * 
         * @param command
         *                资源授权的对象
         * @return 如果授权成功，返回1
         */
        @AuthHelper(value = "Relate resource and route", isRoot = true)
        @RequestMapping(value = "/resource", method = RequestMethod.POST)
        @ResponseBody
        public int saveResourcePermission(@RequestBody RouteResCommand command) {
                sysRouteService.saveRouteRes(command);
                return 1;
        }
}
