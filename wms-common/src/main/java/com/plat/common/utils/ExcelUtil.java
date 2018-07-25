package com.plat.common.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用Excel工具类 Author Hmgx data 2016-10-12 by 融通智联
 */
public class ExcelUtil {

	public static Map<String, Excel> ExcelTemplate = new HashMap<String, Excel>();

	public static class Excel {

		// 表格列
		private Map<String, String> columnMap = new LinkedHashMap<String, String>();

		// 默认列
		public Map<String, Object> defaultColumnMap = new LinkedHashMap<String, Object>();
		
		// 下载模板文件名
		private String tempName = "";

		// 实体类全类名
		private String className;

		/**
		 * 设置表格列
		 */
		public Excel setColumn(String key, String value) {
			this.columnMap.put(key, value);
			return this;
		}

		/**
		 * 获取表格列
		 */
		public Map<String, String> getColumn() {
			return this.columnMap;
		}

		/**
		 * 设置表格默据列
		 */
		public Excel setDefColumn(String key, Object value) {
			this.defaultColumnMap.put(key, value);
			return this;
		}

		/**
		 * 获取表格默据列
		 */
		public Map<String, Object> getDefColumn() {
			return this.defaultColumnMap;
		}

		/**
		 * 设置下载模板文件名
		 */
		public Excel setTempName(String tempName) {
			this.tempName = tempName;
			return this;
		}

		/**
		 * 获取下载文件名
		 */
		public String getTempName() {
			return this.tempName;
		}

		/**
		 * 设置实体类的全类名
		 */
		public Excel setClassName(String className) {
			this.className = className;
			return this;
		}

		/**
		 * 获取实体类的全类名
		 */
		public String getClassName() {
			return this.className;
		}
		
		/**
		 * 完成
		 */
		public void Finish(HttpServletRequest request){
			
			//为避免相同路径不同方法生成的keyNo重复，所以加上了调用者的名字避免生成重复的keyNo
			StackTraceElement[] temp=Thread.currentThread().getStackTrace();
			StackTraceElement caller =(StackTraceElement)temp[2];
			String keyNo = MD5.MD5Encode( request.getRequestURI() + caller.getMethodName() , "utf-8");
			
			if(!ExcelUtil.ExcelTemplate.containsKey(keyNo) ){
				ExcelUtil.ExcelTemplate.put(keyNo , this);
			}
			request.setAttribute("keyNo", keyNo);
		}
	}
}
