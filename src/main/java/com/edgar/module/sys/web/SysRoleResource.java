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
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.service.SysRoleService;

/**
 * 角色的rest接口
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleResource {
        @Autowired
        private SysRoleService sysRoleService;

        /**
         * 保存角色
         * 
         * @param sysRole
         *                角色
         * @return 保存成功返回1，失败返回0
         */
        @AuthHelper("Create Role")
        @ResponseBody
        @RequestMapping(method = RequestMethod.POST)
        public int save(@RequestBody SysRole sysRole) {
                return sysRoleService.save(sysRole);
        }

        /**
         * 更新角色
         * 
         * @param roleId
         *                角色ID
         * @param sysRole
         *                角色
         * @return 保存成功，返回1，保存失败，返回0
         */
        @AuthHelper("Update Role")
        @RequestMapping(method = RequestMethod.PUT, value = "/{roleId}")
        @ResponseBody
        public int update(@PathVariable("roleId") int roleId, @RequestBody SysRole sysRole) {
                sysRole.setRoleId(roleId);
                return sysRoleService.update(sysRole);
        }

        /**
         * 根据角色ID查询角色
         * 
         * @param roleId
         *                角色ID
         * @return 角色
         */
        @AuthHelper("Query Role")
        @RequestMapping(method = RequestMethod.GET, value = "/{roleId}")
        @ResponseBody
        public SysRole get(@PathVariable("roleId") int roleId) {
                return sysRoleService.get(roleId);
        }

        /**
         * 分页查询角色
         * 
         * @param page
         *                当前页，默认为1
         * @param pageSize
         *                每页显示数量，默认为10
         * @param example
         *                查询条件
         * @return 角色的分页类
         */
        @AuthHelper("Query Role")
        @RequestMapping(method = RequestMethod.GET, value = "/pagination")
        @ResponseBody
        public Pagination<SysRole> pagination(
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                        @ToQueryExample QueryExample example) {
                return sysRoleService.pagination(example, page, pageSize);
        }

        /**
         * 查询角色列表
         * 
         * @param example
         *                查询条件
         * @return 角色的分页类
         */
        @AuthHelper("Query Role")
        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
        public List<SysRole> query(@ToQueryExample QueryExample example) {
                return sysRoleService.query(example);
        }

        /**
         * 根据角色ID和时间戳删除角色
         * 
         * @param roleId
         *                角色ID
         * @param updatedTime
         *                时间戳
         * @return 如果删除成功，返回1
         */
        @AuthHelper("Delete Role")
        @RequestMapping(method = RequestMethod.DELETE, value = "/{roleId}")
        @ResponseBody
        public int delete(@PathVariable("roleId") int roleId,
                        @RequestParam("updatedTime") long updatedTime) {
                return sysRoleService.deleteWithLock(roleId, updatedTime);
        }

}
