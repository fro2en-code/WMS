package com.wms.business;

import java.util.List;

import com.plat.common.beans.BaseModel;

/**
 * 发货单的抽象类,并没有对应数据库实体
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年9月1日
 */
public class WmsSendBill extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String number;
    private String priority;// 优先级
    private String type;// 类型
    private String lastRecRequrieTime;// 要求到货时间
    private String mriCreateTime;// 需求创建时间
    private String deliveryRec;// 目的地代码
    private String whCode;// 仓库
    private List<WmsSendBillList> wmsSendBillList;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastRecRequrieTime() {
        return lastRecRequrieTime;
    }

    public void setLastRecRequrieTime(String lastRecRequrieTime) {
        this.lastRecRequrieTime = lastRecRequrieTime;
    }

    public String getMriCreateTime() {
        return mriCreateTime;
    }

    public void setMriCreateTime(String mriCreateTime) {
        this.mriCreateTime = mriCreateTime;
    }

    public String getDeliveryRec() {
        return deliveryRec;
    }

    public void setDeliveryRec(String deliveryRec) {
        this.deliveryRec = deliveryRec;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public List<WmsSendBillList> getWmsSendBillList() {
        return wmsSendBillList;
    }

    public void setWmsSendBillList(List<WmsSendBillList> wmsSendBillList) {
        this.wmsSendBillList = wmsSendBillList;
    }
}
