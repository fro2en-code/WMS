package com.wms.business;

import com.plat.common.beans.BaseModel;

public class WarningReturnOutTime extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mapSheetNo;
	private Integer isEmerge;
	private String deliveryRecType;
	private String mriCreateTime;

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

	public String getDeliveryRecType() {
		return deliveryRecType;
	}

	public void setDeliveryRecType(String deliveryRecType) {
		this.deliveryRecType = deliveryRecType;
	}

	public String getMriCreateTime() {
		return mriCreateTime;
	}

	public void setMriCreateTime(String mriCreateTime) {
		this.mriCreateTime = mriCreateTime;
	}

}
