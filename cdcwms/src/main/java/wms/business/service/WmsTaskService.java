package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.wms.business.WmsTask;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsTaskService extends BaseService<WmsTask> {
    /**
     * 任务是否全部完居(一个billId 会对应多个任务,只有对应的所有任务全部完成,才可以进入下一环节)
     */
    boolean isAllTaskCompleteByBillId(String billId);

    /**
     * 分页查询
     */
    PageData<WmsTask> getPageData(int page, int rows, WmsTask wmsTask);

    /**
     * 根据billId查询
     */
    List<WmsTask> getwmsTaskList(String billId);

    /**
     * 根据登录用户,查询未完成任务
     */
    List<WmsTask> getWmsTaskData(int page, int rows, String user, String whCode);

}
