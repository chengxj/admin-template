package com.edgar.core.shiro;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.Map;

public class TokenManager {

    /**
     * 存储token的MAP，在实际应用用应该使用缓存来代替
     */
    private Map<String, String> map = new HashMap<String, String>();

    /**
     * 加密算法
     */
    private static final String algorithmName = "MD5";

    /**
     * 迭代次数
     */
    private static final int hashIterations = 1024;

    public static Token newToken(String username) {

        Token token = new Token();
        token.setAccessToken(createToken(username, 30 * 3600 * 1000));
        token.setRefreshToken(createToken(username, 60 * 3600 * 1000));
        token.setSessionSecret(createToken(username, 30 * 3600 * 1000));
        token.setExpiresIn(30 * 3600 * 1000);
        return token;
    }

    public static String createToken(String username, long expiresIn) {
        StringBuilder token = new StringBuilder(username);
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = randomNumberGenerator.nextBytes().toHex();
        token.append(salt);
        token.append(System.currentTimeMillis() + expiresIn);
        return new SimpleHash(algorithmName, username, ByteSource.Util.bytes(token.toString()), hashIterations).toHex();
    }
}
