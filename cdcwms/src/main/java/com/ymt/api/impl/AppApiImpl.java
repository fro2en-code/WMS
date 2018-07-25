package com.ymt.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.beans.BillBean;
import com.plat.common.beans.BillBean.AttachData;
import com.plat.common.beans.FormBean;
import com.plat.common.beans.FormElement;
import com.plat.common.beans.FormElement.OptVal;
import com.plat.common.beans.FormElement.RestrainOpt;
import com.plat.common.beans.TaskBean;
import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsDock;
import com.ymt.api.AppApi;
import com.ymt.api.PublicApi;
import com.ymt.utils.UserUtils;

import cn.rtzltech.user.model.Plat_User;

@Service("appApi")
public class AppApiImpl implements AppApi {
    private Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private PublicApi publicApi;
    @Value("${reciverRole}")
    private String reciverRole;

    @Resource
    private UserUtils userUtils;

    @Override
    public ResultResp completeTask(BillBean billBean) {
        String msg = "操作失败，系统错误";

        String nextStorageCode = billBean.getAttachData().getNextStorageCode();
        Map<String, Object> dataHead = billBean.getDataHead();

        WmsTaskBill wmsTaskBill = new WmsTaskBill();
        wmsTaskBill.setTaskid(billBean.getAttachData().getTaskId());
        wmsTaskBill.setBillid(billBean.getAttachData().getBillId());
        List<WmsTaskBillList> wmsTaskBillLists = new ArrayList<>();
        // 取出billList
        if (billBean.getDataDetail() != null) {
            for (Map<String, Object> billmap : billBean.getDataDetail()) {
                WmsTaskBillList wtbl = new WmsTaskBillList();
                wtbl.setGcode(billmap.get("gcode").toString());
                wtbl.setGcode(billmap.get("gname").toString());
                if (billmap.get("actualNum") != null) {
                    wtbl.setGoodRealNum(Integer.parseInt(billmap.get("actualNum").toString()));
                }
                if (billmap.get("needNum") != null) {
                    wtbl.setGoodNeedNum(Integer.parseInt(billmap.get("needNum").toString()));
                }
                if (billmap.get("guse") != null) {
                    wtbl.setGtype(billmap.get("guse").toString());
                }
                wtbl.setId(billmap.get("id").toString());
                wmsTaskBillLists.add(wtbl);
            }

            // bill父单据是共用的
            if (billBean.getAttachData().getNextStorageCode() != null) {
                wmsTaskBill.setNextStorageCode(billBean.getAttachData().getNextStorageCode());
            }
            if (billBean.getAttachData().getStorageCode() != null) {
                wmsTaskBill.setNextStorageCode(billBean.getAttachData().getStorageCode());
            }
            //
            wmsTaskBill.setWmsTaskBillLists(wmsTaskBillLists);
            ResultResp resultResp = publicApi.completeTask(nextStorageCode, wmsTaskBill);
            if (ResultResp.SUCCESS_CODE.equals(resultResp.getRetcode())) {
                return new ResultResp(ResultResp.SUCCESS_CODE, "操作成功");
            }
            msg = resultResp.getRetmsg();
        } else {
            msg = "任务明细不存在";
        }
        return new ResultResp(ResultResp.ERROR_CODE, msg);
    }

    public List<TaskBean> convertTaskBean(List<WmsTask> tasklist) {
        List<TaskBean> taskBeans = new ArrayList<>();
        for (WmsTask wt : tasklist) {
            TaskBean tb = new TaskBean();
            tb.setTaskName(wt.getTaskdesc());
            tb.setCreateTime(wt.getBeginTime());
            tb.setTaskId(wt.getId());
            taskBeans.add(tb);
        }
        return taskBeans;

    }

    @Override
    public List<TaskBean> getTask(int page, int rows, String loginname, String whCode) {
        try {
            ResultRespT<List<WmsTask>> respT = publicApi.getTask(page, rows, loginname, whCode);
            if (ResultResp.SUCCESS_CODE.equals(respT.getRetcode())) {
                return this.convertTaskBean(respT.getT());
            }
        } catch (Exception e) {
            logger.error(null, e);
        }
        return Collections.emptyList();
    }

    @Override
    public FormBean getTaskByTaskId(String taskId) {
        FormBean formBean = new FormBean();
        ResultRespT<WmsTask> respT = publicApi.getTaskByTaskId(taskId);
        if (!ResultResp.SUCCESS_CODE.equals(respT.getRetcode())) {
            formBean.setRetmsg(respT.getRetmsg());
            return formBean;
        }
        WmsTask task = respT.getT();
        if (task == null) {
            formBean.setRetmsg("任务已不存在");
            return formBean;
        }
        WmsTaskBill bill = task.getWmsTaskBill();
        if (bill == null) {
            formBean.setRetmsg("任务关联的单据已不存在");
            return formBean;
        }
        int taskType = bill.getType();

        List<WmsTaskBillList> billDetails = bill.getWmsTaskBillLists();
        for (int i = 0, j = billDetails.size(); i < j; i++) {
            WmsTaskBillList detail = billDetails.get(i);
            String gcode = detail.getGcode();
            String gname = detail.getGname();
            int needNum = detail.getGoodNeedNum();
            List<FormElement> elemList = new ArrayList<>();
            FormElement elem = new FormElement();
            elem.setElemType(1);
            elem.setDefaultVal(detail.getId());
            elem.setTitle("ID");
            elem.setVarName("id");
            elem.setDisplay(false);
            elemList.add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle((i + 1) + " 编号：");
            elem.setDefaultVal(gcode);
            elem.setVarName("gcode");
            elemList.add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("名称：");
            elem.setDefaultVal(gname);
            elem.setVarName("gname");
            elemList.add(elem);
            switch (taskType) {
            case 1:
                // 我日他妈,这段switch代码真是奇葩啊,这里因为没有break用的是2的值
            case 2:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("供应商：");
                elem.setDefaultVal(detail.getOraCode());
                elem.setVarName("oraCode");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("ABC类型：");
                if (StringUtil.isEmpty(detail.getAbcType())) {
                    elem.setDefaultVal(" ");
                } else {
                    elem.setDefaultVal(detail.getAbcType());
                }
                elem.setVarName("abcType");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("应收数量：");
                elem.setDefaultVal(needNum + "");
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(2);
                elem.setTitle("实收数量：");
                elem.setVarName("actualNum");
                elem.setDefaultVal(needNum + "");
                elem.getRestrainOpts().put("notNullRestr", new RestrainOpt("1", "实收数量不能为空"));
                elem.getRestrainOpts().put("formatRestr", new RestrainOpt("2", "请填写数字"));
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(3);
                elem.setTitle("零件用途：");
                elem.setVarName("guse");
                elem.getRestrainOpts().put("notNullRestr", new RestrainOpt("1", "请选择零件用途"));
                List<OptVal> optVals = new ArrayList<>();
                optVals.add(new OptVal("生产件", "3"));
                optVals.add(new OptVal("备件", "1"));
                optVals.add(new OptVal("出口件", "2"));
                elem.setOptVals(optVals);
                elemList.add(elem);
                break;
            case 3:
            case 4:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("供应商：");
                elem.setDefaultVal(detail.getOraCode() + " - " + detail.getOraName());
                elem.setVarName("oraCode");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("操作数量：");
                elem.setDefaultVal(needNum + "");
                elem.setVarName("actualNum");
                elemList.add(elem);
                break;
            case 5:
            case 6:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("供应商：");
                elem.setDefaultVal(detail.getOraCode() + " - " + detail.getOraName());
                elem.setVarName("oraCode");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("物料数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("实际数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("actualNum");
                elem.setDisplay(false);
                elemList.add(elem);
                break;
            case 7:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("供应商：");
                elem.setDefaultVal(detail.getOraCode() + " - " + detail.getOraName());
                elem.setVarName("oraCode");
                elemList.add(elem);
                //
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("随箱卡号：");
                elem.setDefaultVal(detail.getBoxCardid());
                elem.setVarName("boxCarid");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("物料数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("实际数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("actualNum");
                elem.setDisplay(false);
                elemList.add(elem);
                break;
            case 8:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("物料数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("实际数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("actualNum");
                elem.setDisplay(false);
                elemList.add(elem);
                break;
            case 9:
                // elem = new FormElement();
                // elem.setElemType(1);
                // elem.setTitle("应在库数量：");
                // elem.setDefaultVal(String.valueOf(needNum));
                // elem.setVarName("needNum");
                // elemList.add(elem);
                elem = new FormElement();
                elem.setElemType(2);
                elem.setTitle("实盘数量：");
                elem.setVarName("actualNum");
                elem.getRestrainOpts().put("notNullRestr", new RestrainOpt("1", "实收数量不能为空"));
                elem.getRestrainOpts().put("formatRestr", new RestrainOpt("2", "请填写数字"));
                elemList.add(elem);
                break;
            case 10:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("装车数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("实际数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("actualNum");
                elem.setDisplay(false);
                elemList.add(elem);
                break;
            }
            formBean.getFormElems().add(elemList);
        }
        FormElement elem = null;
        switch (taskType) {
        case 1:
        case 2:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("收货道口：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;

        case 3:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("收货道口：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("目标库位：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        case 4:
        case 5:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("起始库位：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("目标库位：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        case 6:
        case 7:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("目标库位：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("发货区：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        case 8:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("起始库位：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("目标库位：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        case 9:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("盘点库位：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);
            break;
        case 10:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("装货区：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        }

        // 表单类型
        formBean.setFormType(bill.getType());
        // 表单携带数据
        AttachData attachData = new AttachData();
        attachData.setTaskId(task.getId());
        attachData.setBillId(bill.getId());
        attachData.setStorageCode(bill.getStorageCode());
        attachData.setNextStorageCode(bill.getNextStorageCode());
        formBean.setAttachData(attachData);
        formBean.setRetcode("0");
        formBean.setRetmsg("success");
        return formBean;
    }

    @Override
    public List<TaskBean> getTaskLog(int page, int rows, String loginName, String whCode) {
        ResultRespT<List<WmsTask>> respT = publicApi.getTaskLog(page, rows, loginName);
        if (ResultResp.SUCCESS_CODE.equals(respT.getRetcode())) {
            return this.convertTaskBean(respT.getT());
        }
        return Collections.emptyList();
    }

    @Override
    public FormBean getTaskLogByTaskId(String taskId) {
        FormBean formBean = new FormBean();
        ResultRespT<WmsTask> respT = publicApi.getTaskByTaskId(taskId);
        if (!ResultResp.SUCCESS_CODE.equals(respT.getRetcode())) {
            formBean.setRetmsg(respT.getRetmsg());
            return formBean;
        }
        WmsTask task = respT.getT();
        if (task == null) {
            formBean.setRetmsg("任务已不存在");
            return formBean;
        }
        WmsTaskBill bill = task.getWmsTaskBill();
        if (bill == null) {
            formBean.setRetmsg("任务关联的单据已不存在");
            return formBean;
        }
        int taskType = bill.getType();

        List<WmsTaskBillList> billDetails = bill.getWmsTaskBillLists();
        for (WmsTaskBillList detail : billDetails) {// 收货
            String gcode = detail.getGcode();
            String gname = detail.getGname();
            int needNum = detail.getGoodNeedNum();
            List<FormElement> elemList = new ArrayList<>();
            FormElement elem = new FormElement();
            elem.setElemType(1);
            elem.setDefaultVal(detail.getId());
            elem.setTitle("ID");
            elem.setVarName("id");
            elem.setDisplay(false);
            elemList.add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("编号：");
            elem.setDefaultVal(gcode);
            elem.setVarName("gcode");
            elemList.add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("名称：");
            elem.setDefaultVal(gname);
            elem.setVarName("gname");
            elemList.add(elem);
            switch (taskType) {
            case 1:
            case 2:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("应收数量：");
                elem.setDefaultVal(needNum + "");
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("实收数量：");
                elem.setDefaultVal(String.valueOf(detail.getGoodRealNum()));
                elem.setVarName("actualNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("零件用途：");
                elem.setDefaultVal(detail.getGtype());
                elem.setVarName("guse");
                elemList.add(elem);
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("物料数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("实际数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("actualNum");
                elem.setDisplay(false);
                elemList.add(elem);
                break;
            case 9:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("应在库数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("实盘数量：");
                elem.setDefaultVal(String.valueOf(detail.getGoodRealNum()));
                elem.setVarName("actualNum");
                elemList.add(elem);
                break;
            case 10:
                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("装车数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("needNum");
                elemList.add(elem);

                elem = new FormElement();
                elem.setElemType(1);
                elem.setTitle("实际数量：");
                elem.setDefaultVal(String.valueOf(needNum));
                elem.setVarName("actualNum");
                elem.setDisplay(false);
                elemList.add(elem);
                break;
            }
            formBean.getFormElems().add(elemList);
        }
        FormElement elem = null;
        switch (taskType) {
        case 1:
        case 2:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("收货道口：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;

        case 3:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("收货道口：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("目标库位：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        case 4:
        case 5:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("起始库位：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("目标库位：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        case 6:
        case 7:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("目标库位：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("发货区：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        case 8:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("起始库位：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("目标库位：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);
            break;
        case 9:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("盘点库位：");
            elem.setDefaultVal(bill.getStorageCode());
            elem.setVarName("storageCode");
            formBean.getFormElem().add(elem);
            break;
        case 10:
            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("装货区：");
            elem.setDefaultVal(bill.getNextStorageCode());
            elem.setVarName("nextStorageCode");
            formBean.getFormElem().add(elem);

            elem = new FormElement();
            elem.setElemType(1);
            elem.setTitle("车牌号：");
            elem.setDefaultVal(bill.getRemark());
            elem.setVarName("remark");
            formBean.getFormElem().add(elem);
            break;
        }

        // 表单类型
        formBean.setFormType(bill.getType());
        // 表单携带数据
        AttachData attachData = new AttachData();
        attachData.setTaskId(task.getId());
        attachData.setBillId(bill.getId());
        attachData.setStorageCode(bill.getStorageCode());
        attachData.setNextStorageCode(bill.getNextStorageCode());
        formBean.setAttachData(attachData);
        formBean.setRetcode("0");
        formBean.setRetmsg("success");
        return formBean;
    }

    @Override
    public ResultRespT<WmsDock> getWmsDockByDockCode(String dockCode, String loginName) {
        ResultRespT<WmsDock> ret = new ResultRespT<>();
        if (StringUtil.isEmpty(dockCode)) {
            ret.setRetmsg("道口标签不能为空值");
            return ret;
        }
        return publicApi.bindWmsDock(dockCode, loginName);
    }

    @Override
    public ResultRespT<UserBean> login(HttpSession session, String loginName, String password) {
        ResultRespT<UserBean> ret = new ResultRespT<>();
        com.plat.common.result.ResultRespT<Plat_User> resp = null;
        logger.debug("登录请求--------------------------------");
        if (!StringUtils.isNotEmpty(password)) {
            resp = userUtils.login(session, loginName);
        } else {
            resp = userUtils.login(session, loginName, password);
        }
        logger.debug("登录请求完成--------------------------------");
        if (!ResultResp.SUCCESS_CODE.equals(resp.getRetcode())) {
            ret.setRetmsg(resp.getRetmsg());
            return ret;
        }

        UserBean ub = new UserBean();
        ub.setLoginname(loginName);
        ub.setTruename(resp.getT().getTruename());
        List<String> rolerList = userUtils.getRolesByUser(loginName);
        if (rolerList.contains(reciverRole)) {
            ub.setUserFlag("1");
        }
        ret.setRetcode("0");
        ret.setT(ub);
        return ret;
    }

    @Override
    public ResultResp receiveTask(String taskId, String loginName) {
        String msg = "操作失败，系统错误";

        try {
            ResultResp resultResp = publicApi.reciveTask(taskId, loginName);
            if (ResultResp.SUCCESS_CODE.equals(resultResp.getRetcode())) {
                return new ResultResp(ResultResp.SUCCESS_CODE, "操作成功");
            }
            msg = resultResp.getRetmsg();
        } catch (Exception e) {
            logger.error(null, e);
        }
        return new ResultResp(ResultResp.ERROR_CODE, msg);
    }

}
