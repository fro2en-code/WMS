package com.wms.warehouse;

import com.plat.common.beans.BaseModel;
// default package

/**
 * WmsStorag entity. @author MyEclipse Persistence Tools
 */

public class WmsStorag extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private String storageCode;
	private String zoneCode;
	private String whCode;
	private String groupCode;
	private String lineNo;
	private String colNo;
	private String rowNo;
	private String layerNo;
	private Integer storageType;
	private Integer sType;
	private Integer layType;
	private Integer mulSup;
	private Integer statu;
	private Integer mulBth;
	private Integer gStatu;
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getWhCode() {
		return whCode;
	}
	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getColNo() {
		return colNo;
	}
	public void setColNo(String colNo) {
		this.colNo = colNo;
	}
	public String getRowNo() {
		return rowNo;
	}
	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}
	public String getLayerNo() {
		return layerNo;
	}
	public void setLayerNo(String layerNo) {
		this.layerNo = layerNo;
	}
	public Integer getStorageType() {
		return storageType;
	}
	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}
	public Integer getsType() {
		return sType;
	}
	public void setsType(Integer sType) {
		this.sType = sType;
	}
	public Integer getLayType() {
		return layType;
	}
	public void setLayType(Integer layType) {
		this.layType = layType;
	}
	public Integer getMulSup() {
		return mulSup;
	}
	public void setMulSup(Integer mulSup) {
		this.mulSup = mulSup;
	}
	public Integer getStatu() {
		return statu;
	}
	public void setStatu(Integer statu) {
		this.statu = statu;
	}
	public Integer getMulBth() {
		return mulBth;
	}
	public void setMulBth(Integer mulBth) {
		this.mulBth = mulBth;
	}
	public Integer getgStatu() {
		return gStatu;
	}
	public void setgStatu(Integer gStatu) {
		this.gStatu = gStatu;
	}
	
	
}