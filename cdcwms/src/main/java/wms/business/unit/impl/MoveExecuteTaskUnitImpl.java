package wms.business.unit.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.result.ResultResp;
import com.wms.business.WmsMove;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsGoods;

import wms.business.service.WmsMoveService;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;

/**
 * 移库任务
 *
 * @author Administrator
 *
 */
@Service("moveExecuteTaskUnit")
public class MoveExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
	@Value("8")
	private int taskType;
	@Resource
	private WmsMoveService wmsMoveService;
	@Resource
	private InventoryUnit inventoryUnit;
	@Resource
	private StoragService storagService;
	@Resource
	private GoodsService goodsService;

	@Override
	public ResultResp createTask(WmsTask task) {
		// 根据移库编码获取移库申请单信息,现在task表里面的buiiid就是move表中的主键id
		WmsMove wmsMove = wmsMoveService.getEntity(task.getBillid());
		// 创建移库任务单据
		WmsTaskBill wmsTaskBill = createWmsTaskBill(task, wmsMove);
		// 创建移库任务单据明细
		WmsTaskBillList wmsTaskBillList = createWmsTaskBillList(task, wmsMove);
		wmsMove.setStatu(1);
		wmsMoveService.updateEntity(wmsMove);
		lockWmsStock(wmsTaskBill.getWhCode(), wmsTaskBill.getStorageCode(), wmsTaskBill.getNextStorageCode(),
				wmsTaskBillList);
		taskUnit.addTask(task, wmsTaskBill, wmsTaskBillList);
		sendMessage(task);
		return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "新增收货任务成功");
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

	/**
	 * 创建移库任务单据
	 */
	private WmsTaskBill createWmsTaskBill(WmsTask task, WmsMove wmsMove) {
		WmsTaskBill wmsTaskBill = new WmsTaskBill();
		wmsTaskBill.setBillid(task.getBillid());
		wmsTaskBill.setTaskid(task.getId());
		wmsTaskBill.setBillNumber(wmsMove.getMoveCode());
		wmsTaskBill.setWhCode(wmsMove.getWhCode());
		wmsTaskBill.setStorageCode(wmsMove.getUsedStorageCode());
		wmsTaskBill.setNextStorageCode(wmsMove.getNewStorageCode());
		wmsTaskBill.setType(getTaskType());
		return wmsTaskBill;
	}

	/**
	 * 创建移库任务单据明细
	 */
	private WmsTaskBillList createWmsTaskBillList(WmsTask task, WmsMove wmsMove) {
		WmsTaskBillList wmsTaskBillList = new WmsTaskBillList();
		wmsTaskBillList.setBillid(task.getBillid());
		wmsTaskBillList.setGcode(wmsMove.getGcode());
		wmsTaskBillList.setGname(wmsMove.getGname());
		wmsTaskBillList.setGtype(wmsMove.getGtype());
		wmsTaskBillList.setOraCode(wmsMove.getSupCode());
		wmsTaskBillList.setOraName(wmsMove.getSupName());
		wmsTaskBillList.setGoodNeedNum(wmsMove.getMoveNum());
		wmsTaskBillList.setGoodRealNum(wmsMove.getMoveNum());
		return wmsTaskBillList;
	}

	@Override
	public int getTaskType() {
		return taskType;
	}

	@Override
	public void setTaskComplete(WmsTask task) {
		WmsMove wmsMove = wmsMoveService.getEntity(task.getBillid());
		WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
		List<WmsTaskBillList> wmsTaskBillLists = wmsTaskBill.getWmsTaskBillLists();
		for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
			// 物料信息
			WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsTaskBillList.getGcode(), wmsTaskBillList.getGtype(),
					wmsTaskBillList.getOraCode(), wmsTaskBill.getWhCode());
			// 移动数量
			int count = wmsTaskBillList.getGoodRealNum();
			// 调整库存
			inventoryUnit.moveWmsStock(wmsTaskBill.getStorageCode(), wmsTaskBill.getNextStorageCode(), wmsGoods, count,
					false);
		}
		wmsMove.setStatu(3);
		wmsMoveService.updateEntity(wmsMove);
	}

	@Override
	public void setTaskRecive(WmsTask task) {
		WmsMove wmsMove = wmsMoveService.getEntity(task.getBillid());
		wmsMove.setStatu(2);
		wmsMoveService.updateEntity(wmsMove);
	}

	@Override
	public void setTaskCancel(WmsTask task) {
		WmsMove wmsMove = wmsMoveService.getEntity(task.getBillid());
		wmsMove.setStatu(0);
		wmsMoveService.updateEntity(wmsMove);
	}

}
