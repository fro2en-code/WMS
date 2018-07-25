package com.wms.business;

import com.plat.common.beans.BaseModel;

public class WarningSaveOutTime extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String supCode;
	private String supName;
	private String gcode;
	private String gname;
	private String gtype;
	public String getSupCode() {
		return supCode;
	}
	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}
	public String getSupName() {
		return supName;
	}
	public void setSupName(String supName) {
		this.supName = supName;
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
	public String getGtype() {
		return gtype;
	}
	public void setGtype(String gtype) {
		this.gtype = gtype;
	}
	

}
