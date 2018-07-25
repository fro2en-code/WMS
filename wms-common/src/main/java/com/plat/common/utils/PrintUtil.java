package com.plat.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class PrintUtil {
	
	private String title;
	private List<Object> dataList = new ArrayList<Object>();	
	private Map<String, String> rowField = new LinkedHashMap<String, String>();	
	
	/**
	 * 数据
	 */
	@SuppressWarnings("rawtypes")
	public List getDataList() {
		return dataList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PrintUtil setDataList(List dataList) {
		this.dataList = dataList;
		return this;
	}
	
	/**
	 * 单据标题
	 */	
	public String getTitle() {
		return title;
	}

	public PrintUtil setTitle(String title) {
		this.title = title;
		return this;
	}
	
	
	/**
	 * 表头
	 */
	public Map<String, String> getRowField() {
		return rowField;
	}

	public PrintUtil setRowField(String key,String val) {
		this.rowField.put(key, val);
		return this;
	}
	
	/**
	 * 完成
	 */
	public PrintUtil Done(HttpServletRequest request){
		request.setAttribute("rowField", rowField);
    	request.setAttribute("rowList", createRow());
    	request.setAttribute("title", title);
		return this;
	}	
	
	//处理数据行
	@SuppressWarnings("rawtypes")
	private List<Map<String, Object>> createRow(){
		List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
		for (Object objBean : this.dataList) {
			Map tempMap = PrintUtil.beanToMap(objBean);
			Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
			for (Map.Entry map : rowField.entrySet()) {
				rowMap.put(map.getKey() + "", tempMap.get(map.getKey()));
			}
			rowList.add(rowMap);
		}
		return rowList;
	}
	

	/**
	 * 对象转Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map beanToMap(Object bean) {
		try {
			Class type = bean.getClass();
			Map returnMap = new HashMap();
			BeanInfo beanInfo;
			beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					if (result != null) {
						returnMap.put(propertyName, result);
					} else {
						returnMap.put(propertyName, "");
					}
				}
			}
			return returnMap;
		} catch (Exception e) {
			return null;
		}
	}
}
