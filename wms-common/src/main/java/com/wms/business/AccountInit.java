package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 初始化台账主表实体类
 */

public class AccountInit extends BaseModel{

	private static final long serialVersionUID = -7050346673519564589L;
	private String initCode;
	private String whCode;
	private String wkCode;
	private String initTime;
	private Integer status;
	
	private String whName;


	public String getInitCode() {
		return this.initCode;
	}

	public void setInitCode(String initCode) {
		this.initCode = initCode;
	}

	public String getWhCode() {
		return this.whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getWkCode() {
		return this.wkCode;
	}

	public void setWkCode(String wkCode) {
		this.wkCode = wkCode;
	}

	public String getInitTime() {
		return this.initTime;
	}

	public void setInitTime(String initTime) {
		this.initTime = initTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getWhName() {
		return whName;
	}

	public void setWhName(String whName) {
		this.whName = whName;
	}

}