package com.wms.orginfo;

import com.plat.common.beans.BaseModel;

/**
 * 公司信息
 */

public class OrgConpany extends BaseModel {
    private static final long serialVersionUID = 1L;
    private String address;// 地址
    private String companyemail;// 公司电子邮箱
    private String companyid;// 公司编码
    private String contacter;// 联系人
    private String latitude;// gps纬度
    private String longitude;// gps经度
    private String mobile;// 手机号码
    private String name;// 公司名称
    private String telphone;// 联系电话
    private String type; // 公司类型 0运行公司1 供应商 2承运商 3 业务中心
    private String whCode;

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getAddress() {
        return address;
    }

    public String getCompanyemail() {
        return companyemail;
    }

    public String getCompanyid() {
        return companyid;
    }

    public String getContacter() {
        return contacter;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getTelphone() {
        return telphone;
    }

    public String getType() {
        return type;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCompanyemail(String companyemail) {
        this.companyemail = companyemail;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public void setContacter(String contacter) {
        this.contacter = contacter;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public void setType(String type) {
        this.type = type;
    }

}