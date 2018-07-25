package com.wms.business;

import java.util.Date;

import com.plat.common.beans.BaseModel;

/**
 * 台账实体
 */

public class WmsAccount extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = 1861865505761228967L;

    /**
     * 台账类型 0 出 1 入
     */
    private int accountType;

    private String batchCode;

    /**
     * 对应操作单号
     */
    private String dealCode;

    /**
     * 流水数量
     */
    private int dealNum;

    /**
     * 操作时间
     */
    private Date dealTime;

    /**
     * 操作类型 0 出库 1 收货 2红冲 3初始化
     */
    private int dealType;

    /**
     * 物料编号
     */
    private String gcode;

    /**
     * 物料名称
     */
    private String gname;

    private String gtype;

    private String inTime;

    /**
     * 供应商编码
     */
    private String oraCode;

    /**
     * 供应商名称
     */
    private String oraName;

    /**
     * 库位编码
     */
    private String storageCode;

    /**
     * 完成后库存总量
     */
    private int totalNum;

    /**
     * 仓库代码
     */
    private String whCode;

    /**
     * 仓库名称
     */
    private String whName;
    private String zoneCode;

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public int getDealNum() {
        return dealNum;
    }

    public void setDealNum(int dealNum) {
        this.dealNum = dealNum;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public int getDealType() {
        return dealType;
    }

    public void setDealType(int dealType) {
        this.dealType = dealType;
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

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
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

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
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

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

}