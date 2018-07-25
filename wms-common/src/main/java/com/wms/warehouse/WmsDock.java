package com.wms.warehouse;

import com.plat.common.beans.BaseModel;

// default package

/**
 * WmsDock entity. @author MyEclipse Persistence Tools
 */

public class WmsDock extends BaseModel {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dockCode;
	private String zoneCode;
	private String tagId;
	private String whCode;

	public String getDockCode() {
		return this.dockCode;
	}

	public void setDockCode(String dockCode) {
		this.dockCode = dockCode;
	}

	public String getZoneCode() {
		return this.zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

}