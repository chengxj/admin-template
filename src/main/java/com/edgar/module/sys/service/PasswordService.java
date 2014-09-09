package com.edgar.module.sys.service;

import com.edgar.module.sys.repository.domain.SysUser;
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
public interface PasswordService {
    public void encryptPassword(SysUser user);

    public String getEncryptPassword(String password, SysUser user);
}
