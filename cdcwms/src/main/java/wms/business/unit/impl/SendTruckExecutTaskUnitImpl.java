package wms.business.unit.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.business.WmsHandworkSend;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsLesSendList;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsWarehouse;

import wms.business.service.WmsHandworkSendService;
import wms.business.service.WmsLesSendListService;
import wms.business.service.WmsLesSendService;
import wms.business.service.WmsWaybillListService;
import wms.business.service.WmsWaybillService;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;

@Service("sendTruckExecutTaskUnit")
public class SendTruckExecutTaskUnitImpl extends BaseExecuteTaskUnitImpl {
    @Resource
    private GoodsService goodsService;
    @Resource(name = "wmsHandworkSendService")
    private WmsHandworkSendService handSendService;
    @Resource(name = "wmsLesSendListService")
    private WmsLesSendListService lesSendListService;
    @Resource(name = "wmsLesSendService")
    private WmsLesSendService lesSendService;

    @Resource
    private StoragService storagService;

    @Value("10")
    private int taskType;

    @Resource
    private WarehouseService warehouseService;
    @Resource
    private WmsWaybillListService wmsWayBillListService;

    @Resource
    private WmsWaybillService wmsWayBillService;

    @Override
    public ResultResp createTask(WmsTask task) {
        WmsTaskBill taskBill = task.getWmsTaskBill();
        String billId = taskBill.getBillid();
        // 拣货任务全部完成才能创建发车任务
        if (!taskUnit.isAllTaskCompleteByBillId(billId)) {
            return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "任务未全部完成跳过执行");
        }
        WmsWarehouse wh = warehouseService.getWmsByCode(task.getWhCode());
        WmsTask sendTask = createDefaultTask(task, task.getId(), "发车申请-" + task.getWmsTaskBill().getBillNumber());
        WmsTaskBill sendTaskBill = sendTask.getWmsTaskBill();
        sendTaskBill.setOraCode(null);// 因为一个发货单有多个供应商,所以不能用这里的
        sendTaskBill.setOraName(null);
        sendTaskBill.setPreBillid(billId);
        sendTaskBill.setNextStorageCode(wh.getSendStorage());
        List<WmsTaskBillList> sendTaskBillLists = sendTaskBill.getWmsTaskBillLists();
        // 因为一个发货单可能对应多个任务,所以根据billID查出所有任务明细来执行(这里还有个问题,任务明细合并,以后再说吧)
        ResultRespT<List<WmsTaskBillList>> result = taskUnit.getWmsTaskBillListData(billId, task.getWhCode());
        if (!ResultResp.SUCCESS_CODE.equals(result.getRetcode())) {
            return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, "任务查询出错");
        }
        if (BaseModel.INT_INIT.equals(taskBill.getSource())) {// LES 单据
            // LES 单据存在任务生成,但是单据取消,生新生成任务的情况,此时部分随箱卡会作废,但是对应任务已完成,这里,不操作这些任务.这个逻辑处理起来真麻烦
            // LES 单据存在收货前补作废的情况,奇瑞那边真任性
            Set<String> boxCaridSet = new HashSet<>();
            WmsLesSend send = lesSendService.getEntity(task.getBillid());
            List<WmsLesSendList> list = lesSendListService.getWmsLesSendList(send.getMapSheetNo(), send.getWhCode());
            for (WmsLesSendList wmsLesSendList : list) {
                boxCaridSet.add(wmsLesSendList.getSxCardNo());
            }
            for (WmsTaskBillList wmsTaskBillList : result.getT()) {
                if (boxCaridSet.contains(wmsTaskBillList.getBoxCardid())) {
                    WmsTaskBillList billList = new WmsTaskBillList();
                    BeanUtils.copyProperties(wmsTaskBillList, billList);
                    billList.setId(null);
                    billList.setParentid(null);
                    sendTaskBillLists.add(billList);
                }
            }
        } else {// 手工单据
            for (WmsTaskBillList wmsTaskBillList : result.getT()) {
                WmsTaskBillList billList = new WmsTaskBillList();
                BeanUtils.copyProperties(wmsTaskBillList, billList);
                billList.setId(null);
                billList.setParentid(null);
                sendTaskBillLists.add(billList);
            }
        }
        if (sendTaskBillLists.size() > 0) {
            taskUnit.addTask(sendTask, sendTaskBill,
                    sendTaskBillLists.toArray(new WmsTaskBillList[sendTaskBillLists.size()]));
            sendMessage(sendTask);
        }
        return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "操作完成");
    }

    @Override
    public int getTaskType() {
        return taskType;
    }

    @Override
    public void setTaskComplete(WmsTask task) {
        WmsTaskBill taskBill = task.getWmsTaskBill();
        //
        if (BaseModel.INT_INIT.equals(taskBill.getSource())) {
            WmsLesSend send = lesSendService.getEntity(taskBill.getPreBillid());
            send.setStatus(6);
            lesSendService.updateEntity(send);
        } else {
            WmsHandworkSend send = handSendService.getEntity(taskBill.getPreBillid());
            if (BaseModel.INT_ERROR.equals(send.getStatus())) {
                throw new RuntimeException("当前发货单已取消,不允许发车");
            } else {
                send.setStatus(6);
                handSendService.updateEntity(send);
            }
        }
    }
}
