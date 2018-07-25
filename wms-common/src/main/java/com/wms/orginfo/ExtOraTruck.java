package com.wms.orginfo;

import com.plat.common.beans.BaseModel;

/**
 * 车辆信息
 */

public class ExtOraTruck extends BaseModel {

	private static final long serialVersionUID = 1L;
	// Fields
	private String companyid;// 所属公司编码
	private String plateInd;// 车牌号码
	private String identity;// 行驶证号码
	private String firstPilot;// 主驾驶员
	private String secondPilot;// 副驾驶员
	private String phoneNumber;// 驾驶员电话号码
	private String whCode;

	public String getPlateInd() {
		return this.plateInd;
	}

	public void setPlateInd(String plateInd) {
		this.plateInd = plateInd;
	}

	public String getIdentity() {
		return this.identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getFirstPilot() {
		return firstPilot;
	}

	public void setFirstPilot(String firstPilot) {
		this.firstPilot = firstPilot;
	}

	public String getSecondPilot() {
		return secondPilot;
	}

	public void setSecondPilot(String secondPilot) {
		this.secondPilot = secondPilot;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

}