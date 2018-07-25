package com.plat.common.beans;

import java.io.Serializable;

public class ClientInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1356843354192624462L;
	/**
	 * 登录名称
	 */
	private String loginname;
	/**
	 * IOS
	 */
	private String token;
	/**
	 * 客户端标识
	 */
	private String clientid;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
}
