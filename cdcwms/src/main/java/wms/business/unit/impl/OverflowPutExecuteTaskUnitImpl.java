package wms.business.unit.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.result.ResultResp;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsStock;
import com.wms.warehouse.WmsWarehouse;

import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;

/**
 * 溢库区上架
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年5月31日
 */
@Service("overflowPutExecuteTaskUnit")
public class OverflowPutExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Value("11")
    private int taskType;
    @Resource
    private GoodsService goodsService;
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource
    private StoragService storagService;

    @Override
    public int getTaskType() {
        return taskType;
    }

    @Resource
    private WarehouseService warehouseService;

    @Override
    public ResultResp createTask(WmsTask task) {
        WmsWarehouse wh = warehouseService.getWmsByCode(task.getWhCode());
        // 获取溢库区所有库存,执行上架?那这个任务不能太频烦执行. 还有一个问题,如果一个库位为空,然后多个任务对它执行上架会怎么样
        List<WmsStock> list = inventoryUnit.getWmsStockCount(wh.getSpillZone(), wh.getWhCode());
        WmsTaskBill taskBill = new WmsTaskBill();
        task.setWmsTaskBill(taskBill);
        taskBill.setStorageCode(wh.getSpillZone());
        taskBill.setNextStorageCode(wh.getSpillZone());
        taskBill.setWhCode(task.getWhCode());
        List<WmsTaskBillList> billList = new ArrayList<>();
        taskBill.setWmsTaskBillLists(billList);
        for (WmsStock wmsStock : list) {
            WmsTaskBillList bill = new WmsTaskBillList();
            bill.setGcode(wmsStock.getGcode());
            bill.setGname(wmsStock.getGname());
            bill.setGtype(wmsStock.getGtype());
            bill.setOraCode(wmsStock.getSupCode());
            bill.setOraName(wmsStock.getSupName());
            bill.setGoodNeedNum(wmsStock.getQuantity());
            bill.setGoodRealNum(wmsStock.getQuantity());
            billList.add(bill);
        }
        if (billList.size() > 0) {
            task.setId(UUID.randomUUID().toString());// 这里生成伪ID,方便后面的操作
            return new ResultResp(com.plat.common.result.ResultResp.SKIP_CODE, "请执行下一任务");
        } else {
            return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "不需要生成下一任务");
        }
    }
}
