package wms.business.biz;

import java.io.Serializable;
import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkSend;
import com.wms.business.WmsHandworkSendList;
import com.wms.business.WmsTask;

public interface WmsHandworkSendBiz {
    WmsTask createDefaultTaskByBillID(String billId);

    /**
     * 删除手工发货单据
     */
    void deleteHandworkSend(WmsHandworkSend wmsHandworkSend);

    /**
     * 删除手工发货明细单
     */
    ResultResp deleteHandworkSendList(WmsHandworkSendList wmsHandworkSendList);

    /**
     * 对手工发货单进行附属单反馈
     */
    ResultResp feedbackHandworkSend(WmsHandworkSend wmsHandworkSend);

    WmsHandworkSend getHandworkSendEntity(Serializable id);

    WmsHandworkSendList getHandworkSendListEntity(Serializable id);

    WmsHandworkSend getInfo(String mapSheetNo, String whCode);

    List<WmsHandworkSendList> getWmsHandworkSendList(String mapSheetNo, String whCode);

    /**
     * 手工发货单据分页
     */
    PageData<WmsHandworkSend> getPageDataHandworkSend(int page, int rows, WmsHandworkSend wmsHandworkSend);

    /**
     * 手工发货单据明细分页
     */
    PageData<WmsHandworkSendList> getPageDataHandworkSendList(int page, int rows,
            WmsHandworkSendList wmsHandworkSendList);

    /**
     * 保存,修改手工发货单据
     */
    ResultResp saveHandworkSend(WmsHandworkSend wmsHandworkSend);

    /**
     * 保存,修改手工发货明细单
     */
    ResultResp saveHandworkSendList(WmsHandworkSendList wmsHandworkSendList);

    /**
     * 设置手工发货 回单
     */
    ResultResp setHandworkSendListReturn(String id, int receiveQty);

    /**
     * 设置手工发货 回单
     */
    public ResultResp setHandworkSendReturn(String id);

}
