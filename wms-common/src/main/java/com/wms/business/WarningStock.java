package com.wms.business;

import com.plat.common.beans.BaseModel;

public class WarningStock extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gcode;
	private String gname;
	private String gtype;
	private String oraCode;
	private String oraName;
	private String warningMaxNum;
	private String warningMinNum;
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
	public String getOraName() {
		return oraName;
	}
	public void setOraName(String oraName) {
		this.oraName = oraName;
	}
	public String getWarningMaxNum() {
		return warningMaxNum;
	}
	public void setWarningMaxNum(String warningMaxNum) {
		this.warningMaxNum = warningMaxNum;
	}
	public String getWarningMinNum() {
		return warningMinNum;
	}
	public void setWarningMinNum(String warningMinNum) {
		this.warningMinNum = warningMinNum;
	}
	
}
