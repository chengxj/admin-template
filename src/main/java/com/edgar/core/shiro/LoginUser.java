package com.edgar.core.shiro;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.repository.domain.SysUserProfile;

/**
 * 登录用户
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class LoginUser extends SysUser implements Serializable {

        private static final long serialVersionUID = -3703011731457038587L;
        
        /**
         * Profile
         */
        private SysUserProfile profile;

        /**
         * 角色列表
         */
        private List<SysRole> roles;

        /**
         * 授权列表
         */
        private Set<String> permissions;

        public SysUserProfile getProfile() {
                return profile;
        }

        public void setProfile(SysUserProfile profile) {
                this.profile = profile;
        }

        public List<SysRole> getRoles() {
                return roles;
        }

        public void setRoles(List<SysRole> roles) {
                this.roles = roles;
        }

        public Set<String> getPermissions() {
                return permissions;
        }

        public void setPermissions(Set<String> permissions) {
                this.permissions = permissions;
        }

}
