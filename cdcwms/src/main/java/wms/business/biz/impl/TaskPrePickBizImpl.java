package wms.business.biz.impl;

import javax.annotation.Resource;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.TaskPrePick;
import com.wms.business.WmsTask;
import com.ymt.utils.YmtJobScheduler;

import wms.business.biz.TaskPrePickBiz;
import wms.business.service.TaskPicPickService;
import wms.business.unit.ExecuteTaskUnit;
import wms.web.business.action.PrePickJob;

@Service("taskPrePickBiz")
public class TaskPrePickBizImpl implements TaskPrePickBiz {
    @Resource
    private YmtJobScheduler ymtJobScheduler;
    @Resource(name = "prePickExecuteTaskUnit")
    private ExecuteTaskUnit executeTaskUnit;
    @Resource
    private TaskPicPickService taskPicPickService;

    @Override
    public void delPick(TaskPrePick taskPrePick) throws SchedulerException {
        ymtJobScheduler.removeJob(new PrePickJob(taskPrePick.getId(), taskPrePick.getExpression()));
        taskPicPickService.deleteEntity(taskPrePick);
    }

    @Override
    public PageData<TaskPrePick> getPageDataPick(int page, int rows, TaskPrePick taskPrePick) {
        return taskPicPickService.getPageDataList(page, rows, taskPrePick);
    }

    @Override
    public TaskPrePick getPrePickById(String id) {
        return taskPicPickService.getEntity(id);
    }

    @Override
    public ResultResp savePick(TaskPrePick taskPrePick) throws Exception {
        ResultResp result = null;
        if (null == taskPrePick.getId()) {
            result = taskPicPickService.save(taskPrePick);
            ymtJobScheduler.addJob(new PrePickJob(result.getBillId(), taskPrePick.getExpression()));
        } else {
            result = taskPicPickService.save(taskPrePick);
            ymtJobScheduler.updateJob(new PrePickJob(result.getBillId(), taskPrePick.getExpression()));
        }
        return result;
    }

    @Override
    public WmsTask createDefaultTaskByBillID(String billId) {
        return executeTaskUnit.createDefaultTaskByBillID(billId);
    }

}
