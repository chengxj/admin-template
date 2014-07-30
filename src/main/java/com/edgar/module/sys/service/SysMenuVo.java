package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.List;

import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysResource;

public class SysMenuVo extends SysMenu {

	private final List<SysResource> sysResources = new ArrayList<SysResource>();

	public List<SysResource> getSysResources() {
		return sysResources;
	}

	public void addResource(SysResource sysResource) {
		sysResources.add(sysResource);
	}
}
