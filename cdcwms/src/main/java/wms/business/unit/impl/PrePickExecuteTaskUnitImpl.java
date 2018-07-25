package wms.business.unit.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.TaskPrePick;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsGoods;

import wms.business.service.TaskPicPickService;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;

/**
 * 预拣货任务
 *
 * @author wangzz
 *
 */
@Service("prePickExecuteTaskUnit")
public class PrePickExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Resource
    private GoodsService goodsService;
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource(name = "taskPicPickService")
    private TaskPicPickService picPickService;
    @Resource
    private StoragService storagService;
    @Value("5")
    private int taskType;

    @Override
    public WmsTask createDefaultTaskByBillID(String billID) {
        TaskPrePick taskPerPick = picPickService.getEntity(billID);
        WmsTask task = new WmsTask();
        task.setBillid(billID);
        task.setLevel(0);
        task.setType(getTaskType());
        task.setStatus(0);
        task.setWhCode(taskPerPick.getWhCode());
        task.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        return task;
    }

    @Override
    public ResultResp createTask(WmsTask task) {
        // 获取预拣货单
        TaskPrePick prePick = picPickService.getEntity(task.getBillid());
        task.setType(getTaskType());
        WmsGoods wmsGoods = goodsService.getGoodsInfo(prePick.getGcode(), prePick.getGtype(), prePick.getOraCode(),
                prePick.getWhCode());
        int count = prePick.getSendPackageNum();
        Map<String, Integer> storagMap = inventoryUnit.getOutWmsStorag(wmsGoods, count);
        for (Map.Entry<String, Integer> storag : storagMap.entrySet()) {
            // WmsTask taskDesc=createDefaultTask(task, task.getBillid(), taskDesc);
            String Storagcode = storag.getKey();
            task.setTaskdesc("预拣货-" + Storagcode);
            // 创建预拣货任务单据
            WmsTaskBill wmsTaskBill = createWmsTaskBill(task, prePick, Storagcode);
            // 创建预拣货任务单据明细
            WmsTaskBillList wmsTaskBillList = createWmsTaskBillList(task, prePick);
            lockWmsStock(wmsTaskBill.getWhCode(), wmsTaskBill.getStorageCode(), null, wmsTaskBillList);
            taskUnit.addTask(task, wmsTaskBill, wmsTaskBillList);
            sendMessage(task);
        }
        return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "新增任务成功");
    }

    /**
     * 创建预拣货任务单据
     */
    private WmsTaskBill createWmsTaskBill(WmsTask task, TaskPrePick prePick, String Storagcode) {
        WmsTaskBill wmsTaskBill = new WmsTaskBill();
        wmsTaskBill.setBillid(prePick.getId());
        wmsTaskBill.setOraCode(prePick.getOraCode());
        wmsTaskBill.setOraName(prePick.getOraName());
        wmsTaskBill.setWhCode(prePick.getWhCode());
        wmsTaskBill.setStorageCode(Storagcode);
        wmsTaskBill.setNextStorageCode(Storagcode);
        wmsTaskBill.setSource(2);
        wmsTaskBill.setType(getTaskType());
        wmsTaskBill.setStatus(0);
        return wmsTaskBill;
    }

    /**
     * 创建预拣货任务单据明细
     */
    private WmsTaskBillList createWmsTaskBillList(WmsTask task, TaskPrePick prePick) {
        WmsTaskBillList wmsTaskBillList = new WmsTaskBillList();
        wmsTaskBillList.setBillid(prePick.getId());
        wmsTaskBillList.setGcode(prePick.getGcode());
        wmsTaskBillList.setGname(prePick.getGname());
        wmsTaskBillList.setOraCode(prePick.getOraCode());
        wmsTaskBillList.setOraName(prePick.getOraName());
        wmsTaskBillList.setGoodNeedNum(prePick.getPrePickNum());
        wmsTaskBillList.setBoxContent(prePick.getSendPackageNum());
        wmsTaskBillList.setBoxNum(prePick.getReqPackageNum());
        wmsTaskBillList.setGtype(prePick.getGtype());
        return wmsTaskBillList;
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

    @Override
    public void setTaskComplete(WmsTask task) {
        WmsTaskBill taskBill = task.getWmsTaskBill();
        List<WmsTaskBillList> taskBillLists = taskBill.getWmsTaskBillLists();
        TaskPrePick prePick = picPickService.getEntity(task.getBillid());
        for (WmsTaskBillList wmsTaskBillList : taskBillLists) {
            WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), prePick.getGtype(),
                    wmsTaskBillList.getOraCode(), prePick.getWhCode());
            inventoryUnit.preWmsStock(taskBill.getStorageCode(), wmsGoods, wmsTaskBillList.getGoodRealNum());
        }
    }

}
