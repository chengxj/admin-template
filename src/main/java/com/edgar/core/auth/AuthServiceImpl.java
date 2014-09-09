package com.edgar.core.auth;

import com.edgar.core.auth.stateless.StatelessUser;
import com.edgar.core.cache.CacheWrapper;
import com.edgar.core.cache.EhCacheWrapper;
import com.edgar.core.util.Constants;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.facade.UserFacde;
import com.edgar.module.sys.repository.domain.SysUser;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * 用户权限的业务逻辑
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final int EXPIRES_IN = 30 * 60 * 1000;
    private static final int REPLAY_ATTACK_EXPIRE = 5 * 60 * 1000;
    private static final int ACCESS_TOKEN_TIME_TO_LIVE = 30 * 60;
    private static final int REFRESH_TOKEN_TIME_TO_LIVE = 24 * 60 * 60;

    private CacheWrapper<String, AccessToken> accessTokenCacheWrapper;

    private CacheWrapper<String, String> replyAttackCacheWrapper;

    private CacheWrapper<String, AccessToken> refreshTokenCacheWrapper;

    @Autowired
    private UserFacde userFacde;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        accessTokenCacheWrapper = new EhCacheWrapper<String, AccessToken>("StatelessCache", cacheManager);
        replyAttackCacheWrapper = new EhCacheWrapper<String, String>("ReplayAttackCache", cacheManager);
        refreshTokenCacheWrapper = new EhCacheWrapper<String, AccessToken>("RefreshTokenCache", cacheManager);
    }

    /**
     * 校验请求合法，下面两种情况被认为是非法请求
     * <pre>
     * 如果时间戳距离现在已经超过5分钟
     * 如果随机数+'-'+时间戳的键已经存在
     * </pre>
     *
     * @param nonce     随机数
     * @param timestamp 时间戳
     * @return 合法请求返回true
     */
    @Override
    public boolean isNewRequest(String nonce, long timestamp) {
        long currentTime = System.currentTimeMillis();
        if (timestamp + REPLAY_ATTACK_EXPIRE < currentTime) {
            return false;
        }
        String replayKey = nonce + "-" + timestamp;
        if (replyAttackCacheWrapper.get(replayKey) == null) {
            replyAttackCacheWrapper.put(replayKey, replayKey);
            return true;
        }
        return false;
    }

    @Override
    public AccessToken login(LoginCommand command) {
        loginHandler(command);
        return tokenHandler(command.getUsername());
    }

    @Override
    public void logout(String accessToken) {
        Assert.notNull(accessToken);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        accessTokenCacheWrapper.remove(accessToken);
        LOGGER.debug("remove token: {}", accessToken);
    }


    /**
     * 获取用户的授权
     *
     * @param userId 用户id
     * @return 授权的集合
     */
    @Override
    public Set<String> getPermissions(int userId) {
        return userFacde.getPermissions(userId);
    }


    @Override
    public String getSecretKey(String accessToken) {
        AccessToken token = accessTokenCacheWrapper.get(accessToken);
        return token.getSecretKey();
    }

    @Override
    public String getUsername(String accessToken) {
        AccessToken token = accessTokenCacheWrapper.get(accessToken);
        return token.getUsername();
    }

    @Override
    public StatelessUser getUser(String accessToken) {
        String username = getUsername(accessToken);
        SysUser sysUser = userFacde.queryByUsername(username);
        StatelessUser statelessUser = new StatelessUser();
        statelessUser.setUserId(sysUser.getUserId());
        statelessUser.setUsername(sysUser.getUsername());
        statelessUser.setFullName(sysUser.getFullName());
        statelessUser.setEmail(sysUser.getEmail());
        statelessUser.setAccessToken(accessToken);
        statelessUser.setRoles(userFacde.getRolesForUser(sysUser.getUserId()));
        return statelessUser;
    }

    private void loginHandler(LoginCommand command) {
        Assert.notNull(command.getUsername());
        Assert.notNull(command.getPassword());
        String username = command.getUsername();
        String password = command.getPassword();

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,
                password);
        try {
            subject.login(token);
            LOGGER.debug("user login : {}", username);
        } catch (UnknownAccountException e) {
            throw ExceptionFactory.userOrPasswordError();
        } catch (IncorrectCredentialsException e) {
            throw ExceptionFactory.userOrPasswordError();
        } catch (AuthenticationException e) {
            throw ExceptionFactory.userOrPasswordError();
        }
    }

    private AccessToken tokenHandler(String username) {
        Assert.notNull(username);
        AccessToken accessToken = newToken(username);
        accessTokenCacheWrapper.put(accessToken.getAccessToken(), accessToken, ACCESS_TOKEN_TIME_TO_LIVE);
        refreshTokenCacheWrapper.put(accessToken.getAccessToken(), accessToken, REFRESH_TOKEN_TIME_TO_LIVE);
        LOGGER.debug("crate new token : {}", accessToken.getAccessToken());
        return accessToken;
    }

    /**
     * 创建AccessToken
     *
     * @param username 用户名
     * @return AccessToken
     */
    private AccessToken newToken(String username) {
        AccessToken token = new AccessToken();
        token.setUsername(username);
        token.setAccessToken(createToken(username, EXPIRES_IN));
        token.setRefreshToken(createToken(username, EXPIRES_IN));
        token.setSecretKey(createToken(username, EXPIRES_IN));
        token.setExpiresIn("" + EXPIRES_IN);
        token.setServerTime("" + System.currentTimeMillis());
        return token;
    }

    /**
     * 生成随机的TOKEN
     *
     * @param key       键
     * @param expiresIn 有效时间
     * @return token
     */
    private String createToken(String key, long expiresIn) {
        StringBuilder token = new StringBuilder(key);
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = randomNumberGenerator.nextBytes().toHex();
        token.append(salt);
        token.append(System.currentTimeMillis() + expiresIn);
        return new SimpleHash(Constants.TOKEN_ALGORITHM_NAME, key, ByteSource.Util.bytes(token.toString()), Constants.TOKEN_HASH_ITERATIONS).toHex();
    }

    @Override
    public AccessToken refresh(RefreshVo command) {
        String accessToken = command.getAccessToken();
        String refreshToken = command.getRefreshToken();
        Assert.hasText(accessToken);
        Assert.hasText(refreshToken);

        AccessToken serverToken = refreshTokenCacheWrapper.get(accessToken);
        if (serverToken != null && refreshToken.equals(serverToken.getRefreshToken())) {
            refreshTokenCacheWrapper.remove(accessToken);
            AccessToken token = tokenHandler(serverToken.getUsername());
            return token;
        }
        throw ExceptionFactory.unAuthorized();
    }
}
