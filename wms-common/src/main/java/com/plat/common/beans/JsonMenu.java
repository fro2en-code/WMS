package com.plat.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsonMenu implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 子菜单
	 */
	private List<Button> children = new ArrayList<Button>();
	private String text;
	public List<Button> getChildren() {
		return children;
	}
	public void setChildren(List<Button> children) {
		this.children = children;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
