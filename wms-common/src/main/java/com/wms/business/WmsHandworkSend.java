package com.wms.business;

import java.util.List;

import com.plat.common.beans.BaseModel;

public class WmsHandworkSend extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String mapSheetNo;
	private Long isEmerge;
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
	private Integer isReturn;
	private String remark;
	private String originalNo;
	private String beginTime;
	private String endTime;
	private String gtype;
	private List<WmsHandworkSendList> wmsHandworkSendList;

	public Integer getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public List<WmsHandworkSendList> getWmsHandworkSendList() {
		return wmsHandworkSendList;
	}

	public void setWmsHandworkSendList(List<WmsHandworkSendList> wmsHandworkSendList) {
		this.wmsHandworkSendList = wmsHandworkSendList;
	}

	public String getMapSheetNo() {
		return mapSheetNo;
	}

	public void setMapSheetNo(String mapSheetNo) {
		this.mapSheetNo = mapSheetNo;
	}

	public Long getIsEmerge() {
		return isEmerge;
	}

	public void setIsEmerge(Long isEmerge) {
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOriginalNo() {
		return originalNo;
	}

	public void setOriginalNo(String originalNo) {
		this.originalNo = originalNo;
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

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

}
