package com.plat.common.beans;

import java.io.Serializable;




public class UserBean implements Serializable{

	private static final long serialVersionUID = 1488318955712837539L;
	/**
	 * 登录名称
	 */
	private String loginname;
	/**
	 * 密码
	 */
	private String pwd;
	/**
	 * 真实名称
	 */
	private String truename;
	//是否需要扫道口标签标识
	private String userFlag="0";
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 邮箱地址
	 */
	private String email;
	/**
	 * 0：启用；1：禁用
	 */
	private int state;
	/**
	 * 记录创建人
	 */
	private String creator;

	// 已绑平台key['zhihuidistrib','zhihuimonitor']
	private String bindPlatKey;
	// 已绑平台名称[智慧运输-分销平台,智慧运输-监控平台]
	private String bindPlatName;
	// 菜单JSON
	private String jsonMenuTree;
	// 菜单按钮JSON
	private String jsonMenuBtn;
	// 菜单集合
//	private List<Plat_Menu> menuList = new ArrayList<Plat_Menu>();
	// 按钮集合
//	private Map<String, List<Plat_Button>> menuBtnMap = new HashMap<String, List<Plat_Button>>();
	
	public String getLoginname() {
		return loginname;
	}

	public String getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getBindPlatKey() {
		return bindPlatKey;
	}
	public void setBindPlatKey(String bindPlatKey) {
		this.bindPlatKey = bindPlatKey;
	}
	public String getBindPlatName() {
		return bindPlatName;
	}
	public void setBindPlatName(String bindPlatName) {
		this.bindPlatName = bindPlatName;
	}
	public String getJsonMenuTree() {
		return jsonMenuTree;
	}
	public void setJsonMenuTree(String jsonMenuTree) {
		this.jsonMenuTree = jsonMenuTree;
	}
	public String getJsonMenuBtn() {
		return jsonMenuBtn;
	}
	public void setJsonMenuBtn(String jsonMenuBtn) {
		this.jsonMenuBtn = jsonMenuBtn;
	}
	@Override
	public String toString() {
		return "UserBean [loginname=" + loginname + ", pwd=" + pwd
				+ ", truename=" + truename + ", phone=" + phone + ", email="
				+ email + ", state=" + state + ", creator=" + creator
				+ ", bindPlatKey=" + bindPlatKey + ", bindPlatName="
				+ bindPlatName + ", jsonMenuTree=" + jsonMenuTree
				+ ", jsonMenuBtn=" + jsonMenuBtn + "]";
	}
	



}
