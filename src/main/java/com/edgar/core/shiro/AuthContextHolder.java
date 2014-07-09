package com.edgar.core.shiro;

/**
 * 与权限有关的线程上下文
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public abstract class AuthContextHolder {

	private static ThreadLocal<Integer> RETRY_COUNT = new ThreadLocal<Integer>();

	private AuthContextHolder() {
		super();
	}

	/**
	 * 设置密码错误次数
	 * 
	 * @param count
	 *            错误次数
	 */
	public static void setRetryCount(int count) {
		RETRY_COUNT.set(count);
	}

	/**
	 * 返回错误次数
	 * 
	 * @return 错误次数
	 */
	public static int getRetryCount() {
		Integer count = RETRY_COUNT.get();
		return count == null ? 0 : count;
	}
}
