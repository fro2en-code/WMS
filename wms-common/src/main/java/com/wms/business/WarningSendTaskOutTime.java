package com.wms.business;

import com.plat.common.beans.BaseModel;

public class WarningSendTaskOutTime extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskdesc;
	private String status;
	private String executorName;
	private String billid;
	private String type;
	public String getTaskdesc() {
		return taskdesc;
	}
	public void setTaskdesc(String taskdesc) {
		this.taskdesc = taskdesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExecutorName() {
		return executorName;
	}
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	public String getBillid() {
		return billid;
	}
	public void setBillid(String billid) {
		this.billid = billid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
