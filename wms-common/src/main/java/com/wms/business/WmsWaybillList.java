package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 运单信息明细
 */
public class WmsWaybillList extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 单据编号
     */
    private String number;

    /**
     * 发货单据编号
     */
    private String sendBillNumber;

    private String type;
    private String whCode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSendBillNumber() {
        return sendBillNumber;
    }

    public void setSendBillNumber(String sendBillNumber) {
        this.sendBillNumber = sendBillNumber;
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
