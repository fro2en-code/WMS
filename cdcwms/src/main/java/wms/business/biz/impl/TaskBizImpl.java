package wms.business.biz.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.ymt.utils.UserUtils;

import wms.business.biz.TaskBiz;
import wms.business.unit.ExecuteTaskUnit;
import wms.business.unit.TaskUnit;

@Service("taskBiz")
public class TaskBizImpl implements TaskBiz {
    private static final Logger logger = LoggerFactory.getLogger(TaskBizImpl.class);
    @Resource(name = "executeTaskList")
    private List<ExecuteTaskUnit> execteTaskUnits;

    // private ExecutorService pool = Executors.newFixedThreadPool(4);// 暂时先开4个线程试试吧

    @Resource
    private TaskUnit taskUnit;

    @Resource
    private UserUtils userUtils;

    @Override
    public ResultResp addTask(WmsTask task) {
        // 这里避免使用原来的对象,免得出问题
        ExecuteTaskUnit unit = getExecuteTaskUnit(task);
        if (null != unit) {
            ResultResp result = unit.createTask(task);
            if (ResultResp.SKIP_CODE.equals(result.getRetcode())) {// 当前任务,需要进入下一任务
                startNextTask(task, true);
            }
            return new ResultResp(ResultResp.SUCCESS_CODE, "添加任务完成");
        } else {
            return new ResultResp(ResultResp.ERROR_CODE, "不存在的任务类型");
        }
    }

    private ExecuteTaskUnit getExecuteTaskUnit(WmsTask task) {
        for (ExecuteTaskUnit executeTaskUnit : execteTaskUnits) {
            if (task.getType() == executeTaskUnit.getTaskType()) {
                return executeTaskUnit;
            }
        }
        throw new RuntimeException("未知的任务类型");
    }

    @Override
    public ResultRespT<List<WmsTask>> getTask(int page, int rows, String user, String whCode) {
        return taskUnit.getWmsTaskData(page, rows, user, whCode);
    }

    @Override
    public List<WmsTask> getTaskByBillID(String billID) {
        return taskUnit.getTaskByBillID(billID);
    }

    @Override
    public WmsTask getTaskByTaskId(String taskId) {
        return taskUnit.getTaskByTaskId(taskId);
    }

    @Override
    public ResultRespT<List<WmsTask>> getTaskHistory(int page, int rows, String user) {
        return taskUnit.getTaskHistory(page, rows, user);
    }

    @Override
    public WmsTaskBill getWmsTaskBillEntity(Serializable id) {
        return taskUnit.getWmsTaskBillEntity(id);
    }

    @Override
    public List<WmsTaskBillList> getWmsTaskList(String wmsTaskId) {
        return taskUnit.getWmsTaskList(wmsTaskId);
    }

    @Override
    public PageData<WmsTaskBillList> getWmsTaskListPageData(int page, int rows, String wmsTaskId) {
        return taskUnit.getWmsTaskListPageData(page, rows, wmsTaskId);
    }

    @Override
    public PageData<WmsTask> getWmsTaskPageData(int page, int rows, WmsTask wmsTask) {
        return taskUnit.getWmsTaskPageData(page, rows, wmsTask);
    }

    @Override
    public void setTaskCancel(String taskId, String user) {
        WmsTask task = getTaskByTaskId(taskId);
        // 新任务,接收 才可以取消
        if (!BaseModel.INT_INIT.equals(task.getStatus()) && !Integer.valueOf(1).equals(task.getStatus())) {
            return;
        }
        ExecuteTaskUnit executeTaskUnit = getExecuteTaskUnit(task);
        executeTaskUnit.setTaskCancel(task);
        taskUnit.cancelTask(task, user);
    }

    @Override
    public void setTaskComplete(WmsTask task) {
        // 已接收才可以完成
        if (!Integer.valueOf(1).equals(task.getStatus())) {
            return;
        }
        ExecuteTaskUnit executeTaskUnit = getExecuteTaskUnit(task);
        taskUnit.completeTask(task);
        executeTaskUnit.setTaskComplete(task);
        startNextTask(task, false);
        // pool.execute(new Runnable() {// 使用子线程生成下一任务,提高响应速度
        // @Override
        // public void run() {
        // try {// 创建下一任务出错,不影响当前任务完成
        // } catch (Exception e) {
        // logger.error("", e);
        // }
        // }
        // });
    }

    @Override
    public void setTaskRecive(WmsTask task, String user) {
        // 新任务才可以接收
        if (!BaseModel.INT_INIT.equals(task.getStatus())) {
            return;
        }
        ExecuteTaskUnit executeTaskUnit = getExecuteTaskUnit(task);
        executeTaskUnit.setTaskRecive(task);
        taskUnit.reciveTask(task, user);
    }

    @Override
    public void startNextTask(WmsTask task, boolean isSkip) {
        Integer nextTaskType = taskUnit.getNextTaskType(task.getType(), task.getWhCode());
        if (null == nextTaskType) {// 无下一任务
            return;
        }
        // 创建下一任务
        if (!isSkip) {// 因为有任务跳过,所以上一任务不变
            task.setRemark(task.getType() + "");// 记录上一任务
        }
        task.setType(nextTaskType);
        ExecuteTaskUnit unit = getExecuteTaskUnit(task);
        // 任务是否全部完成
        if (unit.isAllCompleteNext() && !taskUnit.isAllTaskCompleteByBillId(task.getBillid())) {
            return;
        }
        addTask(task);
    }

}
