
package wms.business.unit.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsStocktake;
import com.wms.business.WmsStocktakePlan;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsStock;
import com.wms.warehouse.WmsStorag;

import wms.business.service.WmsStocktakePlanService;
import wms.business.service.WmsStocktakeService;
import wms.business.unit.InventoryUnit;
import wms.business.unit.TaskUnit;
import wms.warehouse.service.StoragService;

/**
 * 根据盘库申请单生成盘库任务
 *
 * @author wangzz
 *
 */
@Service("checkExecuteTaskUnit")
public class CheckExecuteTaskUnitImpl extends BaseExecuteTaskUnitImpl {
	@Value("9")
	private int taskType;
	@Resource
	private WmsStocktakePlanService wmsStocktakePlanService;
	@Resource
	private WmsStocktakeService wmsStocktakeService;
	@Resource
	private StoragService storagService;
	@Resource
	private InventoryUnit inventoryUnit;
	@Resource
	private TaskUnit taskUnit;

	@Override
	public ResultResp createTask(WmsTask task) {
		// 获取盘库申请表
		WmsStocktakePlan wmsStocktakePlan = wmsStocktakePlanService.getEntity(task.getBillid());
		if (wmsStocktakePlan.getTakeType() == 0) {// 按照库位来进行盘点
			WmsTaskBill wmsTaskBill = createWmsTaskBill(task, wmsStocktakePlan.getStorageCode(), wmsStocktakePlan);
			List<WmsTaskBillList> wmsTaskBillLists = createWmsTaskBillList(task, wmsStocktakePlan.getStorageCode());
			if (wmsTaskBillLists.size() > 0) {
				taskUnit.addTask(task, wmsTaskBill,
						wmsTaskBillLists.toArray(new WmsTaskBillList[wmsTaskBillLists.size()]));
				wmsStocktakePlan.setStatus(BaseModel.INT_CREATE);
				wmsStocktakePlanService.updateEntity(wmsStocktakePlan);
				sendMessage(task);
			} else {
				return new ResultResp(com.plat.common.result.ResultResp.ERROR_CODE, "该库位上没有任何零件,无法生成任务!");
			}
		} else if (wmsStocktakePlan.getTakeType() == 1) {// 按照库区来进行盘点
			// 查询库区的所有库位
			List<WmsStorag> wmsStorags = storagService.getStorageByZoneCode(wmsStocktakePlan.getStorageCode(),
					task.getWhCode());
			for (WmsStorag wmsStorag : wmsStorags) {
				WmsTask wmstask = new WmsTask();
				BeanUtils.copyProperties(task, wmstask);
				if (inventoryUnit.getWmsStockCount(wmsStorag.getStorageCode(), wmstask.getWhCode()).size() > 0) {// 当库位有零件信息时才生成任务
					wmstask.setTaskdesc("盘库-" + wmsStorag.getStorageCode());
					WmsTaskBill wmsTaskBill = createWmsTaskBill(wmstask, wmsStorag.getStorageCode(), wmsStocktakePlan);
					List<WmsTaskBillList> wmsTaskBillLists = createWmsTaskBillList(wmstask, wmsStorag.getStorageCode());
					if (wmsTaskBillLists.size() > 0) {
						taskUnit.addTask(wmstask, wmsTaskBill,
								wmsTaskBillLists.toArray(new WmsTaskBillList[wmsTaskBillLists.size()]));
						sendMessage(wmstask);
					}
				}

			}
		}
		wmsStocktakePlan.setStatus(BaseModel.INT_CREATE);
		wmsStocktakePlanService.updateEntity(wmsStocktakePlan);
		return new ResultResp(com.plat.common.result.ResultResp.SUCCESS_CODE, "新增任务成功");
	}

	private WmsTaskBill createWmsTaskBill(WmsTask task, String storageCode, WmsStocktakePlan wmsStocktakePlan) {
		WmsTaskBill wmsTaskBill = new WmsTaskBill();
		wmsTaskBill.setBillid(task.getBillid());
		wmsTaskBill.setBillNumber(wmsStocktakePlan.getTakePlanCode());
		wmsTaskBill.setWhCode(task.getWhCode());
		wmsTaskBill.setStorageCode(storageCode);
		wmsTaskBill.setNextStorageCode(storageCode);
		wmsTaskBill.setSource(2);
		wmsTaskBill.setType(9);
		return wmsTaskBill;
	}

	private List<WmsTaskBillList> createWmsTaskBillList(WmsTask task, String storageCode) {
		List<WmsTaskBillList> wmsTaskBillLists = new ArrayList<WmsTaskBillList>();
		// 获取当前库位所有的零件,具体信息有gcode,gname,gtype和零件总数4个信息
		List<WmsStock> wmsStocks = inventoryUnit.getWmsStockCount(storageCode, task.getWhCode());
		if (wmsStocks.size() == 0 || wmsStocks == null) {// 如果获取不到库位零件数量则代表库位上所有零件库存数量为0,此时获取库位上所有零件,数量设置为0
			wmsStocks = inventoryUnit.getWmsStockObject(storageCode, task.getWhCode());
			for (WmsStock wmsStock : wmsStocks) {
				WmsTaskBillList wmsTaskBillList = new WmsTaskBillList();
				wmsTaskBillList.setBillid(task.getBillid());
				wmsTaskBillList.setGcode(wmsStock.getGcode());
				wmsTaskBillList.setGname(wmsStock.getGname());
				wmsTaskBillList.setOraCode(wmsStock.getSupCode());
				wmsTaskBillList.setOraName(wmsStock.getSupName());
				wmsTaskBillList.setGtype(wmsStock.getGtype());
				wmsTaskBillList.setGoodNeedNum(0);
				wmsTaskBillLists.add(wmsTaskBillList);
			}
		} else {
			for (WmsStock wmsStock : wmsStocks) {
				WmsTaskBillList wmsTaskBillList = new WmsTaskBillList();
				wmsTaskBillList.setBillid(task.getBillid());
				wmsTaskBillList.setGcode(wmsStock.getGcode());
				wmsTaskBillList.setGname(wmsStock.getGname());
				wmsTaskBillList.setOraCode(wmsStock.getSupCode());
				wmsTaskBillList.setOraName(wmsStock.getSupName());
				wmsTaskBillList.setGtype(wmsStock.getGtype());
				wmsTaskBillList.setGoodNeedNum(wmsStock.getQuantity());
				wmsTaskBillLists.add(wmsTaskBillList);
			}
		}
		return wmsTaskBillLists;
	}

	@Override
	public int getTaskType() {
		return taskType;
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
	public void setTaskComplete(WmsTask task) {
		WmsTaskBill wmsTaskBill = task.getWmsTaskBill();
		// 获取盘库申请单
		WmsStocktakePlan wmsStocktakePlan = wmsStocktakePlanService.getEntity(task.getBillid());
		List<WmsTaskBillList> wmsTaskBillLists = wmsTaskBill.getWmsTaskBillLists();
		// 生成盘库清单
		for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
			WmsStocktake wmsStocktake = new WmsStocktake();
			wmsStocktake.setTakeWokerCode(task.getExecutorId());
			wmsStocktake.setTakePlanCode(wmsStocktakePlan.getTakePlanCode());
			wmsStocktake.setWhCode(wmsStocktakePlan.getWhCode());
			wmsStocktake.setStorageCode(wmsTaskBill.getStorageCode());
			wmsStocktake.setGcode(wmsTaskBillList.getGcode());
			wmsStocktake.setGname(wmsTaskBillList.getGname());
			wmsStocktake.setGtype(wmsTaskBillList.getGtype());
			wmsStocktake.setOraCode(wmsTaskBillList.getOraCode());
			wmsStocktake.setQuantity(wmsTaskBillList.getGoodNeedNum());
			wmsStocktake.setActQuantity(wmsTaskBillList.getGoodRealNum());
			wmsStocktake.setStatu(1);
			wmsStocktakeService.saveEntity(wmsStocktake);
		}
		if (taskUnit.isAllTaskCompleteByBillId(task.getBillid())) {
			wmsStocktakePlan.setStatus(BaseModel.INT_COMPLETE);
			wmsStocktakePlanService.updateEntity(wmsStocktakePlan);
		}
	}
}