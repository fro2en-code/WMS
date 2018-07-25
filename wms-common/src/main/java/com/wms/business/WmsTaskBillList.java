package com.wms.business;

import com.plat.common.beans.BaseModel;

// Generated 2017-3-22 16:50:30 by Hibernate Tools 5.2.1.Final

/**
 * 任务单据明细
 */
public class WmsTaskBillList extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String billid;
    private String gcode;
    private String gname;
    private String gtype;
    private String oraCode;
    private String oraName;
    private String abcType;
    private String boxCardid;
    private String boxType;
    private Integer boxContent;
    private Integer boxNum;
    private Integer goodNeedNum;
    private Integer goodRealNum;
    private String parentid;

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
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

    public String getBoxCardid() {
        return boxCardid;
    }

    public void setBoxCardid(String boxCardid) {
        this.boxCardid = boxCardid;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public Integer getBoxContent() {
        return boxContent;
    }

    public void setBoxContent(Integer boxContent) {
        this.boxContent = boxContent;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public Integer getGoodNeedNum() {
        return goodNeedNum;
    }

    public void setGoodNeedNum(Integer goodNeedNum) {
        this.goodNeedNum = goodNeedNum;
    }

    public Integer getGoodRealNum() {
        return goodRealNum;
    }

    public void setGoodRealNum(Integer goodRealNum) {
        this.goodRealNum = goodRealNum;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
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

	public String getAbcType() {
		return abcType;
	}

	public void setAbcType(String abcType) {
		this.abcType = abcType;
	}
    
    
}
