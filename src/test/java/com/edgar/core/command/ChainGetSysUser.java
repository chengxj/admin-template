package com.edgar.core.command;

import com.edgar.core.command.ChainCommand;
import com.edgar.core.command.Command;

public class ChainGetSysUser implements Command, ChainCommand {
        
        private int userId;

        @Override
        public Command nextCommand() {
                return new GetSysUser(userId);
        }

    public ChainGetSysUser(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
