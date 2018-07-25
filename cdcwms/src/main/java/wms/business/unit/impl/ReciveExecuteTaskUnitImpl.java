package wms.business.unit.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsHandworkReceive;
import com.wms.business.WmsHandworkReceiveList;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.orginfo.OrgConpany;
import com.wms.warehouse.WmsGoods;
import com.ymt.utils.DockUtils;

import wms.business.service.WmsHandworkReceiveListService;
import wms.business.service.WmsHandworkReceiveService;
import wms.business.unit.InventoryUnit;
import wms.orginfo.service.CompanyService;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;

/**
 * WMS 手工收货单 任务处理单元
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年4月5日
 */
@Service("reciveExecuteTaskUnit")
public class ReciveExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Resource(name = "wmsHandworkReceiveListService")
    private WmsHandworkReceiveListService receiveListService;
    @Resource(name = "wmsHandworkReceiveService")
    private WmsHandworkReceiveService receiveService;
    @Value("1")
    private int taskType;

    @Resource
    private DockUtils dockUtils;
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource
    private StoragService storagService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private CompanyService companyService;

    @Override
    public ResultResp createTask(WmsTask task) {
        WmsHandworkReceive receiveBill = receiveService.getEntity(task.getBillid());
        List<WmsHandworkReceiveList> receiveListBills = receiveListService
                .getWmsHandworkReceiveList(receiveBill.getMapSheetNo(), task.getWhCode());
        //
        task.setLevel(receiveBill.getIsEmerge().intValue());
        //
        WmsTaskBill wmsTaskBill = createWmsTaskBill(task, receiveBill);
        //
        List<WmsTaskBillList> taskBillLists = createWmsTaskBillList(receiveListBills, task.getWhCode());
        taskUnit.addTask(task, wmsTaskBill, taskBillLists.toArray(new WmsTaskBillList[taskBillLists.size()]));
        WmsHandworkReceive wmsHandworkReceive = receiveService.getEntity(task.getBillid());
        wmsHandworkReceive.setStatus(BaseModel.INT_CREATE);
        receiveService.updateEntity(wmsHandworkReceive);
        // 发送消息
        sendMessage(task);
        return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "新增任务成功");
    }

    private WmsTaskBill createWmsTaskBill(WmsTask task, WmsHandworkReceive receiveBill) {
        WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
        wmsTaskBill.setBillid(task.getBillid());
        wmsTaskBill.setBillNumber(receiveBill.getMapSheetNo());
        wmsTaskBill.setWhCode(task.getWhCode());
        // 这 里已经从task里传来了
        // wmsTaskBill.setNextStorageCode(dockUtils.getStorage(task.getExecutorName()));
        wmsTaskBill.setSource(1);
        wmsTaskBill.setType(1);
        return wmsTaskBill;
    }

    private List<WmsTaskBillList> createWmsTaskBillList(List<WmsHandworkReceiveList> receiveListBills, String whCode) {
        List<WmsTaskBillList> taskBillLists = new ArrayList<>();
        for (WmsHandworkReceiveList receiveList : receiveListBills) {
            WmsTaskBillList billList = new WmsTaskBillList();
            billList.setGcode(receiveList.getPartNo());
            String gName = goodsService.getNameByGcode(billList.getGcode(), whCode);
            billList.setGname(gName);
            String abcType = goodsService.getAbcByGcode(billList.getGcode(), whCode);
            billList.setAbcType(abcType);
            billList.setOraCode(receiveList.getSupplNo());
            OrgConpany orgConpany = companyService.getConpanyByConCode(receiveList.getSupplNo());
            billList.setOraName(orgConpany.getName());
            billList.setBoxCardid(receiveList.getId());
            billList.setBoxContent(receiveList.getSendPackageNum());
            billList.setBoxNum(receiveList.getReqPackageNum());
            billList.setGoodNeedNum(receiveList.getReqQty());
            taskBillLists.add(billList);
        }
        return taskBillLists;
    }

    @Override
    public Set<String> getExector(WmsTask task) {
        return taskUtils.getTaskUserByTaskCode(taskType, task.getWhCode());
    }

    @Override
    public int getTaskType() {
        return taskType;
    }

    @Override
    public void setTaskComplete(WmsTask task) {
        WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
        List<WmsTaskBillList> wmsTaskBillLists = wmsTaskBill.getWmsTaskBillLists();
        WmsHandworkReceive wmsHandworkReceive = receiveService.getEntity(task.getBillid());
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), wmsTaskBill.getWhCode());
            if (null == wmsGoods) {// 这里有个问题,这里没有事务,库存记录添加了无法回滚
                throw new RuntimeException(wmsTaskBillList.getGcode() + "零件不存在");
            }
        }
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            // 获取手工收货单明细表
            WmsHandworkReceiveList wmsHandworkReceiveList = receiveListService
                    .getEntity(wmsTaskBillList.getBoxCardid());
            // 修改手工收货单据明细实收数量(先随便弄个数据模拟一下效果,以后在修改.)
            wmsHandworkReceiveList.setReceiveQty(wmsTaskBillList.getGoodRealNum());
            wmsHandworkReceiveList.setGtype(wmsTaskBillList.getGtype());
            receiveListService.saveWmsHandworkReceiveList(wmsHandworkReceiveList);
            WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), wmsTaskBill.getWhCode());
            if (null == wmsGoods) {// 这里有个问题,这里没有事务,库存记录添加了无法回滚
                throw new RuntimeException(wmsTaskBillList.getGcode() + "零件不存在");
            }
            int count = wmsHandworkReceiveList.getReceiveQty();
            inventoryUnit.addWmsStock(wmsTaskBill.getNextStorageCode(), wmsGoods, count, false,
                    wmsHandworkReceive.getMapSheetNo());
        }
        wmsHandworkReceive.setStatus(BaseModel.INT_COMPLETE);
        wmsHandworkReceive.setEndTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        receiveService.updateEntity(wmsHandworkReceive);
    }

    @Override
    public void setTaskCancel(WmsTask task) {
        // 当取消任务时,将收货单状态改为未生成任务
        WmsHandworkReceive wmsHandworkReceive = receiveService.getEntity(task.getBillid());
        wmsHandworkReceive.setStatus(BaseModel.INT_CANCEL);
        receiveService.updateEntity(wmsHandworkReceive);
    }

    @Override
    public void setTaskRecive(WmsTask task) {
        WmsHandworkReceive wmsHandworkReceive = receiveService.getEntity(task.getBillid());
        if (StringUtil.isEmpty(wmsHandworkReceive.getBeginTime())) {
            wmsHandworkReceive.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
            receiveService.updateEntity(wmsHandworkReceive);
        }
    }
}
