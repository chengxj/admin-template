package com.edgar.core.auth.stateless;

import com.edgar.module.sys.repository.domain.SysRole;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-12
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class StatelessRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory
            .getLogger(StatelessRealm.class);
    @Autowired
    private StatelessUserService statelessUserService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //        if (principalCollection == null) {
//            throw new AuthorizationException(
//                    "PrincipalCollection method argument cannot be null.");
//        }

        logger.debug("StatelessRealm doGetAuthenticationInfo");
        String accessToken = (String) getAvailablePrincipal(principalCollection);
        StatelessUser user = statelessUserService.getUser(accessToken);
        Set<String> roleNames = new LinkedHashSet<String>();
        for (SysRole sysRole : user.getRoles()) {
            roleNames.add(sysRole.getRoleName());
        }

        Set<String> permissions = statelessUserService.getPermissions(user.getRoles());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.debug("StatelessRealm doGetAuthenticationInfo");

        StatelessToken statelessToken = (StatelessToken) authenticationToken;
        String accessToken = statelessToken.getAccessToken();
        String baseString = statelessToken.getBaseString();

        String key = statelessUserService.getSecretKey(accessToken);// 根据用户名获取密钥（和客户端的一样）
        // 在服务器端生成客户端参数消息摘要
        String serverDigest = HmacSHA256Utils.digest(key,
                baseString);
        return new SimpleAuthenticationInfo(accessToken, serverDigest, getName());
    }

}
