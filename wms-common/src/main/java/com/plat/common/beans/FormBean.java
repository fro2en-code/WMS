package com.plat.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.plat.common.beans.BillBean.AttachData;

public class FormBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6535053024151283315L;
	
	private String retcode="-1";
	private String retmsg;
	/**
	 * 表单类型：1：收货表单；2：....
	 */
	private int formType;
	/**
	 * 表单携带数据
	 */
	private AttachData attachData;

	/**
	 * 表单元素
	 */
	private List<List<FormElement>> formElems=new ArrayList<>();
	/**
	 * 表单元素
	 */
	private List<FormElement> formElem=new ArrayList<>();

	
	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public AttachData getAttachData() {
		return attachData;
	}

	public void setAttachData(AttachData attachData) {
		this.attachData = attachData;
	}

	public List<List<FormElement>> getFormElems() {
		return formElems;
	}

	public int getFormType() {
		return formType;
	}

	public void setFormType(int formType) {
		this.formType = formType;
	}

	public List<FormElement> getFormElem() {
		return formElem;
	}
}
