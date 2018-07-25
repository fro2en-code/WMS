package com.wms.warehouse;

import com.plat.common.beans.BaseModel;
// default package

/**
 * 仓库实体类
 */

public class WmsWarehouse extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private String whCode;
	private String whName;
	private String photo;
	private Integer useArea;
	private String WCountry;
	private String WProvince;
	private String WCity;
	private String WAddress;
	private String WPostcode;
	private String WContact;
	private String WMobile;
	private String WPhone;
	private String WFax;
	private String WEmail;
	private Float longitude;
	private Float latitude;
	private String descrip;
	private String companyid;
	private String centerid;
	private String startTime;
	private String expiredTime;
	private String receivebeginTime;
	private String receiveendTime;
	private String sendbeginTime;
	private String sendendTime;
	private String sendStorage;
	private String spillZone;

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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getUseArea() {
		return useArea;
	}

	public void setUseArea(Integer useArea) {
		this.useArea = useArea;
	}

	public String getWCountry() {
		return WCountry;
	}

	public void setWCountry(String wCountry) {
		WCountry = wCountry;
	}

	public String getWProvince() {
		return WProvince;
	}

	public void setWProvince(String wProvince) {
		WProvince = wProvince;
	}

	public String getWCity() {
		return WCity;
	}

	public void setWCity(String wCity) {
		WCity = wCity;
	}

	public String getWAddress() {
		return WAddress;
	}

	public void setWAddress(String wAddress) {
		WAddress = wAddress;
	}

	public String getWPostcode() {
		return WPostcode;
	}

	public void setWPostcode(String wPostcode) {
		WPostcode = wPostcode;
	}

	public String getWContact() {
		return WContact;
	}

	public void setWContact(String wContact) {
		WContact = wContact;
	}

	public String getWMobile() {
		return WMobile;
	}

	public void setWMobile(String wMobile) {
		WMobile = wMobile;
	}

	public String getWPhone() {
		return WPhone;
	}

	public void setWPhone(String wPhone) {
		WPhone = wPhone;
	}

	public String getWFax() {
		return WFax;
	}

	public void setWFax(String wFax) {
		WFax = wFax;
	}

	public String getWEmail() {
		return WEmail;
	}

	public void setWEmail(String wEmail) {
		WEmail = wEmail;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getCenterid() {
		return centerid;
	}

	public void setCenterid(String centerid) {
		this.centerid = centerid;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getReceivebeginTime() {
		return receivebeginTime;
	}

	public void setReceivebeginTime(String receivebeginTime) {
		this.receivebeginTime = receivebeginTime;
	}

	public String getReceiveendTime() {
		return receiveendTime;
	}

	public void setReceiveendTime(String receiveendTime) {
		this.receiveendTime = receiveendTime;
	}

	public String getSendbeginTime() {
		return sendbeginTime;
	}

	public void setSendbeginTime(String sendbeginTime) {
		this.sendbeginTime = sendbeginTime;
	}

	public String getSendendTime() {
		return sendendTime;
	}

	public void setSendendTime(String sendendTime) {
		this.sendendTime = sendendTime;
	}

	public String getSendStorage() {
		return sendStorage;
	}

	public void setSendStorage(String sendStorage) {
		this.sendStorage = sendStorage;
	}

	public String getSpillZone() {
		return spillZone;
	}

	public void setSpillZone(String spillZone) {
		this.spillZone = spillZone;
	}

}