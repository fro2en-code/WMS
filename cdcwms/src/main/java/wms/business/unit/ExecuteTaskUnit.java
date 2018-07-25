package wms.business.unit;

import java.util.Set;

import com.plat.common.result.ResultResp;
import com.wms.business.WmsTask;


public interface ExecuteTaskUnit {
    /**
     * 获取任务类型代码
     */
    int getTaskType();

    /**
     * 根据ID 创建默认任务
     *
     * @param billID
     * @return
     */
    WmsTask createDefaultTaskByBillID(String billID);

    /**
     * 创建任务
     */
    ResultResp createTask(WmsTask task);

    /**
     * 获取任务执行人
     */
    Set<String> getExector(WmsTask task);

    /**
     * 标识任务完成(task状态不需要变更,调用方法统一变)
     */
    void setTaskComplete(WmsTask task);

    /**
     * 标识任务接收(task状态不需要变更,调用方法统一变)
     */
    void setTaskRecive(WmsTask task);

    /**
     * 标识任务取消(task状态不需要变更,调用方法统一变更)
     *
     * @param task
     */
    void setTaskCancel(WmsTask task);

    /**
     * 是否所有子任务完成才能执行下一任务
     *
     * @return
     */
    boolean isAllCompleteNext();
}
