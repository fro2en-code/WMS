package com.rtzltech.wms.les;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import com.plat.common.utils.StringUtil;

public class LETest {

	@Test
	public void testLogin() {
		try {
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod("http://les.mychery.com/lesuppl/j_spring_security_check");
			NameValuePair[] nameValuePairs = { new NameValuePair("j_username", "3PL0101"),
					new NameValuePair("j_password", "CJcy01") };
			postMethod.setRequestBody(nameValuePairs);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpClient.executeMethod(postMethod);

			// postMethod = new
			// PostMethod("http://les.mychery.com/lesuppl/pullmanage/materal_query.action");
			postMethod = new PostMethod("http://les.mychery.com/lesuppl/pullmanage/material-pull-export.action");
			String curDate = StringUtil.getCurStringDate("yyyy-MM-dd");
			NameValuePair[] _nameValuePairs = {
					// 需求状态：1：已发布；2：供应商已接收；4：供应商已确认
					new NameValuePair("query.queryCondition.mriStatus", "2"),
					// 需求创建时间 从
					new NameValuePair("query.queryCondition.mriCreateTimeFrom", curDate),
					// 需求创建时间 到
					new NameValuePair("query.queryCondition.mriCreateTimeTo", curDate),
					// 收货地要求到货时间 从
					new NameValuePair("query.queryCondition.receiveRequireTimeFrom", curDate),
					// 收货地要求到货时间 到
					new NameValuePair("query.queryCondition.receiveRequireTimeTo", curDate) };
			postMethod.setRequestBody(_nameValuePairs);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpClient.executeMethod(postMethod);
			InputStream inStream = postMethod.getResponseBodyAsStream();
			HSSFWorkbook workbook = new HSSFWorkbook(inStream);
			File billDir=new File("d:/www");
			if(!billDir.exists()){
				billDir.mkdirs();
			}
			File destF = new File(billDir, "bill.xls");
			FileOutputStream outputStream = new FileOutputStream(destF);
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
