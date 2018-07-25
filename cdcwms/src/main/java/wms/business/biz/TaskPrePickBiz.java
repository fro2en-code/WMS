package wms.business.biz;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.TaskPrePick;
import com.wms.business.WmsTask;

public interface TaskPrePickBiz {
    WmsTask createDefaultTaskByBillID(String billId);

    /**
     * 删除预拣货任务
     */
    void delPick(TaskPrePick taskPrePick) throws Exception;

    /**
     * 预拣货任务定义分页
     */
    PageData<TaskPrePick> getPageDataPick(int page, int rows, TaskPrePick taskPrePick);

    TaskPrePick getPrePickById(String id);

    /**
     * 新增修改预拣货任务
     */
    ResultResp savePick(TaskPrePick taskPrePick) throws Exception;
}
