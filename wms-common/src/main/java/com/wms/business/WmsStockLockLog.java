package com.wms.business;

import com.plat.common.beans.BaseModel;

public class WmsStockLockLog extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 5727611480542776221L;
    private String number;
    private String endTime;
    private String exector;
    private String gCode;
    private String gName;
    private String gType;
    private String oraCode;
    private String oraName;
    private String startTime;
    private String type;
    private String whCode;
    private String state;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExector() {
        return exector;
    }

    public void setExector(String exector) {
        this.exector = exector;
    }

    public String getgCode() {
        return gCode;
    }

    public void setgCode(String gCode) {
        this.gCode = gCode;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgType() {
        return gType;
    }

    public void setgType(String gType) {
        this.gType = gType;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }
}
