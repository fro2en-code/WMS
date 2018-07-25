package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 拣货单实体
 */

public class WmsPick extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	private String pickCode;
	private String otaskCode;
	private String whCode;
	private String wkCode;
	private String pickTime;
	private Integer status;
	
	private String whName;

	public String getPickCode() {
		return this.pickCode;
	}

	public void setPickCode(String pickCode) {
		this.pickCode = pickCode;
	}

	public String getOtaskCode() {
		return this.otaskCode;
	}

	public void setOtaskCode(String otaskCode) {
		this.otaskCode = otaskCode;
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

	public String getPickTime() {
		return this.pickTime;
	}

	public void setPickTime(String pickTime) {
		this.pickTime = pickTime;
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