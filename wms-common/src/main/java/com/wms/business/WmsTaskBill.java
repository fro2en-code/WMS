package com.wms.business;
// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

import java.util.List;

import com.plat.common.beans.BaseModel;

/**
 * 任务单据
 */
public class WmsTaskBill extends BaseModel {
    private static final long serialVersionUID = 1L;
    private String billid;
    private boolean input;
    private String nextStorageCode;
    private String oraCode;
    private String oraName;
    private String preBillid;
    private String remark;
    private Integer source;
    private Integer status;
    private String storageCode;
    private String taskid;
    private Integer type;
    private String whCode;
    private String billNumber;

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    private List<WmsTaskBillList> wmsTaskBillLists;

    public String getBillid() {
        return this.billid;
    }

    public String getNextStorageCode() {
        return nextStorageCode;
    }

    public String getPreBillid() {
        return this.preBillid;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    public Integer getSource() {
        return this.source;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public String getTaskid() {
        return this.taskid;
    }

    public Integer getType() {
        return this.type;
    }

    public String getWhCode() {
        return this.whCode;
    }

    public List<WmsTaskBillList> getWmsTaskBillLists() {
        return wmsTaskBillLists;
    }

    public boolean isInput() {
        return input;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public void setNextStorageCode(String nextStorageCode) {
        this.nextStorageCode = nextStorageCode;
    }

    public void setPreBillid(String preBillid) {
        this.preBillid = preBillid;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public void setWmsTaskBillLists(List<WmsTaskBillList> wmsTaskBillLists) {
        this.wmsTaskBillLists = wmsTaskBillLists;
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

}
