package com.edgar.module.sys.service;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.repository.domain.SysUserProfile;
import com.edgar.module.sys.repository.domain.SysUserRole;

/**
 * 用户的业务逻辑接口
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Validated
public interface SysUserService {
	/**
	 * 新增用户
	 * 
	 * @param sysUser
	 *            用户
	 * @return 如果保存成功，返回<code>1</code>，保存失败，返回<code>0</code>或者抛出
	 *         {@link SystemException}
	 */
	int save(@NotNull SysUserRoleCommand sysUser);

	/**
	 * 根据用户ID查询用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户
	 */
	SysUser get(@Min(1) int userId);

	/**
	 * 分页查询用户
	 * 
	 * @param example
	 *            查询条件
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return 用户的分页类
	 */
	@NotNull
	Pagination<SysUser> pagination(@NotNull QueryExample example,
			@Min(1) int page, @Min(1) int pageSize);

	/**
	 * 修改用户
	 * 
	 * @param sysUser
	 *            用户
	 * @return 如果保存成功，返回<code>1</code>，保存失败，返回<code>0</code>或者抛出
	 *         {@link SystemException}
	 */
	int update(@NotNull SysUserRoleCommand sysUser);

	/**
	 * 事件用户ID和时间戳删除用户
	 * 
	 * @param userId
	 *            用户ID
	 * @param updatedTime
	 *            时间戳
	 * @return 如果删除成功，返回<code>1</code>，保存失败，返回<code>0</code>或者抛出
	 *         {@link SystemException}
	 */
	int deleteWithLock(@Min(1) int userId, long updatedTime);

	/**
	 * 检查用户名是否存在
	 * 
	 * @param username
	 *            用户名
	 * @return 如果存在，返回false
	 */
	boolean checkUsername(@NotNull String username);

	/**
	 * 根据用户ID，查询用户角色
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户角色的集合
	 */
	List<SysUserRole> getRoles(@Min(1) int userId);

	/**
	 * 根据用户名查询用户列表
	 * 
	 * @param username
	 *            用户名
	 * @return 用户的集合
	 */
	List<SysUser> queryByUsername(String username);

	/**
	 * 查询用户的个人偏好设置
	 * 
	 * @param userId
	 *            用户ID
	 * @return 个人偏好设置
	 */
	SysUserProfile getProfile(int userId);

	/**
	 * 更新用户的profile
	 * 
	 * @param profile
	 * @return 保存成功，返回1
	 */
	int updateProfile(SysUserProfile profile);

	/**
	 * 修改用户密码
	 * 
	 * @param password
	 *            密码对象
	 * @return 保存成功，返回1
	 */
	int updatePassword(PasswordCommand password);

	/**
	 * 创建管理员角色的用户
	 * 
	 * @param sysUser
	 *            用户
	 * @return 保存成功，返回1
	 */
	int saveAdminUser(SysUser sysUser);

}
