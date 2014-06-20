package com.edgar.core.command;
import lombok.AllArgsConstructor;
import lombok.Data;

import com.edgar.core.command.Command;

/**
 * 根据用户名查询用户的命令
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class DeleteSysUser implements Command {

        private int userId;
}