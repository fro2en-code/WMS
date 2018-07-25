package com.ymt.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.business.WmsLesReceive;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.warehouse.WmsDock;

import cn.rtzltech.user.model.Plat_User;

/**
 * <p>
 * 公共接口实现
 * </p>
 *
 * @version 1.1
 *
 * @author zhouxianglh@gmail.com
 *
 * @snce 2017年4月20日
 */
public interface PublicApi {
    /**
     * 接收LES单据
     */
    ResultResp addLesRecive(WmsLesReceive recive);

    /**
     * 接收LES发货单据
     *
     * @return retcode = 2 库存不够,3 物料不存在,4 其它错误
     */
    ResultResp addLesSend(WmsLesSend send);

    /**
     * 绑定收货道口
     *
     * @param tagId
     *            标签
     */
    ResultRespT<WmsDock> bindWmsDock(String tagId, String loginName);

    /**
     * 任务完成
     *
     *            任务ID
     * @param storageCode
     *            库位条码(收货任务,发货任务不需要校验库位,可以为空)
     * @param wmsTaskBill
     *            taskId, 任务执行明细(id,goodRealNum 必填,收货任务中 gtype 也是必填,发货任务中 remark 里为车牌号)
     */
    ResultResp completeTask(String storageCode, WmsTaskBill wmsTaskBill);

    /**
     * 查询待执行任务(可以接受但未接受+已接受但未完成的任务)
     *
     * @param loginName
     *            登录名
     * @return 任务列表
     */
    ResultRespT<List<WmsTask>> getTask(int page, int rows, String loginName, String whCode);

    /**
     * 根据任务ID查询任务信息
     *
     * @param taskId
     *            任务Id
     * @return 任务信息
     */
    ResultRespT<WmsTask> getTaskByTaskId(String taskId);

    /**
     * 根据当前登录人查询任务执行历史
     *
     * @param loginName
     *            登录名
     * @return 任务信息
     */
    ResultRespT<List<WmsTask>> getTaskLog(int page, int rows, String loginName);

    /**
     * 手持设备登录(登录名+密码 或 标签号 两种方式登录)
     *
     * @param session
     *            当前Sesssin,如果不需要系统配合登录控制(用户只能在一台设备登录),可以为空
     * @param loginName
     *            登录名
     * @param password
     *            密码
     * @param tagId
     *            标签号
     * @return 登录结果(retcode=0,登录成功,retcode=1 登录成功,且当前用户需要扫描收货道口,retcode=-1 登录失败)
     */
    ResultRespT<Plat_User> login(HttpSession session, String loginName, String password, String tagId);

    /**
     * 接收任务
     *
     * @param taskId
     *            任务ID
     * @param user
     *            任务接收人
     */
    ResultResp reciveTask(String taskId, String user);
}
