package com.wms.business;
// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

import java.util.List;

import com.plat.common.beans.BaseModel;

/**
 * WmsHandworkReceive generated by hbm2java
 */
public class WmsHandworkReceive extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String mapSheetNo;
	private Integer isEmerge;
	private String lastRecRequrieTime;
	private String mriCreateTime;
	private String lastUpdateTime;
	private String mriStatus;
	private String mriType;
	private String sheetSuppRecTime;
	private String deliveryRec;
	private String deliveryRecType;
	private String deliverySend;
	private String pullType;
	private String sheetStatus;
	private String plantNo;
	private Integer status;
	private String whCode;
	private String beginTime;
	private String endTime;
	private List<WmsHandworkReceiveList> wmsHandworkReceiveList;

	public List<WmsHandworkReceiveList> getWmsHandworkReceiveList() {
		return wmsHandworkReceiveList;
	}

	public void setWmsHandworkReceiveList(List<WmsHandworkReceiveList> wmsHandworkReceiveList) {
		this.wmsHandworkReceiveList = wmsHandworkReceiveList;
	}

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

	public String getLastRecRequrieTime() {
		return lastRecRequrieTime;
	}

	public void setLastRecRequrieTime(String lastRecRequrieTime) {
		this.lastRecRequrieTime = lastRecRequrieTime;
	}

	public String getMriCreateTime() {
		return mriCreateTime;
	}

	public void setMriCreateTime(String mriCreateTime) {
		this.mriCreateTime = mriCreateTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getMriStatus() {
		return mriStatus;
	}

	public void setMriStatus(String mriStatus) {
		this.mriStatus = mriStatus;
	}

	public String getMriType() {
		return mriType;
	}

	public void setMriType(String mriType) {
		this.mriType = mriType;
	}

	public String getSheetSuppRecTime() {
		return sheetSuppRecTime;
	}

	public void setSheetSuppRecTime(String sheetSuppRecTime) {
		this.sheetSuppRecTime = sheetSuppRecTime;
	}

	public String getDeliveryRec() {
		return deliveryRec;
	}

	public void setDeliveryRec(String deliveryRec) {
		this.deliveryRec = deliveryRec;
	}

	public String getDeliveryRecType() {
		return deliveryRecType;
	}

	public void setDeliveryRecType(String deliveryRecType) {
		this.deliveryRecType = deliveryRecType;
	}

	public String getDeliverySend() {
		return deliverySend;
	}

	public void setDeliverySend(String deliverySend) {
		this.deliverySend = deliverySend;
	}

	public String getPullType() {
		return pullType;
	}

	public void setPullType(String pullType) {
		this.pullType = pullType;
	}

	public String getSheetStatus() {
		return sheetStatus;
	}

	public void setSheetStatus(String sheetStatus) {
		this.sheetStatus = sheetStatus;
	}

	public String getPlantNo() {
		return plantNo;
	}

	public void setPlantNo(String plantNo) {
		this.plantNo = plantNo;
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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
