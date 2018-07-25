package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.TaskFlow;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface TaskFlowService extends BaseService<TaskFlow> {
    TaskFlow getTaskFlowByTaskCode(int taskCode, String whCode);

    /**
     * 任务流程定义分页
     */
    PageData<TaskFlow> getPageDataList(int page, int rows, TaskFlow taskFlow);

    /**
     * 保存,修改任务流程
     */
    public ResultResp save(TaskFlow taskFlow);

}
