package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.wms.business.WmsTaskBillList;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsTaskBillListService extends BaseService<WmsTaskBillList> {

    /**
     * 分页查询
     */
    PageData<WmsTaskBillList> getPageData(int page, int rows, WmsTaskBillList wmsTaskBillList);

    List<WmsTaskBillList> getBillListByParentId(String parentId);

    List<WmsTaskBillList> getBillListByBillId(String billId, String whCode);

    /**
     * 查询所有任务明细的数据
     */
    List<WmsTaskBillList> getBillList(WmsTaskBillList wmsTaskBillList);
}
