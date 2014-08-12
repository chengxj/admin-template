package com.edgar.core.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-12
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class StatelessRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        StatelessToken statelessToken = (StatelessToken) authenticationToken;
        String username = statelessToken.getUsername();
        String clientDigest = statelessToken.getDigest();

        String key = getKey(username);// 根据用户名获取密钥（和客户端的一样）
        // 在服务器端生成客户端参数消息摘要
        String serverDigest = HmacSHA256Utils.digest(key,
                statelessToken.getParams());
        return new SimpleAuthenticationInfo(username, serverDigest, getName());
    }

    private String getKey(String username) {// 得到密钥，此处硬编码一个
        if ("admin".equals(username)) {
            return "dadadswdewq2ewdwqdwadsadasd";
        }
        return null;
    }
}
