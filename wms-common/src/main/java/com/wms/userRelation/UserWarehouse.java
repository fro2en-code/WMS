package com.wms.userRelation;

import com.plat.common.beans.BaseModel;

/**
 * 人员仓库关系
 */

public class UserWarehouse extends BaseModel{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String userLoginname;
	private String whCode;
	private String whName;
	private String truename;
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserLoginname() {
		return this.userLoginname;
	}

	public void setUserLoginname(String userLoginname) {
		this.userLoginname = userLoginname;
	}

	public String getWhCode() {
		return this.whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getWhName() {
		return whName;
	}

	public void setWhName(String whName) {
		this.whName = whName;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}


	
}