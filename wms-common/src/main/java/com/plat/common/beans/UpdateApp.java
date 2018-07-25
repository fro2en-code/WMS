package com.plat.common.beans;

import java.io.Serializable;

/**
 * @author Thunder
 * 
 */
public class UpdateApp implements Serializable {
	private static final long serialVersionUID = 6325224023081069295L;
	private String newVer;
	private String downloadUrl;
	private String updateLog;
	private String iosUpdateFlag;
	
	public String getIosUpdateFlag() {
		return iosUpdateFlag;
	}

	public void setIosUpdateFlag(String iosUpdateFlag) {
		this.iosUpdateFlag = iosUpdateFlag;
	}

	public String getNewVer() {
		return newVer;
	}

	public void setNewVer(String newVer) {
		this.newVer = newVer;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

}
