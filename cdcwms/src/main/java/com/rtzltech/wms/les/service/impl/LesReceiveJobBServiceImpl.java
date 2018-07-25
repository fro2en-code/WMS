package com.rtzltech.wms.les.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wms.business.WmsLesReceive;
import com.ymt.api.PublicApi;
import com.ymt.utils.SpringContex;

/**
 * 股份+凯翼+ 部分标准件 3PL0103 ZSgj1234
 *
 * @author mjl-pc
 *
 */
@Service("lesReceiveJobBService")
public class LesReceiveJobBServiceImpl extends BaseJobService {

    // // 收货账号
    // @Override
    // public void execute() {
    // try {
    //
    // String warehouse_id = "3PL";
    //
    // HttpClient httpClient = new HttpClient();
    // PostMethod postMethod = new PostMethod("http://les.mychery.com/lesuppl/j_spring_security_check");
    // NameValuePair[] nameValuePairs = { new NameValuePair("j_username", "3PL0104"),
    // new NameValuePair("j_password", "Pass123") };
    // postMethod.setRequestBody(nameValuePairs);
    // postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    // httpClient.executeMethod(postMethod);
    //
    // postMethod = new PostMethod("http://les.mychery.com/lesuppl/pullmanage/material-pull-export.action");
    // String curDate = StringUtil.getCurStringDate("yyyy-MM-dd");
    // NameValuePair[] _nameValuePairs = {
    // // 需求状态：1：已发布；2：供应商已接收；4：供应商已确认
    // new NameValuePair("query.queryCondition.mriStatus", "2"),
    // // 需求创建时间 从
    // new NameValuePair("query.queryCondition.mriCreateTimeFrom", curDate),
    // // 需求创建时间 到
    // new NameValuePair("query.queryCondition.mriCreateTimeTo", curDate),
    // // 收货地要求到货时间 从
    // new NameValuePair("query.queryCondition.receiveRequireTimeFrom", curDate),
    // // 收货地要求到货时间 到
    // new NameValuePair("query.queryCondition.receiveRequireTimeTo", curDate) };
    // postMethod.setRequestBody(_nameValuePairs);
    // postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    // httpClient.executeMethod(postMethod);
    // InputStream inStream = postMethod.getResponseBodyAsStream();
    // HSSFWorkbook workbook = new HSSFWorkbook(inStream);
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // 收货账号
    @Override
    public void execute() {
        try {
            String warehouse_id = "3PL";
            String loginUrl = "http://les.mychery.com/lesuppl/j_spring_security_check";
            Map<String, String> loginParams = new HashMap<>();
            loginParams.put("j_username", "3PL0104");
            loginParams.put("j_password", "Pass123");
            String requestUrl = "http://les.mychery.com/lesuppl/pullmanage/material-pull-export.action";
            Map<String, String> requestParams = new HashMap<>();
            // 需求状态：1：已发布；2：供应商已接收；4：供应商已确认
            requestParams.put("query.queryCondition.mriStatus", "2");
            InputStream inStream = sendRequest(loginUrl, loginParams, requestUrl, requestParams);
            Map<String, WmsLesReceive> WmsLesMap = getLesReceiveBill(warehouse_id, inStream);
            inStream.close();
            PublicApi publicApi = SpringContex.getBean("publicApi");
            for (WmsLesReceive value : WmsLesMap.values()) {
                publicApi.addLesRecive(value);
            }
            System.out.print("LesSendJobBService OK！MAP count=" + WmsLesMap.size() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
