package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 初始化台账子表实体类
 */

public class AccountInitList extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String initCode;
    private String gcode;
    private String gname;
    private String oraCode;
    private String oraName;
    private String whCode;
    private String whName;
    private String gtype;
    private int initNum;
    private String zoneCode;
    private String storageCode;

    public String getInitCode() {
        return initCode;
    }

    public void setInitCode(String initCode) {
        this.initCode = initCode;
    }

    public String getGcode() {
        return gcode;
    }

    public void setGcode(String gcode) {
        this.gcode = gcode;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
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

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getGtype() {
        return gtype;
    }

    public void setGtype(String gtype) {
        this.gtype = gtype;
    }

    public int getInitNum() {
        return initNum;
    }

    public void setInitNum(int initNum) {
        this.initNum = initNum;
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

}