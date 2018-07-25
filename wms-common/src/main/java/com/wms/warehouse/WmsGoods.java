package com.wms.warehouse;

import java.math.BigDecimal;

import com.plat.common.beans.BaseModel;

/**
 * 物料实体类
 */

public class WmsGoods extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String gcode;
	private String gname;
	private String oraCode;
	private String oraName;
	private String whCode;
	private String whName;
	private String gtype;
	private String chineseDes;
	private String englishDes;
	private String abcType;
	private Integer qualityDay;
	private BigDecimal gValue;
	private String sendBoxid;
	private Integer boxType;
	private Integer boxNum;
	private Integer batchType;
	private Integer storageType;
	private Integer trayNum;
	private Integer receivetrayNum;
	private Integer storagezoneType;
	private String storagezoneId;
	private Integer repleniShment;
	private Integer moveNum;
	private Integer maxNum;
	private Integer storageMaxNum;
	private Integer forkliftNum;
	private Integer skipPutaway;
	private String innerCoding;
	private Integer packLength;
	private Integer packWidth;
	private Integer packHeigth;
	private Integer packWeigth;
	private Integer warningMaxNum;
	private Integer warningMinNum;
	private String units;

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

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public String getChineseDes() {
		return chineseDes;
	}

	public void setChineseDes(String chineseDes) {
		this.chineseDes = chineseDes;
	}

	public String getEnglishDes() {
		return englishDes;
	}

	public void setEnglishDes(String englishDes) {
		this.englishDes = englishDes;
	}

	public String getAbcType() {
		return abcType;
	}

	public void setAbcType(String abcType) {
		this.abcType = abcType;
	}

	public Integer getQualityDay() {
		return qualityDay;
	}

	public void setQualityDay(Integer qualityDay) {
		this.qualityDay = qualityDay;
	}

	public BigDecimal getgValue() {
		return gValue;
	}

	public void setgValue(BigDecimal gValue) {
		this.gValue = gValue;
	}

	public String getSendBoxid() {
		return sendBoxid;
	}

	public void setSendBoxid(String sendBoxid) {
		this.sendBoxid = sendBoxid;
	}

	public Integer getBoxType() {
		return boxType;
	}

	public void setBoxType(Integer boxType) {
		this.boxType = boxType;
	}

	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	public Integer getBatchType() {
		return batchType;
	}

	public void setBatchType(Integer batchType) {
		this.batchType = batchType;
	}

	public Integer getStorageType() {
		return storageType;
	}

	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}

	public Integer getTrayNum() {
		return trayNum;
	}

	public void setTrayNum(Integer trayNum) {
		this.trayNum = trayNum;
	}

	public Integer getReceivetrayNum() {
		return receivetrayNum;
	}

	public void setReceivetrayNum(Integer receivetrayNum) {
		this.receivetrayNum = receivetrayNum;
	}

	public Integer getStoragezoneType() {
		return storagezoneType;
	}

	public void setStoragezoneType(Integer storagezoneType) {
		this.storagezoneType = storagezoneType;
	}

	public String getStoragezoneId() {
		return storagezoneId;
	}

	public void setStoragezoneId(String storagezoneId) {
		this.storagezoneId = storagezoneId;
	}

	public Integer getRepleniShment() {
		return repleniShment;
	}

	public void setRepleniShment(Integer repleniShment) {
		this.repleniShment = repleniShment;
	}

	public Integer getMoveNum() {
		return moveNum;
	}

	public void setMoveNum(Integer moveNum) {
		this.moveNum = moveNum;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public Integer getStorageMaxNum() {
		return storageMaxNum;
	}

	public void setStorageMaxNum(Integer storageMaxNum) {
		this.storageMaxNum = storageMaxNum;
	}

	public Integer getForkliftNum() {
		return forkliftNum;
	}

	public void setForkliftNum(Integer forkliftNum) {
		this.forkliftNum = forkliftNum;
	}

	public Integer getSkipPutaway() {
		return skipPutaway;
	}

	public void setSkipPutaway(Integer skipPutaway) {
		this.skipPutaway = skipPutaway;
	}

	public String getInnerCoding() {
		return innerCoding;
	}

	public void setInnerCoding(String innerCoding) {
		this.innerCoding = innerCoding;
	}

	public Integer getPackLength() {
		return packLength;
	}

	public void setPackLength(Integer packLength) {
		this.packLength = packLength;
	}

	public Integer getPackWidth() {
		return packWidth;
	}

	public void setPackWidth(Integer packWidth) {
		this.packWidth = packWidth;
	}

	public Integer getPackHeigth() {
		return packHeigth;
	}

	public void setPackHeigth(Integer packHeigth) {
		this.packHeigth = packHeigth;
	}

	public Integer getPackWeigth() {
		return packWeigth;
	}

	public void setPackWeigth(Integer packWeigth) {
		this.packWeigth = packWeigth;
	}

	public Integer getWarningMaxNum() {
		return warningMaxNum;
	}

	public void setWarningMaxNum(Integer warningMaxNum) {
		this.warningMaxNum = warningMaxNum;
	}

	public Integer getWarningMinNum() {
		return warningMinNum;
	}

	public void setWarningMinNum(Integer warningMinNum) {
		this.warningMinNum = warningMinNum;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

}