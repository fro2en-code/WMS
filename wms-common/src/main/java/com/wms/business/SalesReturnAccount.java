package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 退货台账
 * 
 * @author Administrator
 *
 */
public class SalesReturnAccount extends BaseModel {
	private static final long serialVersionUID = -3933101559046402153L;

	/**
	 * 物料代码
	 */
	private String gcode;
	/**
	 * 物料名称
	 */
	private String gname;

	private String gtype;
	/**
	 * 供应商代码
	 */
	private String oraCode;
	/**
	 * 供应商名称
	 */
	private String oraName;

	/**
	 * 库存数量
	 */
	private int totalNum;

	/**
	 * 仓库代码
	 */
	private String whCode;

	/**
	 * 仓库名称
	 */
	private String whName;

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

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
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
}
