package com.edgar.core.workflow;

import lombok.Data;

@Data
public class ActivityImpl {

	/**
	 * 唯一值
	 */
	private String code;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 流程类型：自动和手动
	 */
	private String type;
	
	/**
	 * 流程的处理类
	 */
	private String handler;
}
