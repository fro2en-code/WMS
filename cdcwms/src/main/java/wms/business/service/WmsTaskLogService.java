package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WmsTaskLog;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsTaskLogService extends BaseService<WmsTaskLog> {

    /**
     * 分页查询
     */
    PageData<WmsTaskLog> getPageData(int page, int rows, WmsTaskLog wmsTaskLog);

    /**
     * 根据单据ID查询任务日志
     */
    WmsTaskLog selectLogByBillId(String billId);
}
