package com.rtzltech.wms.les.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.rtzltech.wms.les.service.LesJobService;
import com.wms.business.WmsLesReceive;
import com.wms.business.WmsLesReceiveList;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsLesSendList;

public abstract class BaseJobService implements LesJobService {
    public InputStream sendRequest(String loginUrl, Map<String, String> loginMap, String requestUrl,
            Map<String, String> requestMap) throws HttpException, IOException {
        HttpClient httpClient = new HttpClient();
        //
        sendPost(loginUrl, loginMap, httpClient);
        //
        PostMethod requestPost = sendPost(requestUrl, requestMap, httpClient);
        return requestPost.getResponseBodyAsStream();
    }

    private PostMethod sendPost(String url, Map<String, String> params, HttpClient httpClient)
            throws IOException, HttpException {
        PostMethod post = new PostMethod(url);
        List<NameValuePair> list = new ArrayList<>();
        for (Entry<String, String> entry : params.entrySet()) {
            list.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        post.setRequestBody(list.toArray(new NameValuePair[params.size()]));
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        httpClient.executeMethod(post);
        return post;
    }

    protected Map<String, WmsLesReceive> getLesReceiveBill(String warehouse_id, InputStream inStream)
            throws IOException {
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
        String mriCreateTime = null; // 需求创建时间
        String factory_name = null; // 工厂名称
        String workshop_name = null; // 车间名称
        String DELIVERY = null; // 收货地
        String DELIVERY_NAME = null; // 收货地名称

        Map<String, WmsLesReceive> WmsLesMap = new HashMap<String, WmsLesReceive>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(i);
            for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
                HSSFRow row = sheet.getRow(j);
                for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                    switch (k) {
                    case 2:
                        IS_EMERGE = row.getCell(k).getStringCellValue();
                        break;
                    case 3:
                        MRI_STATUS = row.getCell(k).getStringCellValue();
                        break;
                    case 4:
                        PART_NO = row.getCell(k).getStringCellValue();
                        break;
                    case 6:
                        SUPPL_NO = row.getCell(k).getStringCellValue();
                        break;
                    case 8:
                        STR_REQ_QTY = row.getCell(k).getStringCellValue();
                        try {
                            REQ_QTY = Integer.valueOf(STR_REQ_QTY).intValue();
                        } catch (NumberFormatException e) {
                        }
                        break;
                    case 11:
                        mriCreateTime = row.getCell(k).getStringCellValue();
                        break;
                    case 12:
                        LAST_REC_REQURIE_TIME = row.getCell(k).getStringCellValue();
                        break;
                    case 13:
                        SX_CARD_NO = row.getCell(k).getStringCellValue();
                        break;
                    case 20:
                        MAP_SHEET_NO = row.getCell(k).getStringCellValue();
                        break;
                    case 21:
                        SHEET_STATUS = row.getCell(k).getStringCellValue();
                        break;
                    case 22:
                        STR_REQ_PACKAGE_NUM = row.getCell(k).getStringCellValue();
                        try {
                            REQ_PACKAGE_NUM = Integer.valueOf(STR_REQ_PACKAGE_NUM).intValue();
                        } catch (NumberFormatException e) {
                        }
                        break;
                    case 23:
                        DELIVERY_REC = row.getCell(k).getStringCellValue();
                        break;
                    case 24:
                        SEND_PACKAGE_NO = row.getCell(k).getStringCellValue();
                        break;
                    case 26:
                        factory_name = row.getCell(k).getStringCellValue();
                        break;
                    case 28:
                        workshop_name = row.getCell(k).getStringCellValue();
                        break;
                    case 30:
                        PULL_TYPE = row.getCell(k).getStringCellValue();
                        break;
                    case 33:
                        DELIVERY_SEND = row.getCell(k).getStringCellValue();
                        break;
                    case 35:
                        DELIVERY_REC_TYPE = row.getCell(k).getStringCellValue();
                        break;
                    case 36:
                        DELIVERY = row.getCell(k).getStringCellValue();
                        break;
                    case 37:
                        DELIVERY_NAME = row.getCell(k).getStringCellValue();
                        break;
                    }
                }

                if (MAP_SHEET_NO != null) {
                    WmsLesReceive recive = WmsLesMap.get(MAP_SHEET_NO);

                    List<WmsLesReceiveList> wmsLesReceiveLists = null;

                    if (recive == null) {
                        recive = new WmsLesReceive();
                        recive.setMapSheetNo(MAP_SHEET_NO);
                        if (IS_EMERGE.equals("紧急")) {
                            recive.setIsEmerge(1);
                        } else {
                            recive.setIsEmerge(0);
                        }

                        recive.setMriStatus(MRI_STATUS);
                        recive.setMriCreateTime(mriCreateTime);
                        recive.setLastRecRequrieTime(LAST_REC_REQURIE_TIME);
                        recive.setSheetStatus(SHEET_STATUS);
                        recive.setPlantNo(factory_name);
                        recive.setDeliveryRec(DELIVERY_NAME);
                        recive.setDeliveryRecType(DELIVERY_REC_TYPE);
                        recive.setDeliverySend(DELIVERY_SEND);
                        recive.setPullType(PULL_TYPE);
                        recive.setWhCode(warehouse_id);

                        wmsLesReceiveLists = new ArrayList<WmsLesReceiveList>();
                        recive.setWmsLesReceiveLists(wmsLesReceiveLists);
                        WmsLesMap.put(MAP_SHEET_NO, recive);
                    } else {
                        wmsLesReceiveLists = recive.getWmsLesReceiveLists();
                    }

                    WmsLesReceiveList item = new WmsLesReceiveList();

                    item.setMapSheetNo(MAP_SHEET_NO);
                    item.setSxCardNo(SX_CARD_NO);
                    item.setPartNo(PART_NO);
                    item.setSupplNo(SUPPL_NO);
                    item.setReqQty(REQ_QTY);
                    item.setReqPackageNum(REQ_PACKAGE_NUM);
                    item.setSendPackageNo(SEND_PACKAGE_NO);
                    wmsLesReceiveLists.add(item);
                }
            }
        }
        return WmsLesMap;
    }

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
                LAST_REC_REQURIE_TIME = row.getCell(12).getStringCellValue();
                SX_CARD_NO = row.getCell(13).getStringCellValue();
                MAP_SHEET_NO = row.getCell(20).getStringCellValue();
                SHEET_STATUS = row.getCell(21).getStringCellValue();
                STR_REQ_PACKAGE_NUM = row.getCell(22).getStringCellValue();
                try {
                    REQ_PACKAGE_NUM = Integer.valueOf(STR_REQ_PACKAGE_NUM).intValue();
                } catch (NumberFormatException e) {
                }
                DELIVERY_REC = row.getCell(23).getStringCellValue();
                SEND_PACKAGE_NO = row.getCell(24).getStringCellValue();
                PULL_TYPE = row.getCell(30).getStringCellValue();
                DELIVERY_SEND = row.getCell(33).getStringCellValue();
                DELIVERY_REC_TYPE = row.getCell(36).getStringCellValue();

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
