package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesSendList;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsLesSendListService extends BaseService<WmsLesSendList> {

    /**
     * 分页查询
     */
    PageData<WmsLesSendList> getPageDataList(int page, int rows, WmsLesSendList wmsLesSendList);

    /**
     * 保存,修改
     */
    public ResultResp save(WmsLesSendList wmsLesSendList);

    /**
     * 删除
     */
    public ResultResp del(WmsLesSendList wmsLesSendList);

    void delByMapSheetNo(String mapSheetNo, String whCode);

    /**
     * 根据mapSheetNo查询
     */
    List<WmsLesSendList> getWmsLesSendList(String mapSheetNo, String whCode);

    /**
     * 根据随箱卡号查询
     */
    List<WmsLesSendList> getWmsLesSendListBySXCarid(String sxCarid, String whCode);
    
    /**
     * 根据配送单号和零件信息查询指定的记录
     */
    List<WmsLesSendList> getInfo(String mapSheetNo, String gcode, String whCode);
}
