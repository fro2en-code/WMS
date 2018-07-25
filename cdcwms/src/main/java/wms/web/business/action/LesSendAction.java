package wms.web.business.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.BaseModel;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsTask;
import com.ymt.utils.Lock;

import wms.business.biz.TaskBiz;
import wms.business.biz.WmsLesSendBiz;

/**
 * les发货单据
 *
 */
@Controller
@RequestMapping("/lesSend")
public class LesSendAction extends BaseAction {
    @Resource
    private TaskBiz taskBiz;
    @Resource
    private WmsLesSendBiz wmsLesSendBiz;

    @RequestMapping("/cancel.action")
    @ResponseBody
    @Lock("billId")
    public ResultResp cancel(HttpSession session, String billId) {
        UserBean user = getUserInfo(session);
        WmsLesSend lesSend = wmsLesSendBiz.getWmsLesSendEntity(billId);
        if (BaseModel.INT_INIT.equals(lesSend.getStatus())) {
            lesSend.setStatus(BaseModel.INT_ERROR);
            lesSend.setRemark("任务手动取消,操作人:" + user.getLoginname());
            wmsLesSendBiz.updateWmsLesSendEntity(lesSend);
            return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
        } else if (BaseModel.INT_CREATE.equals(lesSend.getStatus())) {
            List<WmsTask> list = taskBiz.getTaskByBillID(billId);
            for (WmsTask wmsTask : list) {
                if (BaseModel.INT_INIT.equals(wmsTask.getStatus())
                        || BaseModel.INT_CREATE.equals(wmsTask.getStatus())) {
                    taskBiz.setTaskCancel(wmsTask.getId(), user.getLoginname());// 未完成的任务取消
                }
            }
            lesSend.setStatus(BaseModel.INT_ERROR);
            lesSend.setRemark("任务手动取消,操作人:" + user.getLoginname());
            wmsLesSendBiz.updateWmsLesSendEntity(lesSend);
            return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
        } else {
            return new ResultResp(ResultResp.ERROR_CODE, "已取消或已完成任务不允许取消");
        }

    }

    @RequestMapping("/addTask.action")
    @ResponseBody
    @Lock("billId")
    public ResultResp addTask(HttpSession session, String billId) {
        WmsTask task = wmsLesSendBiz.createDefaultTaskByBillID(billId);
        task.setWhCode(getBindWhCode(session));
        return taskBiz.addTask(task);
    }

    @RequestMapping("/setReturn.action")
    @ResponseBody
    public ResultResp setReturn(String id) {
        return wmsLesSendBiz.setLesSendReturn(id);
    }

    @RequestMapping("/toList.action")
    public String toList() {
        return "lesSendList";
    }

    /**
     * les发货单据分页
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<WmsLesSend> list(HttpServletResponse resp, HttpSession session, int page, int rows,
            WmsLesSend wmsLesSend) {
        wmsLesSend.setWhCode(getBindWhCode(session));
        return wmsLesSendBiz.getPageDataLesSend(page, rows, wmsLesSend);
    }

    /**
     * 保存,修改les发货单
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public ResultResp saveHandwork(HttpSession session, WmsLesSend wmsLesSend) {
        if (StringUtil.isEmpty(wmsLesSend.getId())) {// 新增
            wmsLesSend.setWhCode((String) session.getAttribute("defaultWhCode"));
            UserBean user = getUserInfo(session);
            wmsLesSend.setUpdateUser(user.getLoginname());
            return wmsLesSendBiz.saveLesSend(wmsLesSend);
        } else {
            WmsLesSend lesSend = wmsLesSendBiz.getWmsLesSendEntity(wmsLesSend.getId());
            if (BaseModel.INT_INIT.equals(lesSend.getStatus()) || BaseModel.INT_ERROR.equals(lesSend.getStatus())) {
                wmsLesSend.setWhCode((String) session.getAttribute("defaultWhCode"));
                UserBean user = getUserInfo(session);
                wmsLesSend.setUpdateUser(user.getLoginname());
                return wmsLesSendBiz.saveLesSend(wmsLesSend);
            } else {
                return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能修改!");
            }
        }
    }

    /**
     * 删除les发货单
     */
    @RequestMapping("/del.action")
    @ResponseBody
    public ResultResp delHandwork(HttpSession session, WmsLesSend wmsLesSend) {
        WmsLesSend lesSend = wmsLesSendBiz.getWmsLesSendEntity(wmsLesSend.getId());
        if (BaseModel.INT_INIT.equals(lesSend.getStatus())) {
            wmsLesSendBiz.deleteLesSend(wmsLesSend);
            return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
        } else {
            return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能删除!");
        }
    }
}
