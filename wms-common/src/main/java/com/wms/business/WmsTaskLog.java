package com.wms.business;
// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

import com.plat.common.beans.BaseModel;

/**
 * 库内任务历史表	
 */
public class WmsTaskLog extends BaseModel {

	private static final long serialVersionUID = 1L;
    private String billid;
    private String whCode;
    private Integer type;
    private String beginTime;
    private String endTime;
    private Integer status;
    private String updateUser;




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

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

}
