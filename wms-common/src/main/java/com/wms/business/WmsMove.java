package com.wms.business;
// Generated 2017-3-23 11:13:15 by Hibernate Tools 5.2.1.Final

import com.plat.common.beans.BaseModel;

/**
 * 移库申请单
 */
public class WmsMove extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String whCode;
    private String moveCode;
    private String moveTime;
    private String moveReason;
    private String gcode;
    private String gname;
    private String supCode;
    private String supName;
    private String batchCode;
    private String boxCode;
    private String usedZoneCode;
    private String usedStorageCode;
    private String newZoneCode;
    private String newStorageCode;
    private Integer moveNum;
    private Integer statu;
    private String gtype;

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getMoveCode() {
        return moveCode;
    }

    public void setMoveCode(String moveCode) {
        this.moveCode = moveCode;
    }

    public String getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(String moveTime) {
        this.moveTime = moveTime;
    }

    public String getMoveReason() {
        return moveReason;
    }

    public void setMoveReason(String moveReason) {
        this.moveReason = moveReason;
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

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public String getUsedZoneCode() {
        return usedZoneCode;
    }

    public void setUsedZoneCode(String usedZoneCode) {
        this.usedZoneCode = usedZoneCode;
    }

    public String getUsedStorageCode() {
        return usedStorageCode;
    }

    public void setUsedStorageCode(String usedStorageCode) {
        this.usedStorageCode = usedStorageCode;
    }

    public String getNewZoneCode() {
        return newZoneCode;
    }

    public void setNewZoneCode(String newZoneCode) {
        this.newZoneCode = newZoneCode;
    }

    public String getNewStorageCode() {
        return newStorageCode;
    }

    public void setNewStorageCode(String newStorageCode) {
        this.newStorageCode = newStorageCode;
    }

    public Integer getMoveNum() {
        return moveNum;
    }

    public void setMoveNum(Integer moveNum) {
        this.moveNum = moveNum;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

    

}
