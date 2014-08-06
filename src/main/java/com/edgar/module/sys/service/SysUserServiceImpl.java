package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.shiro.PasswordHelper;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.core.util.GlobalUtils;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.SysRole;
import com.edgar.module.sys.repository.domain.SysUser;
import com.edgar.module.sys.repository.domain.SysUserProfile;
import com.edgar.module.sys.repository.domain.SysUserRole;
import com.edgar.module.sys.validator.PasswordValidator;
import com.edgar.module.sys.validator.SysUserUpdateValidator;
import com.edgar.module.sys.validator.SysUserValidator;

/**
 * 用户的业务逻辑实现类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	@Setter
	private CrudRepository<Integer, SysUser> sysUserDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysRole> sysRoleDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysUserRole> sysUserRoleDao;

	@Autowired
	@Setter
	private CrudRepository<Integer, SysUserProfile> sysUserProfileDao;

	private ValidatorStrategy validator = new SysUserValidator();

	private ValidatorStrategy updateValidator = new SysUserUpdateValidator();

	@Override
	@Transactional
	public int saveAdminUser(SysUser sysUser) {
		validator.validator(sysUser);
		sysUser.setUserId(IDUtils.getNextId());
		sysUser.setIsRoot(false);
		PasswordHelper.encryptPassword(sysUser);
		int result = sysUserDao.insert(sysUser);

		QueryExample example = QueryExample.newInstance();
		example.equalsTo("roleCode", "ROLE_CODE_ADMIN");
		example.addField("roleId");
		List<Integer> roleIds = sysRoleDao.querySingleColumn(example,
				Integer.class);
		if (CollectionUtils.isNotEmpty(roleIds)) {
			List<SysUserRole> sysUserRoles = new ArrayList<SysUserRole>();
			for (Integer roleId : roleIds) {
				SysUserRole sysUserRole = new SysUserRole();
				sysUserRole.setUserRoleId(IDUtils.getNextId());
				sysUserRole.setUserId(sysUser.getUserId());
				sysUserRole.setRoleId(roleId);
				sysUserRoles.add(sysUserRole);
			}
			sysUserRoleDao.insert(sysUserRoles);
		}

		saveDefaultProfile(sysUser.getUserId());
		return result;
	}

	@Override
	@Transactional
	public int save(SysUserRoleCommand sysUser) {
		validator.validator(sysUser);
		sysUser.setUserId(IDUtils.getNextId());
		sysUser.setIsRoot(false);
		PasswordHelper.encryptPassword(sysUser);
		int result = sysUserDao.insert(sysUser);
		insertSysUserRoles(sysUser);
		saveDefaultProfile(sysUser.getUserId());
		return result;
	}

	@Override
	public SysUser get(int userId) {
		SysUser sysUser = sysUserDao.get(userId);
		sysUser.setPassword(null);
		return sysUser;
	}

	@Override
	public Pagination<SysUser> pagination(QueryExample example, int page,
			int pageSize) {
		example.equalsTo("isRoot", 0);
		return sysUserDao.pagination(example, page, pageSize);
	}

	@Override
	@Transactional
	public int update(SysUserRoleCommand sysUser) {
		updateValidator.validator(sysUser);
		if (StringUtils.isNotBlank(sysUser.getPassword())) {
			PasswordHelper.encryptPassword(sysUser);
		}
		int result = sysUserDao.update(sysUser);
		deleteRoleByUser(sysUser.getUserId());
		insertSysUserRoles(sysUser);
		return result;
	}

	@Override
	@Transactional
	public int deleteWithLock(int userId, long updatedTime) {
		deleteRoleByUser(userId);
		deleteProfile(userId);
		return sysUserDao.deleteByPkAndVersion(userId, updatedTime);
	}

	@Override
	public boolean checkUsername(String username) {
		List<SysUser> sysUsers = queryByUsername(username);
		if (sysUsers.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public List<SysUser> queryByUsername(String username) {
		Assert.notNull(username);
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("username", username);
		return sysUserDao.query(example);
	}

	@Override
	public List<SysUserRole> getRoles(int userId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("userId", userId);
		return sysUserRoleDao.query(example);
	}

	/**
	 * 根据用户删除角色
	 * 
	 * @param userId
	 *            用户ID
	 */
	@Transactional
	private void deleteRoleByUser(int userId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("userId", userId);
		sysUserRoleDao.delete(example);
	}

	/**
	 * 新增用户角色
	 * 
	 * @param sysUser
	 *            用户角色
	 */
	@Transactional
	private void insertSysUserRoles(SysUserRoleCommand sysUser) {
		if (StringUtils.isNotBlank(sysUser.getRoleIds())) {
			List<SysUserRole> sysUserRoles = new ArrayList<SysUserRole>();
			String[] roleIds = StringUtils.split(sysUser.getRoleIds(), ",");
			for (String roleId : roleIds) {
				SysUserRole sysUserRole = new SysUserRole();
				sysUserRole.setUserRoleId(IDUtils.getNextId());
				sysUserRole.setUserId(sysUser.getUserId());
				sysUserRole.setRoleId(NumberUtils.toInt(roleId));
				sysUserRoles.add(sysUserRole);
			}
			sysUserRoleDao.insert(sysUserRoles);
		}
	}

	@Override
	public SysUserProfile getProfile(int userId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("userId", userId);
		return sysUserProfileDao.uniqueResult(example);
	}

	@Override
	public int updateProfile(SysUserProfile profile) {
		return sysUserProfileDao.update(profile);
	}

	/**
	 * 保存默认profile
	 * 
	 * @return 如果保存成功，返回1
	 */
	private int saveDefaultProfile(int userId) {
		SysUserProfile profile = new SysUserProfile();
		profile.setLanguage(GlobalUtils.DEFAULT_PROFILE_LANG);
		profile.setProfileId(IDUtils.getNextId());
		profile.setUserId(userId);
		return sysUserProfileDao.insert(profile);
	}

	/**
	 * 根据用户ID，删除profile
	 * 
	 * @param userId
	 *            用户ID
	 */
	private void deleteProfile(int userId) {
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("userId", userId);
		sysUserProfileDao.delete(example);
	}

	@Override
	public int updatePassword(PasswordCommand command) {
		ValidatorStrategy validator = new PasswordValidator();
		validator.validator(command);
		if (!command.getNewpassword().equals(command.getRetypepassword())) {
			throw ExceptionFactory
					.inValidParameter("msg.error.twoPasswordNotEquals");
		}
		SysUser oldUser = sysUserDao.get(command.getUserId());
		String oldPassword = command.getOldpassword();

		String encryptPassword = PasswordHelper.getEncryptPassword(oldPassword,
				oldUser);
		if (!encryptPassword.equals(oldUser.getPassword())) {
			throw ExceptionFactory
					.inValidParameter("msg.error.passwordInit.error");
		}
		SysUser sysUser = new SysUser();
		sysUser.setPassword(command.getNewpassword());
		sysUser.setUserId(command.getUserId());
		sysUser.setUsername(oldUser.getUsername());
		PasswordHelper.encryptPassword(sysUser);
		return sysUserDao.update(sysUser);
	}
}
