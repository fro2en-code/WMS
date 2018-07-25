package wms.business.biz.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.TaskFlow;
import com.ymt.utils.YmtJobScheduler;

import wms.business.biz.TaskFlowBiz;
import wms.business.service.TaskFlowService;
import wms.web.business.action.TaskJob;

@Service("taskFlowBiz")
public class TaskFlowBizImpl implements TaskFlowBiz {
    @Resource
    private TaskFlowService taskFlowService;
    @Resource
    private YmtJobScheduler ymtJobScheduler;

    @Override
    public void delFlow(TaskFlow taskFlow) {
        taskFlow = taskFlowService.getEntity(taskFlow.getId());
        taskFlowService.deleteEntity(taskFlow);
        if (StringUtils.isNotEmpty(taskFlow.getCron())) {
            try {
                ymtJobScheduler.removeJob(new TaskJob(taskFlow.getId(), taskFlow.getCron()));
            } catch (SchedulerException e) {
                throw new RuntimeException("删除定时器出错");
            }
        }
    }

    @Override
    public PageData<TaskFlow> getPageDataFlow(int page, int rows, TaskFlow taskFlow) {
        return taskFlowService.getPageDataList(page, rows, taskFlow);
    }

    @Override
    public TaskFlow getTaskFlow(String id) {
        return taskFlowService.getEntity(id);
    }

    @Override
    public ResultResp saveFlow(TaskFlow taskFlow) {
        ResultResp result = taskFlowService.save(taskFlow);
        if (StringUtils.isNotEmpty(taskFlow.getCron())) {
            try {
                ymtJobScheduler.addJob(new TaskJob(result.getBillId(), taskFlow.getCron()));
            } catch (SchedulerException e) {
                throw new RuntimeException("添加定时器出错");
            }
        }
        return result;
    }

}
