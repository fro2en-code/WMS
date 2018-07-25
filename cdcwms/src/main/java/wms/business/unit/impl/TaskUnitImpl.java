package wms.business.unit.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.plat.common.utils.StringUtil;
import com.wms.business.TaskFlow;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;

import wms.business.service.TaskFlowService;
import wms.business.service.WmsTaskBillListService;
import wms.business.service.WmsTaskBillService;
import wms.business.service.WmsTaskService;
import wms.business.unit.TaskUnit;

@Service("taskUnit")
public class TaskUnitImpl implements TaskUnit {
    @Autowired
    private WmsTaskBillListService billListService;
    @Autowired
    private WmsTaskBillService billService;
    @Resource
    private TaskFlowService taskFlowService;
    @Autowired
    private WmsTaskService taskService;

    @Override
    public ResultResp addTask(WmsTask task, WmsTaskBill taskBill, WmsTaskBillList... billLists) {
        // 这里是统一调用,做了很多额外的工作,如统一billID,whCode,系统设计时冗余字段太多,不能保证他们一定设置,只能在这里做
        String billId = task.getBillid();
        String whCode = task.getWhCode();
        String taskId = (String) taskService.saveEntity(task);
        taskBill.setTaskid(taskId);
        taskBill.setBillid(billId);
        taskBill.setWhCode(whCode);
        String taskBillId = (String) billService.saveEntity(taskBill);
        String oraCode = taskBill.getOraCode();// oraCode
                                               // 有时明细里有,主表没有,如果主表里面有,则与主表相同,否则就不管
        String oraName = taskBill.getOraName();
        List<WmsTaskBillList> list = new ArrayList<>();
        for (WmsTaskBillList billList : billLists) {
            billList.setParentid(taskBillId);
            billList.setBillid(billId);
            String billListId = (String) billListService.saveEntity(billList);
            billList.setId(billListId);
            if (null != oraCode) {
                billList.setOraCode(oraCode);
                billList.setOraName(oraName);
            }
            list.add(billList);
        }
        task.setWmsTaskBill(taskBill);
        taskBill.setWmsTaskBillLists(list);
        //
        return new ResultResp(ResultResp.SUCCESS_CODE, "新增任务完成", taskId);
    }

    @Override
    public ResultResp cancelAndAddTask(String taskId, String updateUser) {
        WmsTask task = getTaskByTaskId(taskId);
        ResultResp result = cancelTask(task, updateUser);
        if (!ResultResp.SUCCESS_CODE.equals(result.getRetcode())) {
            return result;
        }
        // 清空旧值
        task.setId(null);
        task.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        task.setAcceptTime(null);
        task.setEndTime(null);
        task.setExecutorName(null);
        //
        WmsTaskBill taskBill = task.getWmsTaskBill();
        taskBill.setStatus(0);
        //
        List<WmsTaskBillList> list = taskBill.getWmsTaskBillLists();
        addTask(task, taskBill, list.toArray(new WmsTaskBillList[list.size()]));
        return new ResultResp(ResultResp.SUCCESS_CODE, "取消并新增任务完成,新任务需要手动更新才能看到");
    }

    @Override
    public ResultResp cancelTask(WmsTask task, String updateUser) {
        task.setEndTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        task.setStatus(3);
        task.setUpdateUser(updateUser);
        WmsTaskBill taskBill = task.getWmsTaskBill();
        taskBill.setStatus(2);
        taskBill.setUpdateUser(updateUser);
        taskService.updateEntity(task);
        billService.updateEntity(taskBill);
        return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
    }

    @Override
    public ResultResp completeTask(WmsTask task) {
        task.setEndTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        task.setStatus(2);
        task.setUpdateUser(task.getExecutorName());
        WmsTaskBill taskBill = task.getWmsTaskBill();
        taskBill.setStatus(1);
        taskBill.setUpdateUser(task.getExecutorName());
        taskService.updateEntity(task);
        billService.updateEntity(taskBill);
        List<WmsTaskBillList> list = taskBill.getWmsTaskBillLists();
        for (WmsTaskBillList wmsTaskBillList : list) {
            billListService.updateEntity(wmsTaskBillList);
        }
        return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
    }

    @Override
    public Integer getNextTaskType(int taskType, String whCode) {
        TaskFlow taskFlow = taskFlowService.getTaskFlowByTaskCode(taskType, whCode);
        if (null != taskFlow) {
            return taskFlow.getNextTaskCode();
        } else {
            return null;
        }
    }

    @Override
    public List<WmsTask> getTaskByBillID(String billID) {
        return taskService.getwmsTaskList(billID);
    }

    @Override
    public WmsTask getTaskByTaskId(String taskId) {
        WmsTask task = taskService.getEntity(taskId);
        WmsTaskBill taskBill = billService.getTaskBillByTaskId(taskId);
        List<WmsTaskBillList> list = billListService.getBillListByParentId(taskBill.getId());
        task.setWmsTaskBill(taskBill);
        taskBill.setWmsTaskBillLists(list);
        return task;
    }

    @Override
    public ResultRespT<List<WmsTask>> getTaskHistory(int page, int rows, String user) {
        WmsTask wmsTask = new WmsTask();
        wmsTask.setExecutorName(user);
        wmsTask.setRemark("2");
        PageData<WmsTask> pageData = taskService.getPageData(page, rows, wmsTask);
        return new com.plat.common.result.ResultRespT<>(ResultResp.SUCCESS_CODE, "查询成功", pageData.getRows());
    }

    @Override
    public ResultRespT<List<WmsTaskBillList>> getWmsTaskBillListData(String billId, String whCode) {
        return new com.plat.common.result.ResultRespT<>(ResultResp.SUCCESS_CODE, "查询完成",
                billListService.getBillListByBillId(billId, whCode));
    }

    @Override
    public ResultRespT<List<WmsTask>> getWmsTaskData(int page, int rows, String logingname, String whCode) {
        List<WmsTask> list = taskService.getWmsTaskData(page, rows, logingname, whCode);
        return new com.plat.common.result.ResultRespT<>(ResultResp.SUCCESS_CODE, "查询完成", list);
    }

    @Override
    public PageData<WmsTaskBillList> getWmsTaskListPageData(int page, int rows, String wmsTaskId) {
        WmsTaskBill taskBill = billService.getTaskBillByTaskId(wmsTaskId);
        WmsTaskBillList billList = new WmsTaskBillList();
        billList.setBillid(taskBill.getBillid());
        billList.setParentid(taskBill.getId());
        return billListService.getPageData(page, rows, billList);
    }

    @Override
    public PageData<WmsTask> getWmsTaskPageData(int page, int rows, WmsTask wmsTask) {
        return taskService.getPageData(page, rows, wmsTask);
    }

    @Override
    public boolean isAllTaskCompleteByBillId(String billId) {
        return taskService.isAllTaskCompleteByBillId(billId);
    }

    @Override
    public Boolean lesReciveTaskExist(String boxCarid) {
        return billService.lesReciveTaskExist(boxCarid);
    }

    @Override
    public ResultResp reciveTask(WmsTask task, String user) {
        task.setAcceptTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        task.setStatus(1);
        task.setUpdateUser(user);
        task.setExecutorName(user);
        taskService.updateEntity(task);
        return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
    }

    /**
     * 查询任务明细,不分页
     */
    @Override
    public List<WmsTaskBillList> getWmsTaskList(String wmsTaskId) {
        WmsTaskBill taskBill = billService.getTaskBillByTaskId(wmsTaskId);
        WmsTaskBillList billList = new WmsTaskBillList();
        billList.setBillid(taskBill.getBillid());
        billList.setParentid(taskBill.getId());
        return billListService.getBillList(billList);
    }

    @Override
    public WmsTaskBill getWmsTaskBillEntity(Serializable id) {
        return billService.getEntity(id);
    }
}
