package com.wms.orginfo;

import com.plat.common.beans.BaseModel;
// default package

/**
 * OrgDepart entity. @author MyEclipse Persistence Tools
 */

public class OrgDepart extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String depCode;
	private String depName;
	private String conCode;
	private String address;
	private String contect;
	private String tel;
	private Integer statu;
	private String descrip;

	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	public String getDepName() {
		return this.depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getConCode() {
		return this.conCode;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContect() {
		return this.contect;
	}

	public void setContect(String contect) {
		this.contect = contect;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getStatu() {
		return this.statu;
	}

	public void setStatu(Integer statu) {
		this.statu = statu;
	}

	public String getDescrip() {
		return this.descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

}