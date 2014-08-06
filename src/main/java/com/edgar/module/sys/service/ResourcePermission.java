package com.edgar.module.sys.service;

import java.util.Set;

import lombok.Data;

import com.edgar.core.command.Command;

@Data
public class ResourcePermission implements Command {
        private int roleId;
        
        private Set<Integer> resourceIds;
}
