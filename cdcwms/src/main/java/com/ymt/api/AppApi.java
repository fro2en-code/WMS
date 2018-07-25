package com.ymt.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.plat.common.beans.BillBean;
import com.plat.common.beans.FormBean;
import com.plat.common.beans.TaskBean;
import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.warehouse.WmsDock;


/**
 * <p>
 * 3PL 对手持的接口实现
 * </p>
 * 关于 rabbitMQ 消息,消息返回结果为 Map<String,Object>
 * <li>taskId,String,任务Id</li>
 * <li>taskdesc,String,任务描述</li>
 * <li>Level,int,任务优先级 0普通1加急</li>
 *
 * @version 1.0
 *
 * @author zhouxianglh@gmail.com
 *
 * @snce 2017年3月29日
 */
public interface AppApi {
    /**
     * 接受任务
     *
     * @param taskId
     * @param loginName
     * @return
     */
    ResultResp receiveTask(String taskId, String loginName);

    /**
     * 变更任务状态
     *
     * @param billBean
     *            Map
     *            <li>TaskBean,TaskBean,任务对象</li>
     *            <li>status,int,变更状态:1.接收2.完成 3.取消</li>
     *            <li>loginName,String,登录名</li>
     * @return 操作结果
     */
    ResultResp completeTask(BillBean billBean);

    /**
     * 查询待执行任务(可以接受但未接受+已接受但未完成的任务)
     *
     * @param loginname
     *            登录名
     * @return 任务列表
     */
    List<TaskBean> getTask(int page, int rows, String loginname, String whCode);

    /**
     * 根据任务ID查询任务信息
     *
     * @param taskId
     *            任务Id
     * @return 任务信息
     */
    FormBean getTaskByTaskId(String taskId);

    /**
     * 根据当前登录人查询任务执行历史
     *
     * @param loginName
     *            登录名
     * @return 任务信息
     */
    List<TaskBean> getTaskLog(int page, int rows, String loginName, String whCode);

    /**
     * 根据任务ID查询历史任务信息
     *
     * @param taskId
     *            任务Id
     * @return 任务信息
     */
    FormBean getTaskLogByTaskId(String taskId);

    /**
     * 手持设备登录(登录名+密码 或 标签号 两种方式登录)
     *
     * @param loginName
     *            登录名
     * @param password
     *            密码
     * @return 登录结果
     */
    ResultRespT<UserBean> login(HttpSession session, String loginName, String password);

    /**
     * 道口条码校验 / 更换道口
     *
     * @param dockCode
     *            月台号
     */
    ResultRespT<WmsDock> getWmsDockByDockCode(String dockCode, String loginName);
}
