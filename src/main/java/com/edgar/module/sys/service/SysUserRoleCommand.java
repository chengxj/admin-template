package com.edgar.module.sys.service;

import com.edgar.core.command.Command;
import com.edgar.module.sys.repository.domain.SysUser;

/**
 * 用户角色的工具类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class SysUserRoleCommand extends SysUser implements Command {

        private String roleIds;

        public String getRoleIds() {
                return roleIds;
        }

        public void setRoleIds(String roleIds) {
                this.roleIds = roleIds;
        }

}
