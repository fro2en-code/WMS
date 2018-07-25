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
import com.wms.business.WmsLesReceive;
import com.wms.business.WmsLesReceiveList;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.orginfo.OrgConpany;
import com.wms.warehouse.WmsGoods;
import com.ymt.utils.DockUtils;

import wms.business.service.WmsLesReceiveListService;
import wms.business.service.WmsLesReceiveService;
import wms.business.unit.InventoryUnit;
import wms.business.unit.TaskUnit;
import wms.orginfo.service.CompanyService;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;

/**
 * WMS LES收货单 任务处理单元
 *
 * @author Administrator
 *
 */
@Service("lesReciveExecuteTaskUnit")
public class LesReciveExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Resource
    private CompanyService companyService;
    @Resource
    private DockUtils dockUtils;
    @Resource
    private GoodsService goodsService;
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource(name = "wmsLesReceiveListService")
    private WmsLesReceiveListService receiveListService;
    @Resource(name = "wmsLesReceiveService")
    private WmsLesReceiveService receiveService;
    @Resource
    private StoragService storagService;
    @Value("2")
    private int taskType;

    @Resource
    private TaskUnit taskUnit;

    @Override
    public ResultResp createTask(WmsTask task) {
        WmsLesReceive receiveBill = receiveService.getEntity(task.getBillid());
        List<WmsLesReceiveList> receiveListBills = receiveListService.getLesReceiveList(receiveBill.getMapSheetNo(),
                task.getWhCode());
        task.setLevel(receiveBill.getIsEmerge().intValue());
        WmsTaskBill wmsTaskBill = createWmsTaskBill(task, receiveBill);
        List<WmsTaskBillList> taskBillLists = createWmsTaskBillList(receiveListBills, task.getWhCode());
        taskUnit.addTask(task, wmsTaskBill, taskBillLists.toArray(new WmsTaskBillList[taskBillLists.size()]));
        // 获取Les收货单据
        WmsLesReceive wmsLesReceive = receiveService.getEntity(task.getBillid());
        wmsLesReceive.setStatus(BaseModel.INT_CREATE);
        receiveService.updateEntity(wmsLesReceive);
        // 发送消息
        sendMessage(task);
        return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "新增任务成功");
    }

    private WmsTaskBill createWmsTaskBill(WmsTask task, WmsLesReceive receiveBill) {
        WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
        // wmsTaskBill.setNextStorageCode(dockUtils.getStorage(task.getExecutorName()));
        wmsTaskBill.setBillid(task.getBillid());
        wmsTaskBill.setBillNumber(receiveBill.getMapSheetNo());
        wmsTaskBill.setWhCode(task.getWhCode());
        wmsTaskBill.setSource(0);
        wmsTaskBill.setType(1);
        return wmsTaskBill;
    }

    private List<WmsTaskBillList> createWmsTaskBillList(List<WmsLesReceiveList> receiveListBills, String whCode) {
        List<WmsTaskBillList> taskBillLists = new ArrayList<>();
        for (WmsLesReceiveList receiveList : receiveListBills) {
            WmsTaskBillList billList = new WmsTaskBillList();
            billList.setGcode(receiveList.getPartNo());
            String gName = goodsService.getNameByGcode(billList.getGcode(), whCode);
            billList.setGname(gName);
            String abcType = goodsService.getAbcByGcode(billList.getGcode(), whCode);
            billList.setAbcType(abcType);
            billList.setOraCode(receiveList.getSupplNo());
            OrgConpany orgConpany = companyService.getConpanyByConCode(receiveList.getSupplNo());
            billList.setOraName(orgConpany.getName());
            billList.setBoxCardid(receiveList.getSxCardNo());
            billList.setBoxContent(receiveList.getSendPackageNum());
            billList.setBoxNum(receiveList.getReqPackageNum());
            billList.setGoodNeedNum(receiveList.getSendQty());
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
    public void setTaskCancel(WmsTask task) {
        WmsLesReceive wmsLesReceive = receiveService.getEntity(task.getBillid());
        wmsLesReceive.setStatus(BaseModel.INT_CANCEL);
        receiveService.updateEntity(wmsLesReceive);
    }

    @Override
    public void setTaskComplete(WmsTask task) {
        WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
        List<WmsTaskBillList> wmsTaskBillLists = wmsTaskBill.getWmsTaskBillLists();
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), wmsTaskBill.getWhCode());
            if (null == wmsGoods) {// 这里有个问题,这里没有事务,库存记录添加了无法回滚
                throw new RuntimeException(wmsTaskBillList.getGcode() + "零件不存在");
            }
        }
        WmsLesReceive wmsLesReceive = receiveService.getEntity(task.getBillid());
        String mapSheetNo = wmsLesReceive.getMapSheetNo();
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            // 根据配送单号和随箱卡号找到对应数据,并且修改实收数量,
            WmsLesReceiveList wmsLesReceiveList = receiveListService.getLesReceiveList(mapSheetNo,
                    wmsTaskBillList.getBoxCardid(), wmsTaskBill.getWhCode());
            // 修改Les收货单据明细实收数量(先随便弄个数据模拟一下效果,以后在修改.)
            wmsLesReceiveList.setReceiveQty(wmsTaskBillList.getGoodRealNum());
            wmsLesReceiveList.setGtype(wmsTaskBillList.getGtype());
            receiveListService.saveNewLesReceiveList(wmsLesReceiveList);
            WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
                    wmsTaskBillList.getOraCode(), wmsTaskBill.getWhCode());
            if (null == wmsGoods) {// 这里有个问题,这里没有事务,库存记录添加了无法回滚
                throw new RuntimeException(wmsTaskBillList.getGcode() + "零件不存在");
            }
            int count = wmsLesReceiveList.getReceiveQty();
            inventoryUnit.addWmsStock(wmsTaskBill.getNextStorageCode(), wmsGoods, count, false, mapSheetNo);
        }
        wmsLesReceive.setStatus(BaseModel.INT_COMPLETE);
        wmsLesReceive.setEndTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        receiveService.updateEntity(wmsLesReceive);
    }

    @Override
    public void setTaskRecive(WmsTask task) {
        WmsLesReceive wmsLesReceive = receiveService.getEntity(task.getBillid());
        if (StringUtil.isEmpty(wmsLesReceive.getBeginTime())) {
            wmsLesReceive.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
            receiveService.updateEntity(wmsLesReceive);
        }
    }
}
