package wms.business.biz;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.TaskFlow;

public interface TaskFlowBiz {
    PageData<TaskFlow> getPageDataFlow(int page, int rows, TaskFlow taskFlow);

    ResultResp saveFlow(TaskFlow taskFlow);

    void delFlow(TaskFlow taskFlow);

    TaskFlow getTaskFlow(String id);
}
