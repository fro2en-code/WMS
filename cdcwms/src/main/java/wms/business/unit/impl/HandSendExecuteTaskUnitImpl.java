package wms.business.unit.impl;

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
import com.wms.business.WmsHandworkSend;
import com.wms.business.WmsHandworkSendList;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsDeliverDock;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsWarehouse;

import wms.business.service.WmsDeliverDockService;
import wms.business.service.WmsHandworkSendListService;
import wms.business.service.WmsHandworkSendService;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;

/**
 * 根据手工发货单生成发货 任务
 *
 * @author wangzz
 *
 */
@Service("handSendExecuteTaskUnit")
public class HandSendExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Resource
    private WmsDeliverDockService deliverDockService;
    @Resource
    private GoodsService goodsService;
    @Resource(name = "wmsHandworkSendListService")
    private WmsHandworkSendListService handSendListService;
    @Resource(name = "wmsHandworkSendService")
    private WmsHandworkSendService handSendService;
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource
    private StoragService storagService;
    @Value("6")
    private int taskType;

    @Resource
    private WarehouseService warehouseService;

    @Override
    public WmsTask createDefaultTaskByBillID(String billID) {
        WmsHandworkSend send = handSendService.getEntity(billID);
        if (!BaseModel.INT_INIT.equals(send.getStatus())) {
            throw new RuntimeException("当前发货单已生成过任务");
        }
        List<WmsHandworkSendList> wmsHandworkSendLists = handSendListService
                .getWmsHandworkSendList(send.getMapSheetNo(), send.getWhCode());
        if (BaseModel.INT_INIT.equals(wmsHandworkSendLists.size())) {
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
        WmsHandworkSend sendBill = handSendService.getEntity(task.getBillid());
        List<WmsHandworkSendList> sendListBills = handSendListService.getWmsHandworkSendList(sendBill.getMapSheetNo(),
                task.getWhCode());
        WmsDeliverDock dock = deliverDockService.getType(sendBill.getDeliveryRec());
        if (null == dock) {
            throw new RuntimeException("收货地址不存在");
        }
        WmsWarehouse wh = warehouseService.getWmsByCode(task.getWhCode());
        Map<String, WmsTask> taskMap = new HashMap<>();
        for (WmsHandworkSendList sendList : sendListBills) {
            WmsGoods wmsGoods = goodsService.getGoodsInfo(sendList.getPartNo(), dock.getGtype(), sendList.getSupplNo(),
                    sendBill.getWhCode());
            int count = sendList.getReqQty();
            Map<String, Integer> storagMap = inventoryUnit.getOutWmsStorag(wmsGoods, count);
            for (Map.Entry<String, Integer> storag : storagMap.entrySet()) {
                String storagcode = storag.getKey();
                WmsTask wmsTask = taskMap.get(storagcode);
                if (null == wmsTask) {
                    wmsTask = createDefaultTask(task, task.getBillid(),
                            "手工拣货-" + storagcode + "-" + sendBill.getMapSheetNo());
                    // 初始化任务
                    WmsTaskBill taskBill = wmsTask.getWmsTaskBill();
                    taskBill.setBillNumber(task.getBillNumber());
                    taskBill.setNextStorageCode(wh.getSendStorage());
                    taskBill.setStorageCode(storagcode);
                    BeanUtils.copyProperty(wmsGoods, taskBill, "oraCode", "oraName", "whCode");
                    taskBill.setSource(1);
                    taskMap.put(storagcode, wmsTask);
                }
                WmsTaskBillList billList = new WmsTaskBillList();
                BeanUtils.copyProperty(wmsGoods, billList, "gcode", "gname", "oraCode", "oraName", "gtype");
                String abcType = goodsService.getAbcByGcode(sendList.getPartNo(), sendBill.getWhCode());
                billList.setAbcType(abcType);
                billList.setBoxCardid(sendList.getId());
                billList.setBoxContent(sendList.getSendPackageNum());
                billList.setBoxNum(sendList.getReqPackageNum());
                billList.setGoodNeedNum(storag.getValue());
                wmsTask.getWmsTaskBill().getWmsTaskBillLists().add(billList);
            }
        }
        for (WmsTask wmsTask : taskMap.values()) {
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
        handSendService.updateEntity(sendBill);
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
        WmsHandworkSend sendBill = handSendService.getEntity(task.getBillid());
        // 设置实发件数=需求件数,因为实际操作中严格按需求件数发货,所以可以这么做
        List<WmsHandworkSendList> list = handSendListService.getWmsHandworkSendList(sendBill.getMapSheetNo(),
                sendBill.getWhCode());
        for (WmsHandworkSendList wmsHandworkSendList : list) {
            wmsHandworkSendList.setSendQty(wmsHandworkSendList.getReqQty());
            wmsHandworkSendList.setReceiveQty(wmsHandworkSendList.getReqQty());
            handSendListService.updateEntity(wmsHandworkSendList);
        }

        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), sendBill.getWhCode());
            int count = wmsTaskBillList.getGoodNeedNum();
            // 移库操作
            inventoryUnit.moveWmsStock(wmsTaskBill.getStorageCode(), wmsTaskBill.getNextStorageCode(), wmsGoods, count,
                    true);
        }
        WmsHandworkSend handworkSend = handSendService.getEntity(task.getBillid());
        handworkSend.setEndTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        handSendService.updateEntity(handworkSend);
    }

    @Override
    public void setTaskRecive(WmsTask task) {
        WmsHandworkSend handworkSend = handSendService.getEntity(task.getBillid());
        if (StringUtil.isEmpty(handworkSend.getBeginTime())) {
            handworkSend.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
            handSendService.updateEntity(handworkSend);
        }
    }
}
