package com.edgar.core.command;

import com.edgar.core.command.ChainCommand;
import com.edgar.core.command.Command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChainGetSysUser implements Command, ChainCommand {
        
        private int userId;

        @Override
        public Command nextCommand() {
                return new GetSysUser(userId);
        }

}
