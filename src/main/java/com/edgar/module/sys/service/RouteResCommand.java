package com.edgar.module.sys.service;

import java.util.Set;

import lombok.Data;

/**
 * 路由资源的辅助类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Data
public class RouteResCommand {

        private int routeId;

        private Set<Integer> resourceIds;
}
