package wms.web.business.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.BillBean;
import com.plat.common.beans.FormBean;
import com.plat.common.beans.TaskBean;
import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsDock;
import com.ymt.api.AppApi;
import com.ymt.utils.Lock;
import com.ymt.utils.UserUtils;
import com.ymt.utils.WmsLog;

@Controller
@RequestMapping("/api")
public class ApiAction extends BaseAction {
    @Resource
    private AppApi appApi;
    @Resource
    private UserUtils userUtils;

    /**
     * 变更任务状态
     */
    @RequestMapping(value = "/completeTask.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultResp completeTask(HttpSession session, String data) {
        BillBean bean = getGson().fromJson(data, BillBean.class);
        return appApi.completeTask(bean);
    }

    /**
     * 接受任务
     */
    @Lock("taskId")
    @RequestMapping(value = "/receiveTask.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultResp receiveTask(HttpSession session, String taskId) {
        UserBean user = getUserInfo(session);
        return appApi.receiveTask(taskId, user.getLoginname());
    }

    /**
     * 查询待执行任务(可以接受但未接受+已接受但未完成的任务)
     *
     * 登录名
     *
     * @return 任务列表
     */
    @RequestMapping(value = "/getTask.action", method = RequestMethod.POST)
    @ResponseBody
    public List<TaskBean> getTask(HttpSession session, int page, int rows) {
        UserBean user = getUserInfo(session);
        return appApi.getTask(page, rows, user.getLoginname(), getBindWhCode(session));
    }

    /**
     * 根据任务ID查询任务信息
     *
     * @param taskId
     *            任务Id
     * @return 任务信息
     */
    @RequestMapping(value = "/getTaskByTaskId.action", method = RequestMethod.POST)
    @ResponseBody
    public FormBean getTaskByTaskId(HttpSession session, String taskId) {

        return appApi.getTaskByTaskId(taskId);
    }

    /**
     * 根据任务ID查询历史任务信息
     *
     * @param taskId
     *            任务Id
     * @return 任务信息
     */
    @RequestMapping(value = "/getTaskLogByTaskId.action", method = RequestMethod.POST)
    @ResponseBody
    public FormBean getTaskLogByTaskId(HttpSession session, String taskId) {

        return appApi.getTaskLogByTaskId(taskId);
    }

    /**
     * 根据当前登录人查询任务执行历史
     *
     * 登录名
     *
     * @return 任务信息
     */
    @RequestMapping(value = "/getTaskLog.action", method = RequestMethod.POST)
    @ResponseBody
    public List<TaskBean> getTaskLog(HttpSession session, int page, int rows) {
        UserBean user = getUserInfo(session);
        return appApi.getTaskLog(page, rows, user.getLoginname(), getBindWhCode(session));
    }

    /**
     * 手持设备登录(登录名+密码 或 标签号 两种方式登录)
     *
     * @param loginName
     *            登录名
     * @param password
     *            密码 标签号
     * @return 登录结果
     */
    @WmsLog("loginName")
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map<String, Object> login(HttpSession session, String loginName, String password) {
        Map<String, Object> retMap = new HashMap<>();
        ResultRespT<UserBean> result = appApi.login(session, loginName, password);
        if (!ResultResp.SUCCESS_CODE.equals(result.getRetcode())) {
            retMap.put("retcode", result.getRetcode());
            retMap.put("retmsg", result.getRetmsg());
            return retMap;
        }
        session.setAttribute(StringUtil.USER_SESSION, result.getT());
        retMap.put("retcode", result.getRetcode());
        retMap.put("retmsg", "登录成功");
        retMap.put("userFlag", result.getT().getUserFlag());
        return retMap;
    }

    /**
     * 道口校验 ／ 更换道口
     *
     * @param session
     * @param barcode
     * @return
     */
    @RequestMapping(value = "/validateWmsDock.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultResp getWmsDockByDockCode(HttpSession session, String barcode) {
        UserBean user = getUserInfo(session);
        ResultRespT<WmsDock> respT = appApi.getWmsDockByDockCode(barcode, user.getLoginname());
        if (!"0".equals(respT.getRetcode())) {
            return new ResultResp(respT.getRetcode(), respT.getRetmsg());
        }
        session.setAttribute(StringUtil.WMS_DOCK, respT.getT());
        return new ResultResp(respT.getRetcode(), "道口校验成功");
    }

    /**
     * 道口收货
     *
     * @param session
     * @param dockCode
     *            道口
     * @param sheetNO
     *            对应收货单号
     * @return
     */
    @RequestMapping("/wmsDockRecive")
    @ResponseBody
    public ResultResp wmsDockRecive(HttpSession session, String dockCode, String sheetNO) {
        UserBean user = getUserInfo(session);
        if (null == user) {
            return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, "登录超时");
        }
        //
        return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "收货登记完成");
    }
}
