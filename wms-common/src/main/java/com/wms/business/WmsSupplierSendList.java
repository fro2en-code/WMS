package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 供应商发货单明细
 * 
 * @author Administrator
 *
 */
public class WmsSupplierSendList extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String mapSheetNo;
	private String partNo;
	private String supplNo;
	private String sxCardNo;
	private Integer sendQty;
	private String whCode;
	private Integer reqQty;

	public String getMapSheetNo() {
		return mapSheetNo;
	}

	public void setMapSheetNo(String mapSheetNo) {
		this.mapSheetNo = mapSheetNo;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getSupplNo() {
		return supplNo;
	}

	public void setSupplNo(String supplNo) {
		this.supplNo = supplNo;
	}

	public String getSxCardNo() {
		return sxCardNo;
	}

	public void setSxCardNo(String sxCardNo) {
		this.sxCardNo = sxCardNo;
	}

	public Integer getSendQty() {
		return sendQty;
	}

	public void setSendQty(Integer sendQty) {
		this.sendQty = sendQty;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public Integer getReqQty() {
		return reqQty;
	}

	public void setReqQty(Integer reqQty) {
		this.reqQty = reqQty;
	}

}
