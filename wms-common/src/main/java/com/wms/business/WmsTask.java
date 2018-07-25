package com.wms.business;

import com.plat.common.beans.BaseModel;

// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

/**
 * 库内任务表
 */
public class WmsTask extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String taskdesc;
    private String billid;
    private String whCode;
    private Integer level;
    private Integer type;
    private String beginTime;
    private String acceptTime;
    private String endTime;
    private Integer status;
    private String executorId;
    private String executorName;
    private WmsTaskBill wmsTaskBill;
    //
    private String oraCode;
    private String oraName;
    private String storageCode;
    private String nextStorageCode;
    private String billNumber;

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getOraCode() {
        return oraCode;
    }

    public void setOraCode(String oraCode) {
        this.oraCode = oraCode;
    }

    public String getOraName() {
        return oraName;
    }

    public void setOraName(String oraName) {
        this.oraName = oraName;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getNextStorageCode() {
        return nextStorageCode;
    }

    public void setNextStorageCode(String nextStorageCode) {
        this.nextStorageCode = nextStorageCode;
    }

    public WmsTaskBill getWmsTaskBill() {
        return wmsTaskBill;
    }

    public void setWmsTaskBill(WmsTaskBill wmsTaskBill) {
        this.wmsTaskBill = wmsTaskBill;
    }

    public String getTaskdesc() {
        return this.taskdesc;
    }

    public void setTaskdesc(String taskdesc) {
        this.taskdesc = taskdesc;
    }

    public String getBillid() {
        return this.billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getWhCode() {
        return this.whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getAcceptTime() {
        return this.acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExecutorId() {
        return this.executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getExecutorName() {
        return this.executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

}
