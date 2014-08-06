package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.List;

import com.edgar.module.sys.repository.domain.SysMenu;
import com.edgar.module.sys.repository.domain.SysResource;
import com.edgar.module.sys.repository.domain.SysRoute;

public class SysMenuVo extends SysMenu {

	private final List<SysMenu> children = new ArrayList<SysMenu>();

	private final List<SysRoute> routes = new ArrayList<SysRoute>();

	private final List<SysResource> resources = new ArrayList<SysResource>();

	private List<Integer> routeIds = new ArrayList<Integer>();
	
	private List<Integer> resourceIds = new ArrayList<Integer>();

	public void addResource(SysResource sysResource) {
		resources.add(sysResource);
	}

	public void addRoute(SysRoute sysRoute) {
		routes.add(sysRoute);
	}

	public void addChild(SysMenu sysMenu) {
		children.add(sysMenu);
	}

	public List<SysMenu> getChildren() {
		return children;
	}

	public List<SysRoute> getRoutes() {
		return routes;
	}

	public List<SysResource> getResources() {
		return resources;
	}

	public List<Integer> getRouteIds() {
		return routeIds;
	}

	public void setRouteIds(List<Integer> routeIds) {
		this.routeIds = routeIds;
	}

	public List<Integer> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(List<Integer> resourceIds) {
		this.resourceIds = resourceIds;
	}
}
