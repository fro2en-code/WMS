package com.rtzltech.wms.les.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wms.business.WmsLesSend;
import com.ymt.api.PublicApi;
import com.ymt.utils.SpringContex;

/**
 * 标准件 3PL0105 ZSgj4321
 *
 * @author mjl-pc
 *
 */
@Service("lesSendJobCService")
public class LesSendJobCServiceImpl extends BaseJobService {

    // 标准件
    @Override
    public void execute() {
        try {
            String warehouse_id = "3PL";
            String loginUrl = "http://les.mychery.com/lesuppl/j_spring_security_check";
            Map<String, String> loginParams = new HashMap<>();
            loginParams.put("j_username", "3PL0105");
            loginParams.put("j_password", "ZSgj4321");
            String requestUrl = "http://les.mychery.com/lesuppl/pullmanage/material-pull-export.action";
            Map<String, String> requestParams = new HashMap<>();
            // 需求状态：1：已发布；2：供应商已接收；4：供应商已确认
            requestParams.put("query.queryCondition.mriStatus", "2");
            InputStream inStream = sendRequest(loginUrl, loginParams, requestUrl, requestParams);
            Map<String, WmsLesSend> WmsLesMap = readLesSendBill(warehouse_id, inStream);
            inStream.close();
            PublicApi publicApi = SpringContex.getBean("publicApi");
            for (WmsLesSend value : WmsLesMap.values()) {
                publicApi.addLesSend(value);
            }
            System.out.print("LesSendJobCService OK！MAP count=" + WmsLesMap.size() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
