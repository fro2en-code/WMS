package com.wms.warehouse;
// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

import com.plat.common.beans.BaseModel;

/**
 * 送货到达道口信息
 */
public class WmsDeliverDock extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String dockCode;
	private String dockName;
	private String gtype;
	private String whCode;

	public String getDockCode() {
		return dockCode;
	}

	public void setDockCode(String dockCode) {
		this.dockCode = dockCode;
	}

	public String getDockName() {
		return dockName;
	}

	public void setDockName(String dockName) {
		this.dockName = dockName;
	}

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
}
