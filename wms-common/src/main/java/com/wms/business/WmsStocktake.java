package com.wms.business;
// Generated 2017-3-23 11:13:15 by Hibernate Tools 5.2.1.Final

import com.plat.common.beans.BaseModel;

/**
 * 盘库清单
 */
public class WmsStocktake extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String takeWokerCode;
	private String takePlanCode;
	private Integer lineNum;
	private String storageCode;
	private String gcode;
	private String gname;
	private String gtype;
	private String batchCode;
	private Integer quantity;
	private Integer actQuantity;
	private Integer statu;
	private String whCode;
	private String oraCode;

	public String getTakeWokerCode() {
		return takeWokerCode;
	}

	public void setTakeWokerCode(String takeWokerCode) {
		this.takeWokerCode = takeWokerCode;
	}

	public String getTakePlanCode() {
		return takePlanCode;
	}

	public void setTakePlanCode(String takePlanCode) {
		this.takePlanCode = takePlanCode;
	}

	public Integer getLineNum() {
		return lineNum;
	}

	public void setLineNum(Integer lineNum) {
		this.lineNum = lineNum;
	}

	public String getStorageCode() {
		return storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
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

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getActQuantity() {
		return actQuantity;
	}

	public void setActQuantity(Integer actQuantity) {
		this.actQuantity = actQuantity;
	}

	public Integer getStatu() {
		return statu;
	}

	public void setStatu(Integer statu) {
		this.statu = statu;
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

}
