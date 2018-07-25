package wms.web.business.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.ymt.api.PublicApi;
import com.ymt.utils.Lock;
import com.ymt.utils.WmsLog;

import wms.business.biz.TaskBiz;

@Controller
@RequestMapping("/task")
public class TaskAction extends BaseAction {
    @Resource
    private PublicApi publicApi;
    @Resource
    private TaskBiz taskBiz;

    @Lock
    @WmsLog()
    @RequestMapping("/cancelTask.action")
    @ResponseBody
    public ResultResp cancelTask(HttpSession session, String id) {
        UserBean user = getUserInfo(session);
        taskBiz.setTaskCancel(id, user.getLoginname());
        return new ResultResp(ResultResp.SUCCESS_CODE, "任务取消");
    }

    @WmsLog()
    @RequestMapping("/complete.action")
    @ResponseBody
    public ResultResp complete(@RequestBody List<WmsTaskBillList> entities) {
        WmsTaskBill wmsTaskBill = taskBiz.getWmsTaskBillEntity(entities.get(0).getParentid());
        wmsTaskBill.setWmsTaskBillLists(entities);
        return publicApi.completeTask(wmsTaskBill.getNextStorageCode(), wmsTaskBill);
    }

    @RequestMapping("/getWmsTaskBillList.action")
    @ResponseBody
    public List<WmsTaskBillList> geWmsTaskBillLists(String id) {
        List<WmsTaskBillList> wmsTaskBillLists = taskBiz.getWmsTaskList(id);
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            wmsTaskBillList.setGoodRealNum(wmsTaskBillList.getGoodNeedNum());
        }
        return wmsTaskBillLists;
    }

    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<WmsTask> list(HttpSession session, int page, int rows, WmsTask task) {
        task.setWhCode(getBindWhCode(session));
        return taskBiz.getWmsTaskPageData(page, rows, task);
    }

    @RequestMapping("/listSub.action")
    @ResponseBody
    public PageData<WmsTaskBillList> listTaskBillList(int page, int rows, String id) {
        return taskBiz.getWmsTaskListPageData(page, rows, id);
    }

    @Lock
    @WmsLog()
    @RequestMapping("/nextTask.action")
    @ResponseBody
    public ResultResp nextTask(HttpSession session, String id) {
        WmsTask wmsTask = taskBiz.getTaskByTaskId(id);
        taskBiz.startNextTask(wmsTask, false);
        return new ResultResp(ResultResp.SUCCESS_CODE, "下一任务启动");
    }

    @Lock
    @WmsLog()
    @RequestMapping("/receiveTask.action")
    @ResponseBody
    public PageData<WmsTask> receiveTask(HttpSession session, String id) {
        UserBean user = getUserInfo(session);
        WmsTask wmsTask = taskBiz.getTaskByTaskId(id);
        taskBiz.setTaskRecive(wmsTask, user.getLoginname());
        return new PageData<WmsTask>();
    }

    /**
     * 渲染页面
     */
    @RequestMapping("/toList.action")
    public String toList() {
        return "taskList";
    }

}
