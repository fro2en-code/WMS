	package com.wms.userRelation;

import com.plat.common.beans.BaseModel;

public class UserDatagridColumn extends BaseModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String column;
	private String whCode;
	private String username;

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

}