package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 供应商发货单
 * 
 * @author Administrator
 *
 */
public class WmsSupplierSend extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String mapSheetNo;
	private Integer isEmerge;
	private Integer status;
	private String whCode;
	private String supplNo;

	public String getMapSheetNo() {
		return mapSheetNo;
	}

	public void setMapSheetNo(String mapSheetNo) {
		this.mapSheetNo = mapSheetNo;
	}

	public Integer getIsEmerge() {
		return isEmerge;
	}

	public void setIsEmerge(Integer isEmerge) {
		this.isEmerge = isEmerge;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getSupplNo() {
		return supplNo;
	}

	public void setSupplNo(String supplNo) {
		this.supplNo = supplNo;
	}

}
