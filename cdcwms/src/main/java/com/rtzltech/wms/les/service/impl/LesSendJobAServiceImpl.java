package com.rtzltech.wms.les.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.plat.common.utils.StringUtil;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsLesSendList;
import com.ymt.api.PublicApi;
import com.ymt.utils.SpringContex;

/**
 * 鄂尔多斯 3PL01 Abc123
 *
 * @author mjl-pc
 *
 */
@Service("lesSendJobAService")
public class LesSendJobAServiceImpl extends BaseJobService {

    // 鄂尔多斯
    @Override
    public void execute() {
        try {
            String warehouse_id = "3PL";
            String loginUrl = "http://ordosles.mychery.com/ordoslesuppl/j_spring_security_check";
            Map<String, String> loginParams = new HashMap<>();
            loginParams.put("j_username", "3PL01");
            loginParams.put("j_password", "CYwl123");
            String requestUrl = "http://ordosles.mychery.com/ordoslesuppl/pullmanage/material-pull-export.action";
            Map<String, String> requestParams = new HashMap<>();
            // 需求状态：1：已发布；2：供应商已接收；4：供应商已确认
            requestParams.put("query.queryCondition.mriStatus", "2");
            String curDate = StringUtil.getCurStringDate("yyyy-MM-dd");
            requestParams.put("query.queryCondition.mriCreateTimeFrom", curDate);
            requestParams.put("query.queryCondition.mriCreateTimeTo", curDate);
            requestParams.put("query.queryCondition.receiveRequireTimeFrom", curDate);
            requestParams.put("query.queryCondition.receiveRequireTimeTo", curDate);
            InputStream inStream = sendRequest(loginUrl, loginParams, requestUrl, requestParams);
            Map<String, WmsLesSend> WmsLesMap = readLesSendBill(warehouse_id, inStream);
            inStream.close();
            PublicApi publicApi = SpringContex.getBean("publicApi");
            for (WmsLesSend value : WmsLesMap.values()) {
                publicApi.addLesSend(value);
            }
            System.out.print("LesSendJobAService OK！MAP count=" + WmsLesMap.size() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Map<String, WmsLesSend> readLesSendBill(String warehouse_id, InputStream inStream) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(inStream);
        HSSFSheet sheet = null;

        String IS_EMERGE = null; // 是否紧急
        String MRI_STATUS = null; // 需求状态
        String PART_NO = null; // 零件编号
        String SUPPL_NO = null; // 供应商编码
        String STR_REQ_QTY = null;
        int REQ_QTY = 0; // 需求数量
        String LAST_REC_REQURIE_TIME = null; // 目的地要求到货时间
        String SHEET_STATUS = null; // 配送单状态
        String SX_CARD_NO = null; // 随箱卡号
        String MAP_SHEET_NO = null; // 配送单号
        String STR_REQ_PACKAGE_NUM = null;
        int REQ_PACKAGE_NUM = 0; // 需求箱数
        String DELIVERY_REC = null; // 收货道口编号
        String DELIVERY_REC_TYPE = null; // 收货地类型
        String DELIVERY_SEND = null; // 发货地代码
        String SEND_PACKAGE_NO = null; // 发运包装代码
        String PULL_TYPE = null; // 配送模式

        Map<String, WmsLesSend> WmsLesMap = new HashMap<String, WmsLesSend>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(i);
            for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
                HSSFRow row = sheet.getRow(j);
                IS_EMERGE = row.getCell(2).getStringCellValue();
                MRI_STATUS = row.getCell(3).getStringCellValue();
                PART_NO = row.getCell(4).getStringCellValue();
                SUPPL_NO = row.getCell(6).getStringCellValue();
                STR_REQ_QTY = row.getCell(8).getStringCellValue();
                try {
                    REQ_QTY = Integer.valueOf(STR_REQ_QTY).intValue();
                } catch (NumberFormatException e) {
                }
                LAST_REC_REQURIE_TIME = row.getCell(10).getStringCellValue();
                SX_CARD_NO = row.getCell(11).getStringCellValue();
                MAP_SHEET_NO = row.getCell(18).getStringCellValue();
                SHEET_STATUS = row.getCell(19).getStringCellValue();
                STR_REQ_PACKAGE_NUM = row.getCell(20).getStringCellValue();
                try {
                    REQ_PACKAGE_NUM = Integer.valueOf(STR_REQ_PACKAGE_NUM).intValue();
                } catch (NumberFormatException e) {
                }
                DELIVERY_REC = row.getCell(21).getStringCellValue();
                SEND_PACKAGE_NO = row.getCell(22).getStringCellValue();
                PULL_TYPE = row.getCell(28).getStringCellValue();
                DELIVERY_SEND = row.getCell(31).getStringCellValue();
                DELIVERY_REC_TYPE = row.getCell(34).getStringCellValue();

                if (MAP_SHEET_NO != null) {
                    WmsLesSend recive = WmsLesMap.get(MAP_SHEET_NO);
                    List<WmsLesSendList> wmsLessendLists = null;

                    if (recive == null) {
                        recive = new WmsLesSend();
                        recive.setMapSheetNo(MAP_SHEET_NO);
                        if (IS_EMERGE.equals("紧急")) {
                            recive.setIsEmerge((long) 1);
                        } else {
                            recive.setIsEmerge((long) 0);
                        }

                        recive.setMriStatus(MRI_STATUS);
                        recive.setLastRecRequrieTime(LAST_REC_REQURIE_TIME);
                        recive.setSheetStatus(SHEET_STATUS);
                        recive.setDeliveryRec(DELIVERY_REC);
                        recive.setDeliveryRecType(DELIVERY_REC_TYPE);
                        recive.setDeliverySend(DELIVERY_SEND);
                        recive.setPullType(PULL_TYPE);
                        recive.setWhCode(warehouse_id);

                        wmsLessendLists = new ArrayList<WmsLesSendList>();
                        recive.setWmsLesSendList(wmsLessendLists);
                        WmsLesMap.put(MAP_SHEET_NO, recive);
                    } else {
                        wmsLessendLists = recive.getWmsLesSendList();
                    }

                    WmsLesSendList item = new WmsLesSendList();
                    item.setWhCode(warehouse_id);
                    item.setMapSheetNo(MAP_SHEET_NO);
                    item.setSxCardNo(SX_CARD_NO);
                    item.setPartNo(PART_NO);
                    item.setSupplNo(SUPPL_NO);
                    item.setReqQty(REQ_QTY);
                    item.setReqPackageNum(REQ_PACKAGE_NUM);
                    item.setSendPackageNo(SEND_PACKAGE_NO);
                    wmsLessendLists.add(item);
                }
            }
        }
        return WmsLesMap;
    }

}
