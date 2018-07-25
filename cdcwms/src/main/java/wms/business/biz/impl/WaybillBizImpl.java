package wms.business.biz.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsHandworkSend;
import com.wms.business.WmsHandworkSendList;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsLesSendList;
import com.wms.business.WmsSendBill;
import com.wms.business.WmsSendBillList;
import com.wms.business.WmsWaybill;
import com.wms.business.WmsWaybillList;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsWarehouse;
import com.ymt.utils.SerialNumberUtils;

import wms.business.biz.WaybillBiz;
import wms.business.service.WmsHandworkSendListService;
import wms.business.service.WmsHandworkSendService;
import wms.business.service.WmsLesSendListService;
import wms.business.service.WmsLesSendService;
import wms.business.service.WmsSendBillListService;
import wms.business.service.WmsSendBillService;
import wms.business.service.WmsWaybillListService;
import wms.business.service.WmsWaybillService;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;

@Service("waybillBiz")
public class WaybillBizImpl implements WaybillBiz {
    @Resource
    private GoodsService goodsService;
    @Resource
    protected InventoryUnit inventoryUnit;
    @Resource
    private StoragService storagService;
    private SerialNumberUtils utils = new SerialNumberUtils() {

        @Override
        public boolean isExpired(Date startTime) {
            return new Date().getTime() / (1000L * 60 * 60 * 24) == startTime.getTime() / (1000L * 60 * 60 * 24);// 是否为同一天
        }
    };

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private WmsHandworkSendListService wmsHandworkSendListService;

    @Resource
    private WmsHandworkSendService wmsHandworkSendService;

    @Resource
    private WmsLesSendListService wmsLesSendListService;

    @Resource
    private WmsLesSendService wmsLesSendService;
    @Resource
    private WmsSendBillListService wmsSendBillListService;
    @Resource
    private WmsSendBillService wmsSendBillService;
    @Resource
    private WmsWaybillListService wmsWaybillListService;
    @Resource
    private WmsWaybillService wmsWaybillService;

    @Override
    public List<WmsSendBill> getDataWmsSendBill(WmsSendBill wmsSendBill) {
        return wmsSendBillService.getDataList(wmsSendBill);
    }

    @Override
    public PageData<WmsWaybill> getPageDataWaybill(int page, int rows, WmsWaybill wmsWaybill) {
        return wmsWaybillService.getPageData(page, rows, wmsWaybill);
    }

    @Override
    public PageData<WmsWaybillList> getPageDataWaybillList(int page, int rows, WmsWaybillList wmsWaybillList) {
        return wmsWaybillListService.getPageData(page, rows, wmsWaybillList);
    }

    @Override
    public PageData<WmsSendBillList> getPageDataWmsSendBillList(int page, int rows, WmsSendBillList wmsSendBillList) {
        return wmsSendBillListService.getPageDataList(page, rows, wmsSendBillList);
    }

    private int sendHandworkSendList(String[] handBill, String wayNumber, String whCode, String wmsStorage) {
        if (null == handBill) {
            return 0;
        }
        int flag = 0;
        for (String string : handBill) {
            WmsHandworkSend send = wmsHandworkSendService.getInfo(string, whCode);
            if (6 != send.getStatus()) {
                continue;
            }
            send.setStatus(BaseModel.INT_COMPLETE);
            wmsHandworkSendService.updateEntity(send);
            WmsWaybillList wmsWaybillList = new WmsWaybillList();
            wmsWaybillList.setNumber(wayNumber);
            wmsWaybillList.setSendBillNumber(string);
            wmsWaybillList.setType("手工发货单");
            wmsWaybillList.setWhCode(whCode);
            wmsWaybillListService.saveEntity(wmsWaybillList);
            List<WmsHandworkSendList> list = wmsHandworkSendListService.getWmsHandworkSendList(string, whCode);
            for (WmsHandworkSendList wmsHandworkSendList : list) {
                WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsHandworkSendList.getPartNo(), send.getGtype(),
                        wmsHandworkSendList.getSupplNo(), whCode);
                inventoryUnit.addWmsStock(wmsStorage, wmsGoods, 0 - wmsHandworkSendList.getReqQty(), true,
                        wmsHandworkSendList.getMapSheetNo());
            }
            flag++;
        }
        return flag;
    }

    private int sendLesSendList(String[] lesBill, String wayNumber, String whCode, String wmsStorage) {
        if (null == lesBill) {
            return 0;
        }
        int flag = 0;
        for (String string : lesBill) {
            WmsLesSend send = wmsLesSendService.getInfo(string, whCode);
            if (6 != send.getStatus()) {
                continue;
            }
            send.setStatus(BaseModel.INT_COMPLETE);
            wmsLesSendService.updateEntity(send);
            WmsWaybillList wmsWaybillList = new WmsWaybillList();
            wmsWaybillList.setNumber(wayNumber);
            wmsWaybillList.setSendBillNumber(string);
            wmsWaybillList.setType("LES发货单");
            wmsWaybillList.setWhCode(whCode);
            wmsWaybillListService.saveEntity(wmsWaybillList);
            List<WmsLesSendList> list = wmsLesSendListService.getWmsLesSendList(string, whCode);
            for (WmsLesSendList wmsLesSendList : list) {
                WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsLesSendList.getPartNo(), send.getGtype(),
                        wmsLesSendList.getSupplNo(), whCode);
                inventoryUnit.addWmsStock(wmsStorage, wmsGoods, 0 - wmsLesSendList.getReqQty(), true,
                        wmsLesSendList.getMapSheetNo());
            }
            flag++;
        }
        return flag;
    }

    @Override
    public void sendTruck(WmsWaybill wmsWaybill, String[] lesBill, String[] handBill) {
        String wayNumber = "YD-" + StringUtil.getCurStringDate("yyyyMMdd") + utils.getSerialNumber(5);
        wmsWaybill.setNumber(wayNumber);
        //
        String whCode = wmsWaybill.getWhCode();
        WmsWarehouse wh = warehouseService.getWmsByCode(whCode);
        int result = 0;
        result += sendHandworkSendList(handBill, wayNumber, whCode, wh.getSendStorage());
        result += sendLesSendList(lesBill, wayNumber, whCode, wh.getSendStorage());
        if (result != 0) {
            wmsWaybillService.saveEntity(wmsWaybill);
        }
    }

}
