package com.wms.business;
// Generated 2017-3-23 11:13:15 by Hibernate Tools 5.2.1.Final

import com.plat.common.beans.BaseModel;

/**
 * 盘库申请表
 */
public class WmsStocktakePlan extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String takePlanCode;
    private String beginDatetime;
    private String endDatetime;
    private String takeWokerCode;
    private Integer takeType;
    private String zoneCode;
    private String storageCode;
    private String whCode;
    private String batchCode;
    private String gcode;
    private String ownerCode;
    private Integer status;

    public String getTakePlanCode() {
        return takePlanCode;
    }

    public void setTakePlanCode(String takePlanCode) {
        this.takePlanCode = takePlanCode;
    }

    public String getBeginDatetime() {
        return beginDatetime;
    }

    public void setBeginDatetime(String beginDatetime) {
        this.beginDatetime = beginDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getTakeWokerCode() {
        return takeWokerCode;
    }

    public void setTakeWokerCode(String takeWokerCode) {
        this.takeWokerCode = takeWokerCode;
    }

    public Integer getTakeType() {
        return takeType;
    }

    public void setTakeType(Integer takeType) {
        this.takeType = takeType;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getGcode() {
        return gcode;
    }

    public void setGcode(String gcode) {
        this.gcode = gcode;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
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

}
