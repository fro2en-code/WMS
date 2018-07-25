package wms.business.unit.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.result.ResultResp;
import com.plat.common.utils.BeanUtils;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsGoods;

import wms.business.service.WmsStockService;
import wms.business.service.WmsTaskBillListService;
import wms.business.service.WmsTaskBillService;
import wms.business.service.WmsTaskService;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;

/**
 * 根据收货单生成上架任务
 *
 * @author Administrator
 *
 */
@Service("putawayExecuteTaskUnit")
public class PutawayExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Resource
    private GoodsService goodsService;
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource
    private StoragService storagService;
    @Value("4")
    private int taskType;
    @Resource
    private WmsStockService wmsStockService;
    @Resource
    private WmsTaskBillListService wmsTaskBillListService;
    @Resource
    private WmsTaskBillService wmsTaskBillService;
    @Resource
    private WmsTaskService wmsTaskService;

    @Override
    public ResultResp createTask(WmsTask task) {
        WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
        if ("3".equals(task.getRemark())) {// 搬运任务过来的,直接转换成上架任务
            WmsTask wmsTask = createDefaultTask(task, task.getId(), "上架-" + wmsTaskBill.getNextStorageCode());
            WmsTaskBill taskBill = wmsTask.getWmsTaskBill();
            taskBill.setStorageCode(wmsTaskBill.getStorageCode());
            taskBill.setNextStorageCode(wmsTaskBill.getNextStorageCode());
            List<WmsTaskBillList> list = wmsTaskBill.getWmsTaskBillLists();
            taskUnit.addTask(wmsTask, wmsTask.getWmsTaskBill(), list.toArray(new WmsTaskBillList[list.size()]));
            sendMessage(wmsTask);
        } else {// 收货任务过来的
            List<WmsTaskBillList> wmsTaskBillList = wmsTaskBill.getWmsTaskBillLists();
            Map<String, WmsTask> storage = new HashMap<>();
            for (WmsTaskBillList taskBillList : wmsTaskBillList) {
                String gcode = taskBillList.getGcode();
                String gtype = taskBillList.getGtype();
                String oraCode = taskBillList.getOraCode();
                WmsGoods wmsGoods = goodsService.getGoodsInfo(gcode, gtype, oraCode, wmsTaskBill.getWhCode());
                Map<String, Integer> storagMap = inventoryUnit.getInWmsStorag(wmsGoods, taskBillList.getGoodRealNum());
                for (Map.Entry<String, Integer> storag : storagMap.entrySet()) {
                    String storagcode = storag.getKey();
                    WmsTask putawayTask = storage.get(storagcode);
                    if (null == putawayTask) {
                        putawayTask = createDefaultTask(task, task.getId(), "上架-" + storagcode);
                        storage.put(storagcode, putawayTask);
                        WmsTaskBill taskBill = putawayTask.getWmsTaskBill();
                        taskBill.setStorageCode(wmsTaskBill.getNextStorageCode());
                        taskBill.setNextStorageCode(storagcode);
                    }
                    WmsTaskBillList billList = new WmsTaskBillList();
                    billList.setGoodNeedNum(storag.getValue());
                    BeanUtils.copyProperty(taskBillList, billList, "gcode", "gname", "gtype", "oraCode", "oraName");
                    putawayTask.getWmsTaskBill().getWmsTaskBillLists().add(billList);
                }
            }
            // 添加上架任务任务
            for (Map.Entry<String, WmsTask> storages : storage.entrySet()) {
                WmsTask wmsTask = storages.getValue();
                WmsTaskBill taskBill = wmsTask.getWmsTaskBill();
                if (!taskBill.getStorageCode().equals(taskBill.getNextStorageCode())) {// 搬运库位和上架库位一致则跳过
                    List<WmsTaskBillList> wmsTaskBillLists = taskBill.getWmsTaskBillLists();
                    lockWmsStock(taskBill.getWhCode(), taskBill.getStorageCode(), taskBill.getNextStorageCode(),
                            wmsTaskBillLists);
                    taskUnit.addTask(wmsTask, taskBill, taskBill.getWmsTaskBillLists()
                            .toArray(new WmsTaskBillList[taskBill.getWmsTaskBillLists().size()]));
                    sendMessage(wmsTask);
                }
            }
        }
        return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "新增上架任务成功");
    }

    @Override
    public Set<String> getExector(WmsTask task) {
        Set<String> onlineSet = super.getExector(task);
        Set<String> managerSet = getStorageMange(task.getWmsTaskBill().getNextStorageCode());
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

    /**
     * 单据类型
     *
     * @return
     */
    @Override
    public int getTaskType() {
        return taskType;
    }

    @Override
    public void setTaskComplete(WmsTask task) {
        WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
        List<WmsTaskBillList> wmsTaskBillLists = wmsTaskBill.getWmsTaskBillLists();
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), wmsTaskBill.getWhCode());
            int count = wmsTaskBillList.getGoodRealNum();
            inventoryUnit.moveWmsStock(wmsTaskBill.getStorageCode(), wmsTaskBill.getNextStorageCode(), wmsGoods, count,
                    false);
        }
    }

}
