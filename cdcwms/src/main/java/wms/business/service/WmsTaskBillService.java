package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WmsTaskBill;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsTaskBillService extends BaseService<WmsTaskBill> {

    /**
     * 分页查询
     */
    PageData<WmsTaskBill> getPageData(int page, int rows, WmsTaskBill wmsTaskBill);

    WmsTaskBill getTaskBillByTaskId(String taskId);

    void updateStatusByTaskId(String taskId, Integer status, String updateUser);

    Boolean lesReciveTaskExist(String boxCarid);
}
