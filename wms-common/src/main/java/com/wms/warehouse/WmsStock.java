package com.wms.warehouse;

import com.plat.common.beans.BaseModel;

/**
 * 库存查询实体(实物)
 */

public class WmsStock extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String whCode;
    private String zoneCode;
    private String storageCode;
    private String gcode;
    private String gname;
    private String gtype;
    private String supCode;
    private String supName;
    private String batchCode;
    private Integer quantity = 0;
    private Integer prePickNum = 0;
    private Integer lockNum = 0;
    private Integer preLock = 0;

    public Integer getPreLock() {
        return preLock;
    }

    public void setPreLock(Integer preLock) {
        this.preLock = preLock;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
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

    public String getGtype() {
        return gtype;
    }

    public void setGtype(String gtype) {
        this.gtype = gtype;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrePickNum() {
        return prePickNum;
    }

    public void setPrePickNum(Integer prePickNum) {
        this.prePickNum = prePickNum;
    }

    public Integer getLockNum() {
        return lockNum;
    }

    public void setLockNum(Integer lockNum) {
        this.lockNum = lockNum;
    }
}