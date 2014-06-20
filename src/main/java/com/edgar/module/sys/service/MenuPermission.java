package com.edgar.module.sys.service;

import java.util.Set;

import lombok.Data;

import com.edgar.core.command.ChainCommand;
import com.edgar.core.command.Command;
import com.edgar.core.command.UnResolvedCommand;

/**
 * 菜单授权的命令，实现了ChainCommand，声明为一个链式的command
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Data
public class MenuPermission implements Command, ChainCommand {

        /**
         * 角色ID
         */
        private int roleId;

        /**
         * 菜单ID的集合
         */
        private Set<Integer> menuIds;

        /**
         * 是否需要根据菜单路由关联设置路由
         */
        private boolean cascadeRoute;

        /**
         * 下一个命令
         */
        private Command nextCommand;

        @Override
        public Command nextCommand() {
                if (nextCommand == null) {
                        return new UnResolvedCommand();
                }
                return nextCommand;
        }

}
