package com.wms.business;
// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

import com.plat.common.beans.BaseModel;

/**
 * 预警规则信息	
 */
public class WarnRule extends BaseModel{

	private static final long serialVersionUID = 1L;
    private String whCode;
    private Integer warnType;
    private String warnTime;
    private String warnObject;
    private String warnResult;



    public String getWhCode() {
        return this.whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public Integer getWarnType() {
        return this.warnType;
    }

    public void setWarnType(Integer warnType) {
        this.warnType = warnType;
    }

    public String getWarnTime() {
        return this.warnTime;
    }

    public void setWarnTime(String warnTime) {
        this.warnTime = warnTime;
    }

    public String getWarnObject() {
        return this.warnObject;
    }

    public void setWarnObject(String warnObject) {
        this.warnObject = warnObject;
    }

    public String getWarnResult() {
        return this.warnResult;
    }

    public void setWarnResult(String warnResult) {
        this.warnResult = warnResult;
    }


}
