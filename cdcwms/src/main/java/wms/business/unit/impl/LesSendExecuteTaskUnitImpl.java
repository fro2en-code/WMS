package wms.business.unit.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.BeanUtils;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsLesSendList;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsDeliverDock;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsWarehouse;
import com.ymt.utils.GoodsException;

import wms.business.service.WmsDeliverDockService;
import wms.business.service.WmsLesSendListService;
import wms.business.service.WmsLesSendService;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;

/**
 * 根据LES发货单生成发货 任务
 *
 * @author wangzz
 *
 */
@Service("lesSendExecuteTaskUnit")
public class LesSendExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Resource
    private WmsDeliverDockService deliverDockService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource(name = "wmsLesSendListService")
    private WmsLesSendListService lesSendListService;
    @Resource(name = "wmsLesSendService")
    private WmsLesSendService lesSendService;
    @Resource
    private StoragService storagService;
    @Value("7")
    private int taskType;

    @Resource
    private WarehouseService warehouseService;

    @Override
    public WmsTask createDefaultTaskByBillID(String billID) {
        WmsLesSend send = lesSendService.getEntity(billID);
        if (!BaseModel.INT_INIT.equals(send.getStatus())) {
            throw new RuntimeException("当前发货单已生成过任务");
        }
        List<WmsLesSendList> wmsLesSendLists = lesSendListService.getWmsLesSendList(send.getMapSheetNo(),
                send.getWhCode());
        if (BaseModel.INT_INIT.equals(wmsLesSendLists.size())) {
            throw new RuntimeException("该单据没有明细,无法生成任务!");
        }
        WmsTask task = new WmsTask();
        task.setBillid(billID);
        task.setBillNumber(send.getMapSheetNo());
        task.setWhCode(send.getWhCode());
        task.setLevel(0);
        task.setType(taskType);
        task.setStatus(0);
        task.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        return task;
    }

    @Override
    public ResultResp createTask(WmsTask task) {
        WmsLesSend sendBill = lesSendService.getEntity(task.getBillid());
        List<WmsLesSendList> sendListBills = lesSendListService.getWmsLesSendList(sendBill.getMapSheetNo(),
                task.getWhCode());
        WmsDeliverDock dock = deliverDockService.getType(sendBill.getDeliveryRec());
        if (null == dock) {
            throw new RuntimeException("收货地址不存在");
        }
        Map<WmsGoods, Integer> goodsCountMap = new HashMap<>();
        for (WmsLesSendList WmsLesSendList : sendListBills) {
            WmsGoods wmsGoods = goodsService.getGoodsInfo(WmsLesSendList.getPartNo(), dock.getGtype(),
                    WmsLesSendList.getSupplNo(), sendBill.getWhCode());
            if (null == wmsGoods) {
                throw new GoodsException("零件号:" + WmsLesSendList.getPartNo() + "关联物料不存在");
            }
            if (taskUnit.lesReciveTaskExist(WmsLesSendList.getSxCardNo())) {// 当前任务已生成,不作处理
                continue;
            }
            if (null == goodsCountMap.get(wmsGoods)) {
                goodsCountMap.put(wmsGoods, WmsLesSendList.getReqQty());
            } else {
                goodsCountMap.put(wmsGoods, goodsCountMap.get(wmsGoods) + WmsLesSendList.getReqQty());
            }
        }
        /*
         * 发货任务生成规则:不同 随箱卡号,库位 对应不同的任务
         */
        WmsWarehouse wh = warehouseService.getWmsByCode(task.getWhCode());
        List<WmsTask> taskLists = new ArrayList<>();
        for (WmsLesSendList sendList : sendListBills) {
            if (taskUnit.lesReciveTaskExist(sendList.getSxCardNo())) {// 当前任务已生成,不作处理
                continue;
            }
            WmsGoods wmsGoods = goodsService.getGoodsInfo(sendList.getPartNo(), dock.getGtype(), sendList.getSupplNo(),
                    sendBill.getWhCode());
            Map<String, Integer> storagMap = inventoryUnit.getOutWmsStorag(wmsGoods, sendList.getReqQty());
            for (Map.Entry<String, Integer> storag : storagMap.entrySet()) {
                String storagcode = storag.getKey();
                WmsTask wmsTask = createDefaultTask(task, task.getBillid(),
                        "LES拣货-" + storagcode + "-" + sendBill.getMapSheetNo());
                // 初始化任务
                WmsTaskBill taskBill = wmsTask.getWmsTaskBill();
                taskBill.setBillNumber(task.getBillNumber());
                taskBill.setNextStorageCode(wh.getSendStorage());
                taskBill.setStorageCode(storagcode);
                BeanUtils.copyProperty(wmsGoods, taskBill, "oraCode", "oraName", "whCode");
                taskBill.setSource(0);
                //
                WmsTaskBillList billList = new WmsTaskBillList();
                BeanUtils.copyProperty(wmsGoods, billList, "gcode", "gname", "oraCode", "oraName", "gtype");
                String abcType = goodsService.getAbcByGcode(sendList.getPartNo(), sendBill.getWhCode());
                billList.setAbcType(abcType);
                billList.setBoxCardid(sendList.getSxCardNo());
                billList.setBoxContent(sendList.getSendPackageNum());
                billList.setBoxNum(sendList.getReqPackageNum());
                billList.setGoodNeedNum(storag.getValue());
                wmsTask.getWmsTaskBill().getWmsTaskBillLists().add(billList);
                taskLists.add(wmsTask);
            }
        }
        for (WmsTask wmsTask : taskLists) {
            WmsTaskBill taskBill = wmsTask.getWmsTaskBill();
            List<WmsTaskBillList> billList = taskBill.getWmsTaskBillLists();
            lockWmsStock(taskBill.getWhCode(), taskBill.getStorageCode(), null, billList);
            taskUnit.addTask(wmsTask, taskBill, billList.toArray(new WmsTaskBillList[billList.size()]));
            // 发送消息
            sendMessage(wmsTask);
        }
        // 变更状态
        sendBill.setStatus(BaseModel.INT_CREATE);
        sendBill.setGtype(dock.getGtype());
        lesSendService.updateEntity(sendBill);
        return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "新增任务成功");
    }

    @Override
    public Set<String> getExector(WmsTask task) {
        Set<String> onlineSet = super.getExector(task);
        Set<String> managerSet = getStorageMange(task.getWmsTaskBill().getStorageCode());
        Set<String> result = new HashSet<>();
        if (onlineSet.size() > 0 && managerSet.size() > 0) {
            for (String string : onlineSet) {
                if (managerSet.contains(string)) {
                    result.add(string);
                }
            }
        }
        return result;
    }

    @Override
    public int getTaskType() {
        return taskType;
    }

    @Override
    public boolean isAllCompleteNext() {
        return true;
    }

    @Override
    public void setTaskCancel(WmsTask task) {
        WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
        List<WmsTaskBillList> list = wmsTaskBill.getWmsTaskBillLists();
        for (WmsTaskBillList wmsTaskBillList : list) {
            WmsGoods goods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), wmsTaskBill.getWhCode());
            int count = wmsTaskBillList.getGoodNeedNum();
            inventoryUnit.unLockWmsStock(wmsTaskBill.getStorageCode(), goods, count);
        }
    }

    @Override
    public void setTaskComplete(WmsTask task) {
        WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
        List<WmsTaskBillList> wmsTaskBillLists = wmsTaskBill.getWmsTaskBillLists();
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            List<WmsLesSendList> sendListBill = lesSendListService
                    .getWmsLesSendListBySXCarid(wmsTaskBillList.getBoxCardid(), task.getWhCode());
            WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), task.getWhCode());
            int count = wmsTaskBillList.getGoodNeedNum();
            for (WmsLesSendList wmsLesSendList : sendListBill) {
                wmsLesSendList
                        .setSendQty(null == wmsLesSendList.getSendQty() ? count : count + wmsLesSendList.getSendQty());
                wmsLesSendList.setReceiveQty(
                        null == wmsLesSendList.getReceiveQty() ? count : count + wmsLesSendList.getReqQty());
                lesSendListService.updateEntity(wmsLesSendList);
            }
            // 移库操作
            inventoryUnit.moveWmsStock(wmsTaskBill.getStorageCode(), wmsTaskBill.getNextStorageCode(), wmsGoods, count,
                    true);
        }
        // 修改les发货单开始时间
        WmsLesSend lesSend = lesSendService.getEntity(task.getBillid());
        lesSend.setEndTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        lesSendService.updateEntity(lesSend);
    }

    @Override
    public void setTaskRecive(WmsTask task) {
        WmsLesSend lesSend = lesSendService.getEntity(task.getBillid());
        // 如果没有开始时间则修改开始时间
        if (StringUtil.isEmpty(lesSend.getBeginTime())) {
            lesSend.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
            lesSendService.updateEntity(lesSend);
        }
    }
}
