package com.edgar.core.validator;

import com.edgar.module.sys.service.SysUserService;
import com.edgar.module.sys.vo.SysUserRoleVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class ValidatoredTest {

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void testValid() {
        SysUserRoleVo sysUserRoleVo = new SysUserRoleVo();
        sysUserService.save(sysUserRoleVo);
    }
}
