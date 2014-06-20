package com.edgar.core.command;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.edgar.core.command.Command;
import com.edgar.core.command.CommandBus;
import com.edgar.core.command.CommandResult;
import com.edgar.core.exception.SystemException;
import com.edgar.module.sys.repository.domain.SysUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
                TransactionalTestExecutionListener.class })
public class CommandTest {

        @Autowired
        private CommandBus commandBus;

        @Test
        public void test() {
                Command command = new GetSysUser(1);
                CommandResult<SysUser> result = commandBus.executeCommand(command);
                Assert.assertNotNull(result.getResult());
        }
        
        @Test
        public void testChain() {
                Command command = new ChainGetSysUser(1);
                CommandResult<SysUser> result = commandBus.executeCommand(command);
                Assert.assertNotNull(result.getResult());
        }
        
        @Test
        public void testBatch() {
                List<Command> commands = new ArrayList<Command>();
                commands.add(new GetSysUser(1));
                commands.add(new ChainGetSysUser(1));
                CommandResult<SysUser> result = commandBus.executeCommands(commands);
                Assert.assertNotNull(result.getResult());
        }
        
        @Test(expected=SystemException.class)
        public void testError() {
                Command command = new DeleteSysUser(1);
                commandBus.executeCommand(command);
        }

}
