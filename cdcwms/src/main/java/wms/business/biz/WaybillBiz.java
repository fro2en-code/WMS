package wms.business.biz;

import java.util.List;

import com.plat.common.page.PageData;
import com.wms.business.WmsSendBill;
import com.wms.business.WmsSendBillList;
import com.wms.business.WmsWaybill;
import com.wms.business.WmsWaybillList;

public interface WaybillBiz {
    /**
     * 运单信息分页查询
     */
    PageData<WmsWaybill> getPageDataWaybill(int page, int rows, WmsWaybill wmsWaybill);

    /**
     * 运单信息明细分页查询
     */
    PageData<WmsWaybillList> getPageDataWaybillList(int page, int rows, WmsWaybillList wmsWaybillList);

    List<WmsSendBill> getDataWmsSendBill(WmsSendBill wmsSendBill);

    PageData<WmsSendBillList> getPageDataWmsSendBillList(int page, int rows, WmsSendBillList wmsSendBillList);

    void sendTruck(WmsWaybill wmsWaybill, String[] lesBill, String[] handBill);
}
