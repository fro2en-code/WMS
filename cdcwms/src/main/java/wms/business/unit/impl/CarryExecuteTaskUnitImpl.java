package wms.business.unit.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

import wms.business.unit.InventoryUnit;
import wms.warehouse.service.StoragService;

/**
 * 搬货任务
 *
 * @author wangzz
 *
 */
@Service("carryExecuteTaskUnit")
public class CarryExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource
    private StoragService storagService;

    @Value("3")
    private int taskType;

    private void addTask(WmsTask task, Map<String, List<WmsTaskBillList>> taskMap) {
        for (Map.Entry<String, List<WmsTaskBillList>> storage : taskMap.entrySet()) {
            String wmsStorag = storage.getKey();
            WmsTask wmsTask = createDefaultTask(task, task.getId(), "搬运-" + wmsStorag);
            WmsTaskBill wmsTaskBill = wmsTask.getWmsTaskBill();
            wmsTaskBill.setNextStorageCode(wmsStorag);
            wmsTaskBill.setStorageCode(task.getWmsTaskBill().getNextStorageCode());
            if (!wmsTaskBill.getStorageCode().equals(wmsTaskBill.getNextStorageCode())) {// 搬运库位和上架库位一致则跳过
                List<WmsTaskBillList> wmsTaskBillLists = storage.getValue();
                lockWmsStock(wmsTaskBill.getWhCode(), wmsTaskBill.getStorageCode(), wmsTaskBill.getNextStorageCode(),
                        wmsTaskBillLists);
                taskUnit.addTask(wmsTask, wmsTaskBill,
                        wmsTaskBillLists.toArray(new WmsTaskBillList[wmsTaskBillLists.size()]));
                sendMessage(wmsTask);
            }
        }
    }

    private void addTaskBillList(Map<String, List<WmsTaskBillList>> taskMap, WmsTaskBillList wmsTaskBillList,
            WmsGoods goods) {
        // 统计入库数据
        Map<String, Integer> storageMap = inventoryUnit.getInWmsStorag(goods, wmsTaskBillList.getGoodRealNum());
        for (Map.Entry<String, Integer> storage : storageMap.entrySet()) {
            String wmsStorag = storage.getKey();
            List<WmsTaskBillList> lists = taskMap.get(wmsStorag);
            if (null == lists) {
                lists = new ArrayList<>();
                taskMap.put(wmsStorag, lists);
            }
            WmsTaskBillList taskBillList = new WmsTaskBillList();
            taskBillList.setGoodNeedNum(storage.getValue());
            BeanUtils.copyProperty(wmsTaskBillList, taskBillList, "gcode", "gname", "gtype", "oraCode", "oraName");
            lists.add(taskBillList);
        }
    }

    @Override
    public ResultResp createTask(WmsTask task) {
        boolean flag = false;
        // 一个收货任务对应多个搬运任务,任务个数根据库位来
        Map<String, List<WmsTaskBillList>> taskMap = new HashMap<>();
        WmsTaskBill taskBill = task.getWmsTaskBill();
        List<WmsTaskBillList> taskBillLists = taskBill.getWmsTaskBillLists();
        Iterator<WmsTaskBillList> iterator = taskBillLists.iterator();
        while (iterator.hasNext()) {
            WmsTaskBillList wmsTaskBillList = iterator.next();
            WmsGoods goods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), taskBill.getWhCode());
            // 是否需要搬运
            if (goods.getForkliftNum() > wmsTaskBillList.getGoodRealNum()) {
                continue;
            }
            flag = true;
            iterator.remove();// 移除已搬运的任务
            addTaskBillList(taskMap, wmsTaskBillList, goods);
        }
        if (flag) {
            addTask(task, taskMap);
            if (taskBillLists.size() != 0) {
                return new ResultResp(ResultResp.SKIP_CODE, "需要创建下一任务");
            } else {
                return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "新增任务成功");
            }
        } else {// 如果没有搬运任务,则直跳过
            return new ResultResp(com.plat.common.result.ResultResp.SKIP_CODE, "无需要创建任务");
        }
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

    @Override
    public int getTaskType() {
        return taskType;
    }

}
