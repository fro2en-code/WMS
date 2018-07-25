package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 运单信息
 */
public class WmsWaybill extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String number;
    private String truckNO;
    private String drive;
    private String drivePhone;
    private String whCode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTruckNO() {
        return truckNO;
    }

    public void setTruckNO(String truckNO) {
        this.truckNO = truckNO;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getDrivePhone() {
        return drivePhone;
    }

    public void setDrivePhone(String drivePhone) {
        this.drivePhone = drivePhone;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

}
