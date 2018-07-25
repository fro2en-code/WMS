package wms.business.unit;

import java.io.Serializable;
import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;

/**
 * 任务单元
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年3月22日
 */
public interface TaskUnit {
    /**
     * 添加任务
     */
    ResultResp addTask(WmsTask task, WmsTaskBill taskBill, WmsTaskBillList... billList);

    /**
     * 取消且新增任务
     */
    ResultResp cancelAndAddTask(String taskId, String updateUser);

    /**
     * 取消任务
     */
    ResultResp cancelTask(WmsTask task, String updateUser);

    /*
     * 任务完成
     */
    ResultResp completeTask(WmsTask task);

    /**
     * 获取下一任务的任务类型
     */
    Integer getNextTaskType(int taskType, String whCode);

    List<WmsTask> getTaskByBillID(String billID);

    /**
     * 根据任务ID获取任务信息,任务单据,任务单据明细
     */
    WmsTask getTaskByTaskId(String taskId);

    /**
     * 查询任务执行历史
     */
    ResultRespT<List<WmsTask>> getTaskHistory(int page, int rows, String user);

    WmsTaskBill getWmsTaskBillEntity(Serializable id);

    /**
     * 根据billid 获取任务明细(一个主单会产生多个任务)
     */
    ResultRespT<List<WmsTaskBillList>> getWmsTaskBillListData(String billId, String whCode);

    /**
     * 根据登录用户,查询未完成任务
     */
    ResultRespT<List<WmsTask>> getWmsTaskData(int page, int rows, String logingname, String whCode);

    /**
     * 查询任务明细,不分页
     */
    List<WmsTaskBillList> getWmsTaskList(String wmsTaskId);

    /**
     * 查询任务分页
     */
    PageData<WmsTaskBillList> getWmsTaskListPageData(int page, int rows, String wmsTaskId);

    /**
     * 查询任务分页
     */
    PageData<WmsTask> getWmsTaskPageData(int page, int rows, WmsTask wmsTask);

    /**
     * 任务是否全部完居(一个billId 会对应多个任务,只有对应的所有任务全部完成,才可以进入下一环节)
     */
    boolean isAllTaskCompleteByBillId(String billId);

    Boolean lesReciveTaskExist(String boxCarid);

    /*
     * 任务接收
     */
    ResultResp reciveTask(WmsTask task, String user);
}
