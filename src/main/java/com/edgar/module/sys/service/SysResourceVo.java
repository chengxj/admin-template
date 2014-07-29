package com.edgar.module.sys.service;

import com.edgar.module.sys.repository.domain.SysResource;

public class SysResourceVo extends SysResource {

	private boolean checked;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
