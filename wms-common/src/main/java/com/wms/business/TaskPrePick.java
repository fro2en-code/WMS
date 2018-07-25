package com.wms.business;

import com.plat.common.beans.BaseModel;

public class TaskPrePick extends BaseModel {
    /**
     * 预拣货任务定义
     * wangzz
     */
    private static final long serialVersionUID = 1L;
    private String expression;
    private String gcode;
    private String gname;
    private String gtype;
    private Integer prePickNum;
    private Integer reqPackageNum;
    private Integer sendPackageNum;
    private String whCode;
    private String whName;
    private String oraCode;
    private String oraName;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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

	public Integer getPrePickNum() {
        return prePickNum;
    }

    public void setPrePickNum(Integer prePickNum) {
        this.prePickNum = prePickNum;
    }

    public Integer getReqPackageNum() {
        return reqPackageNum;
    }

    public void setReqPackageNum(Integer reqPackageNum) {
        this.reqPackageNum = reqPackageNum;
    }

    public Integer getSendPackageNum() {
        return sendPackageNum;
    }

    public void setSendPackageNum(Integer sendPackageNum) {
        this.sendPackageNum = sendPackageNum;
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
}
