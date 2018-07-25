package wms.business.biz;

import java.io.Serializable;
import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;

/**
 * 任务接口
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年3月22日
 */
public interface TaskBiz {
    /**
     * 添加任务
     *
     * @param task
     *            如果是任务第一个环节则会有:任务描述,单据ID,仓库代码,任务优先级,单据类型;否则将会是上一环节的任务(除了任务类型,别的都不变)
     */
    ResultResp addTask(WmsTask task);

    /**
     * 查询待执行任务
     */
    ResultRespT<List<WmsTask>> getTask(int page, int rows, String user, String whCode);

    List<WmsTask> getTaskByBillID(String billID);

    /**
     * 根据任务ID获取任务信息,任务单据,任务单据明细
     */
    WmsTask getTaskByTaskId(String taskId);

    /**
     * 根据当前登录人查询任务执行历史
     */
    ResultRespT<List<WmsTask>> getTaskHistory(int page, int rows, String user);

    WmsTaskBill getWmsTaskBillEntity(Serializable id);

    List<WmsTaskBillList> getWmsTaskList(String wmsTaskId);

    /**
     * 分页查询
     */
    PageData<WmsTaskBillList> getWmsTaskListPageData(int page, int rows, String wmsTaskId);

    /**
     * 分页查询
     */
    PageData<WmsTask> getWmsTaskPageData(int page, int rows, WmsTask wmsTask);

    /**
     * 标识任务取消
     */
    void setTaskCancel(String taskId, String user);

    /**
     * 标识任务完成
     */
    void setTaskComplete(WmsTask task);

    /**
     * 标识任务完成
     */
    void setTaskRecive(WmsTask task, String user);

    /**
     * 开始下一任务
     */
    void startNextTask(WmsTask task, boolean isSkip);
}
