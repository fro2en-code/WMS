package com.wms.business;
// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

import java.util.List;

import com.plat.common.beans.BaseModel;

/**
 * WmsLesReceive generated by hbm2java
 */
public class WmsLesReceive extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String mapSheetNo;// 对应配送单号
	private Integer isEmerge;// 是否紧急需求
	private String lastRecRequrieTime;// 目的地要求到货时间
	private String mriCreateTime;// 需求创建时间
	private String lastUpdateTime;// 最后更新时间
	private String mriStatus;// 需求状态（对应到数据）
	private String mriType;// 需求类别
	private String sheetSuppRecTime;// 供应商接收时间
	private String deliveryRec;// 目的地代码（对应到数据）
	private String deliveryRecType;// 目的地类型
	private String deliverySend;// 发运地代码（对应到数据）
	private String pullType;// 配送模式（对应到数据）
	private String sheetStatus;// 配送单状态（对应到数据）
	private String plantNo;// 工厂
	private Integer status;// 收货状态0获取单据1收货登记 2 收货完成3单据取消 4单据回写
	private String whCode;// 仓库代码
	private String beginTime;// 开始时间
	private String endTime;// 结束时间
	private List<WmsLesReceiveList> wmsLesReceiveLists;

	public List<WmsLesReceiveList> getWmsLesReceiveLists() {
		return wmsLesReceiveLists;
	}

	public void setWmsLesReceiveLists(List<WmsLesReceiveList> wmsLesReceiveLists) {
		this.wmsLesReceiveLists = wmsLesReceiveLists;
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
