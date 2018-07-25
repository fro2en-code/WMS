package com.plat.common.beans;

public class Attr {

	/**
	 * 平台标识
	 */
	private String wmsMark;

	public String getWmsMark() {
		return wmsMark;
	}

	public void setWmsMark(String wmsMark) {
		this.wmsMark = wmsMark;
	}

	/**
	 * LE平台
	 */
	private String loginUrl;
	private String j_username;
	private String j_password;

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getJ_username() {
		return j_username;
	}

	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}

	public String getJ_password() {
		return j_password;
	}

	public void setJ_password(String j_password) {
		this.j_password = j_password;
	}
}
