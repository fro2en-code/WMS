package wms.business.biz;

import java.io.Serializable;
import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesReceive;
import com.wms.business.WmsLesReceiveList;

public interface WmsLesReceiveBiz {
    void deleteLesReceive(WmsLesReceive wmsLesReceive);

    ResultResp deleteLesReceiveList(WmsLesReceiveList wmsLesReceiveList);

    List<WmsLesReceiveList> getLesReceiveList(String mapSheetNo, String whCode);

    PageData<WmsLesReceive> getPageDataLesReceive(int page, int rows, WmsLesReceive wmsLesRece);

    PageData<WmsLesReceiveList> getPageDataLesReceiveList(int page, int rows, WmsLesReceiveList wmsLesReceiveList);

    WmsLesReceive getWmsLesReceiveEntity(Serializable id);

    /**
     * 保存,修改Les收货单据
     */
    ResultResp saveLesReceive(WmsLesReceive wmsLesReceive);

    /**
     * 保存,修改Les收货单据及明细
     */
    ResultResp saveLesReceiveBill(WmsLesReceive wmsLesReceive);

    ResultResp saveLesReceiveList(WmsLesReceiveList wmsLesReceiveList);
}
