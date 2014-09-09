package com.edgar.module.sys.service.impl;

import java.util.List;

import com.edgar.module.sys.service.SysCompanyCommand;
import com.edgar.module.sys.service.SysCompanyService;
import com.edgar.module.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.module.sys.repository.domain.SysCompany;
import com.edgar.module.sys.repository.domain.SysUser;

@Service
public class SysCompanyServiceImpl implements SysCompanyService {

	@Autowired
	private CrudRepository<Integer, SysCompany> sysCompanyDao;

	@Autowired
	private SysUserService sysUserService;

	@Override
	@Transactional
	public void save(SysCompanyCommand command) {
		SysCompany sysCompany = new SysCompany();
		sysCompany.setCompanyId(IDUtils.getNextId());
		sysCompany.setIsDel(false);
		sysCompany.setCompanyCode(command.getCompanyCode());
		sysCompany.setCompanyName(command.getCompanyName());
		sysCompanyDao.insert(sysCompany);

		SysUser sysUser = new SysUser();
		sysUser.setUsername(command.getUsername());
		sysUser.setEmail(command.getEmail());
		sysUser.setFullName(command.getFullName());
		sysUser.setPassword(command.getPassword());

		sysUserService.saveAdminUser(sysUser);
	}

	@Override
	public Pagination<SysCompany> pagination(QueryExample example, int page,
			int pageSize) {
		example.equalsTo("isDel", 0);
		return sysCompanyDao.pagination(example, page, pageSize);
	}

	@Override
	public void delete(int companyId) {
		SysCompany company = new SysCompany();
		company.setCompanyId(companyId);
		company.setIsDel(true);
		sysCompanyDao.update(company);
	}

	@Override
	public boolean checkCode(String code) {
		Assert.notNull(code);
		QueryExample example = QueryExample.newInstance();
		example.equalsTo("companyCode", code);

		List<SysCompany> sysCompanies = sysCompanyDao.query(example);
        return sysCompanies.isEmpty();
    }

}
