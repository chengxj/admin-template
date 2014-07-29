package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.List;

import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysRoute;

public class SysMenuVo extends SysMenu {

	private final List<SysRoute> sysRoutes = new ArrayList<SysRoute>();

	public List<SysRoute> getSysRoutes() {
		return sysRoutes;
	}

	public void addRoute(SysRoute sysRoute) {
		sysRoutes.add(sysRoute);
	}
}
