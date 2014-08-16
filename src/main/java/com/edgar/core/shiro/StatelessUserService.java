package com.edgar.core.shiro;

import com.edgar.core.cache.CacheWrapper;
import com.edgar.core.cache.EhCacheWrapper;
import com.edgar.module.sys.repository.domain.SysResource;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.repository.domain.SysUserRole;
import com.edgar.module.sys.service.PermissionService;
import com.edgar.module.sys.service.SysRoleService;
import com.edgar.module.sys.service.SysUserService;
import net.sf.ehcache.CacheManager;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-15
 * Time: 下午4:23
 * To change this template use File | Settings | File Templates.
 */
@Service
public class StatelessUserService {
    private CacheWrapper<String, Token> cacheWrapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        cacheWrapper = new EhCacheWrapper<String, Token>("StatelessCache", cacheManager);
    }

    public Token newToken(String username) {
        Token token = TokenManager.newToken(username);
        cacheWrapper.put(token.getAccessToken(), token);
        return token;
    }

    public String getSecretKey(String accessToken) {
        Token token = cacheWrapper.get(accessToken);
        return token.getSecretKey();
    }

    public void removeToken(String accessToken) {
        cacheWrapper.remove(accessToken);
    }

    public String getUsername(String accessToken) {
        Token token = cacheWrapper.get(accessToken);
        return token.getUsername();
    }

    public StatelessUser getUser(String accessToken) {
        String username = getUsername(accessToken);
        List<SysUser> sysUserList = sysUserService.queryByUsername(username);

        if (CollectionUtils.isNotEmpty(sysUserList)) {
            SysUser sysUser = sysUserList.get(0);
            StatelessUser statelessUser = new StatelessUser();
            statelessUser.setUserId(sysUser.getUserId());
            statelessUser.setUsername(sysUser.getUsername());
            statelessUser.setFullName(sysUser.getFullName());
            statelessUser.setEmail(sysUser.getEmail());
            statelessUser.setRoles(getRolesForUser(sysUser.getUserId()));
            statelessUser.setAccessToken(accessToken);
            return statelessUser;
        }
        return null;
    }


    /**
     * 根据用户ID获取用户角色
     *
     * @param userId 用户ID
     * @return 角色的集合
     */
    protected List<SysRole> getRolesForUser(int userId) {

        List<SysUserRole> sysUserRoles = sysUserService.getRoles(userId);
        List<SysRole> roles = new ArrayList<SysRole>();
        for (SysUserRole sysUserRole : sysUserRoles) {
            roles.add(sysRoleService.get(sysUserRole.getRoleId()));
        }
        return roles;
    }

    /**
     * 获取用户的授权
     *
     * @param roles 角色列表
     * @return 授权的集合
     */
    protected Set<String> getPermissions(List<SysRole> roles) {
        Set<String> permissions = new LinkedHashSet<String>();
        for (SysRole role : roles) {
            List<SysResource> sysResources = permissionService
                    .getResource(role.getRoleId());
            for (SysResource sysResource : sysResources) {
                permissions.add(sysResource.getPermission());
            }
        }

        return permissions;
    }
}
