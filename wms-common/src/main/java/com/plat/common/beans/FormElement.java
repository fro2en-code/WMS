package com.plat.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.plat.common.beans.BillBean.AttachData;

import net.sf.json.JSONObject;

public class FormElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4884268918701739973L;
	
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 默认值
	 */
	private String defaultVal;
	/**
	 * 变量名称:收集数据映射到该字段上
	 */
	private String varName;
	/**
	 * 元素类型：1：文本框 textBox; <br/>
	 * 2：输入框 inputBox;<br/>
	 * 3：下拉列表框 select; <br/>
	 * 4:多选框 checkbox;<br/>
	 * 5:单选框 radioBox <br/>
	 *
	 */
	private int elemType;
	/**
	 * 显示或隐藏，默认为显示
	 */
	private boolean display = true;
	/**
	 * 可选项列表，对于下拉列表框和多选框。
	 */
	private List<OptVal> optVals = new ArrayList<>();
	/**
	 * 约束列表 非空约束Key：notNullRestr 文本格式约束Key：formatRestr
	 */
	private Map<String, RestrainOpt> restrainOpts = new HashMap<>();

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public int getElemType() {
		return elemType;
	}

	public void setElemType(int elemType) {
		this.elemType = elemType;
	}

	public List<OptVal> getOptVals() {
		return optVals;
	}

	public void setOptVals(List<OptVal> optVals) {
		this.optVals = optVals;
	}

	public Map<String, RestrainOpt> getRestrainOpts() {
		return restrainOpts;
	}

	public void setRestrainOpts(Map<String, RestrainOpt> restrainOpts) {
		this.restrainOpts = restrainOpts;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	/**
	 * 下拉列表或多选框选项值
	 *
	 */
	public static final class OptVal {

		public OptVal(String key, String val) {
			this.key = key;
			this.val = val;
		}

		// 可选项名称
		private final String key;
		// 可选项值
		private final String val;

		public String getKey() {
			return key;
		}

		public String getVal() {
			return val;
		}

	}

	/**
	 * 约束
	 *
	 */
	public static final class RestrainOpt {

		public RestrainOpt(String condition, String tip) {
			this.condition = condition;
			this.tip = tip;
		}

		// 约束条件：
		/**
		 * 是否必填，取值：默认0，可为空;1:必填 <br/>
		 * 文本格式：取值：默认0，不限制;1：中文；2：数字
		 */

		private final String condition;
		// 违反约束后提示
		private final String tip;

		public String getCondition() {
			return condition;
		}

		public String getTip() {
			return tip;
		}
	}

	/**
	 * 表单元素测试
	 * 
	 * @param args
	 */
	@Test
	public void testFormElem() {
		List<FormElement[]> list = new ArrayList<>();
		FormElement[] elems = new FormElement[4];
		elems[0] = new FormElement();
		elems[0].setElemType(1);
		elems[0].setDisplay(false);
		elems[0].setDefaultVal("100010");
		elems[0].setVarName("gcode");

		elems[1] = new FormElement();
		elems[1].setElemType(1);
		elems[1].setTitle("应收数量");
		elems[1].setDefaultVal("100");
		elems[1].setVarName("needNum");
		
		elems[2] = new FormElement();
		elems[2].setElemType(2);
		elems[2].setTitle("实收数量");
		elems[2].getRestrainOpts().put("notNullRestr", new RestrainOpt("1", "实收数量不能为空"));
		elems[2].getRestrainOpts().put("formatRestr", new RestrainOpt("2", "请填写数字"));
		elems[2].setVarName("actualNum");
		
		elems[3] = new FormElement();
		elems[3].setElemType(3);
		elems[3].setTitle("零件用途");
		elems[3].getRestrainOpts().put("notNullRestr", new RestrainOpt("1", "请选择零件用途"));
		List<OptVal> optVals = new ArrayList<>();
		optVals.add(new OptVal("备件", "1"));
		optVals.add(new OptVal("出口", "2"));
		optVals.add(new OptVal("进口", "3"));
		elems[3].setOptVals(optVals);
		elems[3].setVarName("guse");
		list.add(elems);

		elems = new FormElement[4];
		elems[0] = new FormElement();
		elems[0].setElemType(1);
		elems[0].setDisplay(false);
		elems[0].setDefaultVal("100011");
		elems[0].setVarName("gcode");

		elems[1] = new FormElement();
		elems[1].setElemType(1);
		elems[1].setTitle("应收数量");
		elems[1].setDefaultVal("80");
		elems[1].setVarName("needNum");

		elems[2] = new FormElement();
		elems[2].setElemType(2);
		elems[2].setTitle("实收数量");
		elems[2].getRestrainOpts().put("notNullRestr", new RestrainOpt("1", "实收数量不能为空"));
		elems[2].getRestrainOpts().put("formatRestr", new RestrainOpt("2", "请填写数字"));
		elems[2].setVarName("actualNum");

		elems[3] = new FormElement();
		elems[3].setElemType(3);
		elems[3].setTitle("零件用途");
		elems[3].getRestrainOpts().put("notNullRestr", new RestrainOpt("1", "请选择零件用途"));
		optVals = new ArrayList<>();
		optVals.add(new OptVal("备件", "1"));
		optVals.add(new OptVal("出口", "2"));
		optVals.add(new OptVal("进口", "3"));
		elems[3].setOptVals(optVals);
		elems[3].setVarName("guse");
		list.add(elems);

		FormBean formBean = new FormBean();
		// 表单类型
		formBean.setFormType(1);
		AttachData attachData = new AttachData();
		attachData.setBillId("100010");
		attachData.setStorageCode("2340");
		attachData.setTaskId("1000782598");
		formBean.setAttachData(attachData);
//		formBean.setFormElems(list);
		String strJson = JSONObject.fromObject(formBean).toString();

		System.out.println(strJson);
	}
}
