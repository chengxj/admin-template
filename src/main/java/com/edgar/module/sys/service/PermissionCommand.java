package com.edgar.module.sys.service;

import java.util.Set;

import lombok.Data;

@Data
public class PermissionCommand {
	private int roleId;

	private Set<Integer> permissionIds;
}
