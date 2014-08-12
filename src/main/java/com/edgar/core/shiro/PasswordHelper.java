package com.edgar.core.shiro;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.edgar.module.sys.repository.domain.SysUser;

/**
 * 密码加密的工具类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class PasswordHelper {

        /**
         * 加密算法
         */
        private static final String algorithmName = "MD5";

        /**
         * 迭代次数
         */
        private static final int hashIterations = 1024;

        /**
         * 加密
         * 
         * @param user
         *                系统用户
         */
        public static void encryptPassword(SysUser user) {
                RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
                user.setSalt(randomNumberGenerator.nextBytes().toHex());
                String password = new SimpleHash(algorithmName, user.getPassword(),
                                ByteSource.Util.bytes(user.getUsername() + user.getSalt()),
                                hashIterations).toHex();
                user.setPassword(password);
        }

        public static String getEncryptPassword(String password, SysUser user) {
                return new SimpleHash(algorithmName, password, ByteSource.Util.bytes(user
                                .getUsername() + user.getSalt()), hashIterations).toHex();
        }

}