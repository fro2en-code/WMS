package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkReceiveList;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsHandworkReceiveListService extends BaseService<WmsHandworkReceiveList> {

    /**
     * 删除
     */
    public ResultResp del(WmsHandworkReceiveList wmsHandworkReceiveList);

    /**
     * 根据配送单号删除手工收货单明细表
     */
    void delByHandworkReceiveList(String mapSheetNo, String whCode);

    /**
     * 分页查询
     */
    PageData<WmsHandworkReceiveList> getPageData(int page, int rows, WmsHandworkReceiveList wmsHandworkReceiveList);

    /**
     *
     */
    PageData<WmsHandworkReceiveList> getPageDataList(int page, int rows, WmsHandworkReceiveList wmsHandworkReceiveList);

    List<WmsHandworkReceiveList> getWmsHandworkReceiveList(String mapSheetNo, String whCode);

    /**
     * 保存,修改
     */
    public ResultResp save(WmsHandworkReceiveList wmsHandworkReceiveList);

    /**
     * 保存新数据
     */
    void saveWmsHandworkReceiveList(WmsHandworkReceiveList wmsHandworkReceiveList);
}
