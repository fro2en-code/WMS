package com.wms.warehouse;

import com.plat.common.beans.BaseModel;

public class WmsStoragGroup extends BaseModel{

	/**
	 * 库位组信息
	 */
	private static final long serialVersionUID = 1L;
	private String whCode;
	private String groupName;
	public String getWhCode() {
		return whCode;
	}
	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}

