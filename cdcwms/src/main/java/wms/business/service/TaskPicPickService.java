package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.TaskPrePick;
import its.base.service.BaseService;

/**
 * 预拣货任务定义
 * @author wangzz
 *
 */
public interface TaskPicPickService extends BaseService<TaskPrePick> {
    /**
     * 预拣货任务定义分页
     */
    PageData<TaskPrePick> getPageDataList(int page, int rows, TaskPrePick taskPrePick);
    
    /**
     * 保存,修改预拣货任务定义
     */
    public ResultResp save(TaskPrePick taskPrePick);
    
    /**
     * 删除预拣货任务定义
     */
    public ResultResp del(TaskPrePick taskPrePick);
}

