package com.edgar.module.sys.service;

import java.util.Set;

import lombok.Data;

/**
 * 菜单路由的辅助类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Data
public class MenuRouteCommand {

        private int menuId;

        private Set<Integer> routeIds;
}
