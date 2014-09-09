package com.edgar.module.sys.service.impl;

import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.service.PasswordService;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-9-9
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class MD51024ServiceImpl implements PasswordService {


    /**
     * 加密算法
     */
    private static final String algorithmName = "MD5";

    /**
     * 迭代次数
     */
    private static final int hashIterations = 1024;

    @Override
    public void encryptPassword(SysUser user) {
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String password = new SimpleHash(algorithmName, user.getPassword(),
                ByteSource.Util.bytes(user.getUsername() + user.getSalt()),
                hashIterations).toHex();
        user.setPassword(password);
    }

    @Override
    public String getEncryptPassword(String password, SysUser user) {
        return new SimpleHash(algorithmName, password, ByteSource.Util.bytes(user
                .getUsername() + user.getSalt()), hashIterations).toHex();
    }
}
