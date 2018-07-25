package com.wms.business;
// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

import com.plat.common.beans.BaseModel;

/**
 * 发货单的抽象类,并没有对应数据库实体
 */
public class WmsSendBillList extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String number;
    private String gCode;
    private String oraCode;
    private String sxCardNo;
    private String sendPackageNo;
    private Integer sendPackageNum;
    private Integer reqPackageNum;
    private Integer reqQty;
    private String whCode;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getgCode() {
        return gCode;
    }

    public void setgCode(String gCode) {
        this.gCode = gCode;
    }

    public String getOraCode() {
        return oraCode;
    }

    public void setOraCode(String oraCode) {
        this.oraCode = oraCode;
    }

    public String getSxCardNo() {
        return sxCardNo;
    }

    public void setSxCardNo(String sxCardNo) {
        this.sxCardNo = sxCardNo;
    }

    public String getSendPackageNo() {
        return sendPackageNo;
    }

    public void setSendPackageNo(String sendPackageNo) {
        this.sendPackageNo = sendPackageNo;
    }

    public Integer getSendPackageNum() {
        return sendPackageNum;
    }

    public void setSendPackageNum(Integer sendPackageNum) {
        this.sendPackageNum = sendPackageNum;
    }

    public Integer getReqPackageNum() {
        return reqPackageNum;
    }

    public void setReqPackageNum(Integer reqPackageNum) {
        this.reqPackageNum = reqPackageNum;
    }

    public Integer getReqQty() {
        return reqQty;
    }

    public void setReqQty(Integer reqQty) {
        this.reqQty = reqQty;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }
}
