package com.wms.warehouse;

import com.plat.common.beans.BaseModel;
// default package

/**
 * 库区实体类
 */

public class WmsZone extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String zoneCode;
	private String whCode;
	private String ZName;
	private String descrip;
	private Integer statu;
	
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
	public String getZName() {
		return ZName;
	}
	public void setZName(String zName) {
		ZName = zName;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public Integer getStatu() {
		return statu;
	}
	public void setStatu(Integer statu) {
		this.statu = statu;
	}
	

}