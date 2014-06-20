package com.edgar.module.sys.service;

import java.util.Set;

import lombok.Data;

import com.edgar.core.command.ChainCommand;
import com.edgar.core.command.Command;
import com.edgar.core.command.UnResolvedCommand;

@Data
public class RoutePermission implements Command, ChainCommand {
        private int roleId;

        private Set<Integer> routeIds;

        private Command nextCommand;
        
        /**
         * 是否需要根据路由资源设置资源
         */
        private boolean cascadeRes;

        @Override
        public Command nextCommand() {
                if (nextCommand == null) {
                        return new UnResolvedCommand();
                }
                return nextCommand;
        }
}
