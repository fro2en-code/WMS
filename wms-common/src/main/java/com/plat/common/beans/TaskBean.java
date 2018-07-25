package com.plat.common.beans;

import java.io.Serializable;

public class TaskBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 167704852225439033L;
	private String taskId;
	private String taskName;
	private String createTime;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
