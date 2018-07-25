package wms.business.biz.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesReceive;
import com.wms.business.WmsLesReceiveList;

import wms.business.biz.WmsLesReceiveBiz;
import wms.business.service.WmsLesReceiveListService;
import wms.business.service.WmsLesReceiveService;

@Service("wmsLesReceiveBiz")
public class WmsLesReceiveBizImpl implements WmsLesReceiveBiz {
    @Resource
    private WmsLesReceiveListService wmsLesReceiveListService;
    @Resource
    private WmsLesReceiveService wmsLesReceiveService;

    @Override
    public void deleteLesReceive(WmsLesReceive wmsLesReceive) {
        WmsLesReceive wmsLesReceiveList = wmsLesReceiveService.getEntity(wmsLesReceive.getId());
        wmsLesReceiveService.deleteEntity(wmsLesReceiveList);
        wmsLesReceiveListService.delByHandworkReceiveList(wmsLesReceiveList.getMapSheetNo(), wmsLesReceive.getWhCode());
    }

    @Override
    public ResultResp deleteLesReceiveList(WmsLesReceiveList wmsLesReceiveList) {
        return wmsLesReceiveListService.del(wmsLesReceiveList);
    }

    @Override
    public List<WmsLesReceiveList> getLesReceiveList(String mapSheetNo, String whCode) {
        return wmsLesReceiveListService.getLesReceiveList(mapSheetNo, whCode);
    }

    @Override
    public PageData<WmsLesReceive> getPageDataLesReceive(int page, int rows, WmsLesReceive wmsLesReceive) {
        return wmsLesReceiveService.getpageDataLesReceive(page, rows, wmsLesReceive);
    }

    @Override
    public PageData<WmsLesReceiveList> getPageDataLesReceiveList(int page, int rows,
            WmsLesReceiveList wmsLesReceiveList) {
        return wmsLesReceiveListService.getPageDataLesReceiveList(page, rows, wmsLesReceiveList);
    }

    @Override
    public WmsLesReceive getWmsLesReceiveEntity(Serializable id) {
        return wmsLesReceiveService.getEntity(id);
    }

    @Override
    public ResultResp saveLesReceive(WmsLesReceive wmsLesReceive) {
        return wmsLesReceiveService.save(wmsLesReceive);
    }

    @Override
    public ResultResp saveLesReceiveBill(WmsLesReceive wmsLesReceive) {
        String id = null;
        WmsLesReceive rec = wmsLesReceiveService.getLesReceiveByMapSheetNo(wmsLesReceive.getMapSheetNo(),
                wmsLesReceive.getWhCode());
        if (null != rec) {// 修改
            if (!BaseModel.INT_INIT.equals(rec.getStatus())) {// 已操作,不可以修改
                throw new RuntimeException(wmsLesReceive.getMapSheetNo() + "已操作,不允许修改");
            }
            id = rec.getId();
            wmsLesReceive.setId(id);
            wmsLesReceiveService.updateEntity(wmsLesReceive);
            wmsLesReceiveListService.delByHandworkReceiveList(wmsLesReceive.getMapSheetNo(), wmsLesReceive.getWhCode());
        } else {
            wmsLesReceive.setStatus(BaseModel.INT_INIT);
            id = (String) wmsLesReceiveService.saveEntity(wmsLesReceive);
        }
        List<WmsLesReceiveList> list = wmsLesReceive.getWmsLesReceiveLists();
        for (WmsLesReceiveList lesReceiveList : list) {
            lesReceiveList.setSendQty(lesReceiveList.getReqQty());// 发货数量==需求数量
            wmsLesReceiveListService.saveEntity(lesReceiveList);
        }
        return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG, id);
    }

    @Override
    public ResultResp saveLesReceiveList(WmsLesReceiveList wmsLesReceiveList) {
        return wmsLesReceiveListService.save(wmsLesReceiveList);
    }

}
