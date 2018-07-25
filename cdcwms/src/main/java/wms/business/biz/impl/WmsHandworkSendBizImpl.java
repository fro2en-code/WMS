package wms.business.biz.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkSend;
import com.wms.business.WmsHandworkSendList;
import com.wms.business.WmsTask;

import wms.business.biz.WmsHandworkSendBiz;
import wms.business.service.WmsHandworkSendListService;
import wms.business.service.WmsHandworkSendService;
import wms.business.unit.ExecuteTaskUnit;

@Service("wmsHandworkSendBiz")
public class WmsHandworkSendBizImpl implements WmsHandworkSendBiz {
    @Resource
    private WmsHandworkSendListService wmsHandworkSendListService;
    @Resource
    private WmsHandworkSendService wmsHandworkSendService;
    @Resource(name = "handSendExecuteTaskUnit")
    private ExecuteTaskUnit sendExcecut;

    @Override
    public ResultResp setHandworkSendListReturn(String id, int receiveQty) {
        WmsHandworkSendList handworkSendList = wmsHandworkSendListService.getEntity(id);
        handworkSendList.setReceiveQty(receiveQty);
        wmsHandworkSendListService.updateEntity(handworkSendList);
        return new ResultResp(ResultResp.SUCCESS_CODE, "操作完成");
    }

    @Override
    public ResultResp setHandworkSendReturn(String id) {
        WmsHandworkSend handworkSend = wmsHandworkSendService.getEntity(id);
        if (handworkSend.getStatus() != null && Integer.valueOf(2).equals(handworkSend.getStatus())) {
            handworkSend.setStatus(4);
            wmsHandworkSendService.updateEntity(handworkSend);
            return new ResultResp(ResultResp.SUCCESS_CODE, "操作完成");
        } else {
            return new ResultResp(ResultResp.ERROR_CODE, "当前单据状态不允许进行回单操作");
        }
    }

    @Override
    public ResultResp feedbackHandworkSend(WmsHandworkSend wmsHandworkSend) {
        wmsHandworkSendService.updateEntity(wmsHandworkSend);
        return new ResultResp(ResultResp.SUCCESS_CODE, "反馈成功!");
    }

    /**
     * 保存,修改手工发货单据
     */
    @Override
    public ResultResp saveHandworkSend(WmsHandworkSend wmsHandworkSend) {
        return wmsHandworkSendService.save(wmsHandworkSend);
    }

    /**
     * 保存,修改手工发货明细单
     */
    @Override
    public ResultResp saveHandworkSendList(WmsHandworkSendList wmsHandworkSendList) {
        return wmsHandworkSendListService.save(wmsHandworkSendList);
    }

    /**
     * 手工发货单
     */
    @Override
    public PageData<WmsHandworkSend> getPageDataHandworkSend(int page, int rows, WmsHandworkSend wmsHandworkSend) {
        return wmsHandworkSendService.getPageDataList(page, rows, wmsHandworkSend);
    }

    /**
     * 手工发货明细单
     */
    @Override
    public PageData<WmsHandworkSendList> getPageDataHandworkSendList(int page, int rows,
            WmsHandworkSendList wmsHandworkSendList) {
        return wmsHandworkSendListService.getPageDataList(page, rows, wmsHandworkSendList);
    }

    /**
     * 删除手工发货单据
     */
    @Override
    public void deleteHandworkSend(WmsHandworkSend wmsHandworkSend) {
        // WmsHandworkSend wmsHandworkReceiveList =
        // wmsHandworkSendService.getEntity(wmsHandworkSend.getId());
        wmsHandworkSendService.deleteEntity(wmsHandworkSend);
        // wmsHandworkReceiveListService.delByHandworkReceiveList(wmsHandworkReceiveList.getMapSheetNo());
    }

    /**
     * 删除手工发货明细单
     */
    @Override
    public ResultResp deleteHandworkSendList(WmsHandworkSendList wmsHandworkSendList) {
        return wmsHandworkSendListService.del(wmsHandworkSendList);
    }

    @Override
    public WmsTask createDefaultTaskByBillID(String billId) {
        return sendExcecut.createDefaultTaskByBillID(billId);
    }

    @Override
    public WmsHandworkSend getHandworkSendEntity(Serializable id) {
        return wmsHandworkSendService.getEntity(id);
    }

    @Override
    public WmsHandworkSendList getHandworkSendListEntity(Serializable id) {
        return wmsHandworkSendListService.getEntity(id);
    }

    @Override
    public WmsHandworkSend getInfo(String mapSheetNo, String whCode) {
        return wmsHandworkSendService.getInfo(mapSheetNo, whCode);
    }

    @Override
    public List<WmsHandworkSendList> getWmsHandworkSendList(String mapSheetNo, String whCode) {
        return wmsHandworkSendListService.getWmsHandworkSendList(mapSheetNo, whCode);
    }

}
