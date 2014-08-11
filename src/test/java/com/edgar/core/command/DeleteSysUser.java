package com.edgar.core.command;


import com.edgar.core.command.Command;

/**
 * 根据用户名查询用户的命令
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class DeleteSysUser implements Command {

        private int userId;

    public DeleteSysUser(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}