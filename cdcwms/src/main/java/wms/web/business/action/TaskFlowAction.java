package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.TaskFlow;

import wms.business.biz.TaskFlowBiz;

/**
 * 任务流程定义
 */
@Controller
@RequestMapping("/taskFlow")
public class TaskFlowAction extends BaseAction {
    @Resource
    private TaskFlowBiz taskFlowBiz;

    @RequestMapping("/toList.action")
    public String toList() {
        return "taskFlowList";
    }

    /**
     * 任务流程分页
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<TaskFlow> list(HttpServletResponse resp, HttpSession session, int page, int rows,
            TaskFlow taskFlow) {
        taskFlow.setWhCode(getBindWhCode(session));
        return taskFlowBiz.getPageDataFlow(page, rows, taskFlow);
    }

    /**
     * 保存,修改任务流程
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public ResultResp saveFlow(HttpSession session, TaskFlow taskFlow) {
        taskFlow.setWhCode(getBindWhCode(session));
        UserBean user = getUserInfo(session);
        taskFlow.setInsertUser(user.getLoginname());
        if (null != taskFlow.getNowPerson() && taskFlow.getNowPerson().length() == 0) {
            taskFlow.setNowPerson(null);
        }
        if (null != taskFlow.getNowRole() && taskFlow.getNowRole().length() == 0) {
            taskFlow.setNowRole(null);
        }
        return taskFlowBiz.saveFlow(taskFlow);
    }

    /**
     * 删除任务流程
     */
    @RequestMapping("/del.action")
    @ResponseBody
    public ResultResp delFlow(HttpSession session, TaskFlow taskFlow) {
        taskFlowBiz.delFlow(taskFlow);
        return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
    }

}
