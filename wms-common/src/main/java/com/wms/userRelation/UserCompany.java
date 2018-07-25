package com.wms.userRelation;

import com.plat.common.beans.BaseModel;

// default package

/**
 * UserCompany entity. @author MyEclipse Persistence Tools
 */

public class UserCompany extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String companyid;
	private String companyName;
	private String companyType;
	private String userId;
	private String userLoginname;
	private String whCode;

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyid() {
		return this.companyid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getUserLoginname() {
		return this.userLoginname;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserLoginname(String userLoginname) {
		this.userLoginname = userLoginname;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

}