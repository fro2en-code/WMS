package com.wms.warehouse;

import com.plat.common.beans.BaseModel;

/*
 * 库位策略实体关系
 */
public class WmsStoragLocation extends BaseModel {

	private static final long serialVersionUID = 3281283758712060911L;

	// 零件编码
	private String gcode;
	//零件名称
	private String gname;
	// 零件用途
	private String gtype;
	// 供应商编码
	private String oraCode;
	// 供应商名称
	private String oraName;
	// 库区/库位
	private String storagezoneId;
	//库区/库位的类型
	private Integer storagezoneType;
	// 仓库
	private String whCode;
	// 最大库存数
	private Integer storageMaxNum;

	
	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getOraName() {
		return oraName;
	}

	public void setOraName(String oraName) {
		this.oraName = oraName;
	}

	public String getGcode() {
		return gcode;
	}

	public void setGcode(String gcode) {
		this.gcode = gcode;
	}

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public String getOraCode() {
		return oraCode;
	}

	public void setOraCode(String oraCode) {
		this.oraCode = oraCode;
	}

	public String getStoragezoneId() {
		return storagezoneId;
	}

	public void setStoragezoneId(String storagezoneId) {
		this.storagezoneId = storagezoneId;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public Integer getStoragezoneType() {
		return storagezoneType;
	}

	public void setStoragezoneType(Integer storagezoneType) {
		this.storagezoneType = storagezoneType;
	}

	public Integer getStorageMaxNum() {
		return storageMaxNum;
	}

	public void setStorageMaxNum(Integer storageMaxNum) {
		this.storageMaxNum = storageMaxNum;
	}

}
