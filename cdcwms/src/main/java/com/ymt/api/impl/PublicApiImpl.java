package com.ymt.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.business.WmsLesReceive;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsDock;
import com.wms.warehouse.WmsGoods;
import com.ymt.api.PublicApi;
import com.ymt.utils.BaseException;
import com.ymt.utils.DockUtils;
import com.ymt.utils.LockUtils;
import com.ymt.utils.UserUtils;

import cn.rtzltech.user.model.Plat_User;
import wms.business.biz.TaskBiz;
import wms.business.biz.WarehouseManagementBiz;
import wms.business.biz.WmsLesReceiveBiz;
import wms.business.biz.WmsLesSendBiz;

@Service("publicApi")
public class PublicApiImpl implements PublicApi {
    private static final Logger logger = LoggerFactory.getLogger(PublicApiImpl.class);
    @Resource
    private DockUtils dockUtils;
    @Value("${lockTime}")
    private int lockTime;
    @Value("${needEqualsTaskType}")
    private String needEquals;
    @Value("${needGtypeTaskType}")
    private String needGtype;
    @Resource
    private LockUtils lockUtils;
    @Resource
    private TaskBiz taskBiz;
    @Resource
    private UserUtils userUtils;
    @Resource
    private WarehouseManagementBiz warehouseManagementBiz;

    @Resource
    private WmsLesReceiveBiz wmsLesReceiveBiz;

    @Resource
    private WmsLesSendBiz wmsLesSendBiz;

    @Override
    public ResultResp addLesRecive(WmsLesReceive recive) {
        try {
            recive.setStatus(0);
            wmsLesReceiveBiz.saveLesReceiveBill(recive);
            return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "操作成功");
        } catch (Exception e) {
            return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, e.getMessage());
        }
    }

    @Override
    public ResultResp addLesSend(WmsLesSend send) {
        try {
            // 这里注意whcode 这个字段
            send.setStatus(0);
            com.plat.common.result.ResultResp result = wmsLesSendBiz.saveLesSendBill(send);
            // 另一个问题,如果生成任务失败,那么是否删除 WmsLesSend 对象?如果有事务,这里会回滚,现在没事务,跟马哥确认下吧
            send.setId(result.getBillId());
            WmsTask task = wmsLesSendBiz.createDefaultTaskByBillID(result.getBillId());
            try {
                taskBiz.addTask(task);
                return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
            } catch (BaseException e) {
                setSendError(send, e.getMessage());
                return new ResultResp(e.getExceptionCode(), e.getMessage());
            } catch (Exception e) {
                setSendError(send, e.getMessage());
                return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, e.getMessage());
            }
        } catch (Exception e) {
            return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, e.getMessage());
        }
    }

    @Override
    public ResultRespT<WmsDock> bindWmsDock(String tagId, String loginName) {
        try {
            WmsDock dock = dockUtils.getWmsDockByTag(tagId);
            if (null == dock) {
                throw new RuntimeException("条码绑定收货道口为空");
            }
            dockUtils.bindWmsDock(dock.getDockCode(), loginName);
            return new ResultRespT<>(ResultResp.SUCCESS_CODE, "绑定道口完成", dock);
        } catch (Exception e) {
            logger.error("", e);
            return new ResultRespT<>(ResultResp.ERROR_CODE, e.getMessage(), null);
        }
    }

    @Override
    public ResultResp completeTask(String storageCode, WmsTaskBill wmsTaskBill) {
        lockUtils.lock(wmsTaskBill.getTaskid(), "com.ymt.api.impl.PublicApiImpl.completeTask", lockTime);
        try {
            String taskId = wmsTaskBill.getTaskid();
            List<WmsTaskBillList> list = wmsTaskBill.getWmsTaskBillLists();
            WmsTask task = taskBiz.getTaskByTaskId(taskId);
            WmsTaskBill taskBill = task.getWmsTaskBill();
            if (StringUtils.isNotEmpty(storageCode) && !taskBill.getNextStorageCode().equals(storageCode)) {
                return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, "操作库位与目标库位不符");
            }
            List<WmsTaskBillList> billList = taskBill.getWmsTaskBillLists();
            if (billList.size() != list.size()) {
                return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, "任务未全部完成");
            }
            // 写入值数据
            Map<String, WmsTaskBillList> valuesMap = new HashMap<>();
            for (WmsTaskBillList wmsTaskBillList : list) {
                valuesMap.put(wmsTaskBillList.getId(), wmsTaskBillList);
            }
            for (WmsTaskBillList wmsTaskBillList : billList) {
                WmsTaskBillList valueList = valuesMap.get(wmsTaskBillList.getId());
                if (isNeedGtype(task)) {// 是否需要gType
                    wmsTaskBillList.setGtype(valueList.getGtype());
                }
                wmsTaskBillList.setGoodRealNum(valueList.getGoodRealNum());
            }
            //
            if (isNeedGtype(task)) {
                validataGoods(task);// 校验证收货
            }
            validataNeedEquals(task);
            // 调用任务接口完成任务
            taskBiz.setTaskComplete(task);
            return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "任务完成");
        } catch (Exception e) {
            logger.error("", e);
            return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, e.getMessage());
        } finally {
            lockUtils.releaseLock(wmsTaskBill.getTaskid());
        }
    }

    @Override
    public ResultRespT<List<WmsTask>> getTask(int page, int rows, String loginName, String whCode) {
        try {
            return taskBiz.getTask(page, rows, loginName, whCode);
        } catch (Exception e) {
            logger.error("", e);
            return new com.plat.common.result.ResultRespT<>(com.plat.common.result.ResultResp.ERROR_CODE,
                    e.getMessage());
        }
    }

    @Override
    public ResultRespT<WmsTask> getTaskByTaskId(String taskId) {
        try {
            WmsTask task = taskBiz.getTaskByTaskId(taskId);
            return new com.plat.common.result.ResultRespT<>(com.plat.common.result.ResultResp.SUCCESS_CODE, "查询完成",
                    task);
        } catch (Exception e) {
            logger.error("", e);
            return new com.plat.common.result.ResultRespT<>(com.plat.common.result.ResultResp.ERROR_CODE,
                    e.getMessage());
        }
    }

    @Override
    public ResultRespT<List<WmsTask>> getTaskLog(int page, int rows, String loginName) {
        try {
            return taskBiz.getTaskHistory(page, rows, loginName);
        } catch (Exception e) {
            logger.error("", e);
            return new com.plat.common.result.ResultRespT<>(com.plat.common.result.ResultResp.ERROR_CODE,
                    e.getMessage());
        }
    }

    /**
     * 是否需要Gype字段
     */
    private boolean isNeedGtype(WmsTask task) {
        String[] strArr = needGtype.split(",");
        for (String string : strArr) {
            if (Integer.valueOf(string).equals(task.getType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResultRespT<Plat_User> login(HttpSession session, String loginName, String password, String tagId) {
        com.plat.common.result.ResultRespT<Plat_User> result = null;
        try {
            if (!StringUtils.isEmpty(tagId)) {// tagID登录
                loginName = userUtils.getLoginNameByTid(tagId);
                if (StringUtils.isNotEmpty(loginName)) {
                    result = userUtils.login(session, loginName);
                } else {
                    return new com.plat.common.result.ResultRespT<>(com.plat.common.result.ResultResp.ERROR_CODE,
                            "TagID 未登记");
                }
            } else {// loginName 登录
                result = userUtils.login(session, loginName, password);
            }
            // 是否为收货员
            if (null != result && "0".equals(result.getRetcode())) {
                if (userUtils.isReciver(loginName)) {
                    result.setRetcode("1");
                }
            }
            return new ResultRespT<>(result.getRetcode(), result.getRetmsg(), result.getT());
        } catch (Exception e) {
            logger.error("", e);
            return new com.plat.common.result.ResultRespT<>(com.plat.common.result.ResultResp.ERROR_CODE,
                    e.getMessage());
        }
    }

    @Override
    public ResultResp reciveTask(String taskId, String user) {
        try {
            WmsTask task = taskBiz.getTaskByTaskId(taskId);
            taskBiz.setTaskRecive(task, user);
            return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "接收任务完成");
        } catch (Exception e) {
            logger.error("", e);
            return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, e.getMessage());
        }
    }

    private void setSendError(WmsLesSend send, String message) {
        send.setStatus(5);
        send.setRemark(message);
        wmsLesSendBiz.saveLesSend(send);
    }

    private void validataGoods(WmsTask task) {
        WmsTaskBill taskBill = task.getWmsTaskBill();
        String whCode = taskBill.getWhCode();
        List<WmsTaskBillList> list = taskBill.getWmsTaskBillLists();
        for (WmsTaskBillList wmsTaskBillList : list) {
            String oraCode = wmsTaskBillList.getOraCode();
            WmsGoods wmsGoods = warehouseManagementBiz.getGoodsInfo(wmsTaskBillList.getGcode(),
                    wmsTaskBillList.getGtype(), oraCode, whCode);
            if (null == wmsGoods) {
                throw new RuntimeException(wmsTaskBillList.getGcode() + " 物料基础资料有误");
            }
        }
    }

    /**
     * 验证需求件数,实际件数必需一致
     */
    private void validataNeedEquals(WmsTask task) {
        boolean flag = false;
        String[] strArr = needEquals.split(",");
        for (String string : strArr) {
            if (Integer.valueOf(string) == task.getType()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return;
        }
        //
        WmsTaskBill taskBill = task.getWmsTaskBill();
        List<WmsTaskBillList> list = taskBill.getWmsTaskBillLists();
        for (WmsTaskBillList wmsTaskBillList : list) {
            if (null == wmsTaskBillList.getGoodRealNum() || null == wmsTaskBillList.getGoodNeedNum()
                    || !wmsTaskBillList.getGoodRealNum().equals(wmsTaskBillList.getGoodNeedNum())) {
                throw new RuntimeException(wmsTaskBillList.getGcode() + " 实际操作数量和预计操作数量不符");
            }
        }
    }

}
