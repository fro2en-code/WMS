package com.rtzltech.wms.les.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.plat.common.utils.StringUtil;
import com.rtzltech.wms.les.service.LesJobService;
import com.wms.business.WmsLesBill;

import its.base.service.BaseServiceImpl;

/**
 * 股份+凯翼+ 部分标准件 3PL0103 ZSgj1234
 * 
 * @author mjl-pc
 *
 */
@Service("lesSendJobBService")
public class LesSendJobBServiceImpl extends BaseServiceImpl<WmsLesBill> implements LesJobService {

	//收货账号
	@Override
	public void execute() {
		try {
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod("http://les.mychery.com/lesuppl/j_spring_security_check");
			NameValuePair[] nameValuePairs = { new NameValuePair("j_username", "3PL0104"),
					new NameValuePair("j_password", "Pass123") };
			postMethod.setRequestBody(nameValuePairs);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpClient.executeMethod(postMethod);

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
