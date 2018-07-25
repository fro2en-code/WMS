package com.wms.warehouse;

import com.plat.common.beans.BaseModel;

/**
 * 物料供应商关系实体
 */

public class WmsGoodsOraRelation extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String gcode;
	private String gname;
	private String gtype;
	private Integer warningMaxNum;
	private Integer warningMinNum;
	private String whCode;
	private String oraCode;
	private String oraName;
	private Integer storageType;
	private Integer boxType;

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public Integer getWarningMaxNum() {
		return warningMaxNum;
	}

	public void setWarningMaxNum(Integer warningMaxNum) {
		this.warningMaxNum = warningMaxNum;
	}

	public Integer getWarningMinNum() {
		return warningMinNum;
	}

	public void setWarningMinNum(Integer warningMinNum) {
		this.warningMinNum = warningMinNum;
	}

	public String getGcode() {
		return gcode;
	}

	public void setGcode(String gcode) {
		this.gcode = gcode;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getOraCode() {
		return oraCode;
	}

	public void setOraCode(String oraCode) {
		this.oraCode = oraCode;
	}

	public String getOraName() {
		return oraName;
	}

	public void setOraName(String oraName) {
		this.oraName = oraName;
	}

	public Integer getStorageType() {
		return storageType;
	}

	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}

	public Integer getBoxType() {
		return boxType;
	}

	public void setBoxType(Integer boxType) {
		this.boxType = boxType;
	}

}
