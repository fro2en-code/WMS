package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesReceiveList;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsLesReceiveListService extends BaseService<WmsLesReceiveList> {

    /**
     * 分页查询
     */
    PageData<WmsLesReceiveList> getPageData(int page, int rows, WmsLesReceiveList wmsLesReceiveList);

    /**
    *
    */
    PageData<WmsLesReceiveList> getPageDataLesReceiveList(int page, int rows, WmsLesReceiveList wmsLesReceiveList);

    List<WmsLesReceiveList> getLesReceiveList(String billid, String whCode);

    /**
     * 根据配送单号删除手工收货单明细表
     */
    void delByHandworkReceiveList(String mapSheetNo, String whCode);

    /**
     * 保存,修改
     */
    public ResultResp save(WmsLesReceiveList wmsLesReceiveList);

    /**
     * 删除
     */
    public ResultResp del(WmsLesReceiveList wmsLesReceiveList);

    /**
     * 根据配送单号和随箱卡号查询明细表
     */
    public WmsLesReceiveList getLesReceiveList(String mapSheetNo, String sxCardNo, String whCode);

    /**
     *
     */
    public void saveNewLesReceiveList(WmsLesReceiveList wmsLesReceiveList);
}
