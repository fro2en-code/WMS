package com.wms.userRelation;

import com.plat.common.beans.BaseModel;

/**
 * 库区管理人
 */
public class ZoneManager extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String groupid;// 货位组编码
	private String zonecode;// 所属库区编号
	private String zonename;//所属库区名字
	private String whcode;// 所属仓库编号
	private String userid;// 库管用户编号
	private String userloginname;// 库管用户登陆名

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getZonecode() {
		return zonecode;
	}

	public void setZonecode(String zonecode) {
		this.zonecode = zonecode;
	}

	public String getZonename() {
		return zonename;
	}

	public void setZonename(String zonename) {
		this.zonename = zonename;
	}

	public String getWhcode() {
		return whcode;
	}

	public void setWhcode(String whcode) {
		this.whcode = whcode;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserloginname() {
		return userloginname;
	}

	public void setUserloginname(String userloginname) {
		this.userloginname = userloginname;
	}

}
