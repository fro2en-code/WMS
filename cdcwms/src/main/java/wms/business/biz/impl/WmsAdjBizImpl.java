package wms.business.biz.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsAdj;
import com.wms.warehouse.WmsGoods;

import wms.business.biz.WmsAdjBiz;
import wms.business.service.WmsAdjService;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;

@Service("wmsAdjBiz")
public class WmsAdjBizImpl implements WmsAdjBiz {
	@Resource
	private GoodsService goodsService;
	@Resource
	private InventoryUnit inventoryUnit;
	@Resource
	private StoragService storagService;
	@Resource
	private WmsAdjService wmsAdjService;

	@Override
	public ResultResp updateStock(WmsAdj wmsAdj) {
		ResultResp resultResp = new ResultResp();
		wmsAdj = wmsAdjService.getEntity(wmsAdj.getId());
		if (BaseModel.INT_INIT.equals(wmsAdj.getStatu())) {// 只有当状态处于未执行的时候才可以对库存进行操作
			WmsGoods wmsGoods = goodsService.getGoodsInfo(wmsAdj.getGcode(), wmsAdj.getGtype(), wmsAdj.getOraCode(),
					wmsAdj.getWhCode());
			// 默认不操作锁定库存
			boolean lockStock = false;
			// 获取物料库存总数
			int stockNum = inventoryUnit.getCountWmsStock(wmsAdj.getStorageCode(), wmsGoods, false);
			if (stockNum == 0 && !(wmsAdj.getType().equals(BaseModel.INT_INIT)
					|| wmsAdj.getType().equals(BaseModel.INT_CANCEL))) {// 当库存数为0,如果不是新增库存或者解锁库存时,不允许操作!
				resultResp.setRetcode("-1");
				resultResp.setRetmsg("物料库存为0,不允许操作!");
				return resultResp;
			}
			switch (wmsAdj.getType()) {
			case 0:
				return addStock(wmsAdj, wmsGoods, wmsAdj.getStorageCode(), lockStock, stockNum);
			case 1:
				return delStock(wmsAdj, wmsGoods, wmsAdj.getStorageCode(), lockStock, stockNum);
			case 2:
				return lockStock(wmsAdj, wmsGoods, wmsAdj.getStorageCode(), stockNum);
			case 3:
				return unlockStock(wmsAdj, wmsGoods, wmsAdj.getStorageCode(), stockNum);
			case 4:
				if (wmsAdj.getGtype().equals("生产件")) {
					resultResp.setRetcode("-1");
					resultResp.setRetmsg("生产件不能转生产件!");
					return resultResp;
				}
				return changeProductionParts(wmsAdj, wmsGoods, wmsAdj.getStorageCode(), lockStock, stockNum);
			case 5:
				if (wmsAdj.getGtype().equals("出口件")) {
					resultResp.setRetcode("-1");
					resultResp.setRetmsg("出口件不能转出口件!");
					return resultResp;
				}
				return changeSpareParts(wmsAdj, wmsGoods, wmsAdj.getStorageCode(), lockStock, stockNum);
			case 6:
				if (wmsAdj.getGtype().equals("备件")) {
					resultResp.setRetcode("-1");
					resultResp.setRetmsg("备件不能转备件!");
					return resultResp;
				}
				return changeExportParts(wmsAdj, wmsGoods, wmsAdj.getStorageCode(), lockStock, stockNum);
			default:
				resultResp.setRetcode("-1");
				resultResp.setRetmsg("当前单据已进行过操作,不允许再次操作!");
				return resultResp;
			}

		} else {
			resultResp.setRetcode("-1");
			resultResp.setRetmsg("当前单据已进行过操作,不允许再次操作!");
			return resultResp;
		}
	}

	private ResultResp addStock(WmsAdj wmsAdj, WmsGoods wmsGoods, String wmsStorag, boolean lockStock, int stockNum) {
		ResultResp resultResp = new ResultResp();
		inventoryUnit.addWmsStock(wmsStorag, wmsGoods, wmsAdj.getAdjNum(), lockStock, wmsAdj.getAdjCode());
		wmsAdj.setUsedNum(stockNum);
		wmsAdj.setGoalNum(stockNum + wmsAdj.getAdjNum());
		wmsAdj.setStatu(BaseModel.INT_COMPLETE);
		wmsAdj.setAdjTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
		wmsAdjService.updateEntity(wmsAdj);
		resultResp.setRetcode("0");
		resultResp.setRetmsg("新增库存成功！");
		return resultResp;
	}

	/**
	 * 转备件
	 */
	private ResultResp changeExportParts(WmsAdj wmsAdj, WmsGoods wmsGoods, String wmsStorag, boolean lockStock,
			int stockNum) {
		ResultResp resultResp = new ResultResp();
		if (wmsAdj.getAdjNum() <= stockNum) {
			// 查找该零件的物料信息是否有备件
			List<WmsGoods> goods = goodsService.findEntityByHQL(
					"from WmsGoods where gcode = ?  and gtype = ? and oraCode=? and whCode=?", wmsAdj.getGcode(), "备件",
					wmsAdj.getOraCode(), wmsAdj.getWhCode());
			if (goods.size() > 0) {// 有备件
				// 减去现在类型零件的库存
				inventoryUnit.addWmsStock(wmsStorag, wmsGoods, -wmsAdj.getAdjNum(), lockStock, wmsAdj.getAdjCode());
				// 增加备件零件的库存
				inventoryUnit.addWmsStock(wmsStorag, goods.get(0), wmsAdj.getAdjNum(), lockStock, wmsAdj.getAdjCode());
				wmsAdj.setStatu(BaseModel.INT_COMPLETE);
				wmsAdj.setAdjTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
				wmsAdjService.updateEntity(wmsAdj);
				resultResp.setRetcode("0");
				resultResp.setRetmsg("转备件成功！");
				return resultResp;
			} else {// 无备件不允许转化
				resultResp.setRetcode("-1");
				resultResp.setRetmsg("此物料没有备件的信息,不允许转化！");
				return resultResp;
			}
		} else {
			resultResp.setRetcode("-1");
			resultResp.setRetmsg("调整数量大于库存数量,不允许调整!");
			return resultResp;
		}
	}

	/**
	 * 转生产件
	 */
	private ResultResp changeProductionParts(WmsAdj wmsAdj, WmsGoods wmsGoods, String wmsStorag, boolean lockStock,
			int stockNum) {
		ResultResp resultResp = new ResultResp();
		if (wmsAdj.getAdjNum() <= stockNum) {
			// 查找该零件的物料信息是否有生产件
			List<WmsGoods> goods = goodsService.findEntityByHQL(
					"from WmsGoods where gcode = ?  and gtype = ? and oraCode=? and whCode=?", wmsAdj.getGcode(), "生产件",
					wmsAdj.getOraCode(), wmsAdj.getWhCode());
			if (goods.size() > 0) {// 有生产件
				// 减去现在类型零件的库存
				inventoryUnit.addWmsStock(wmsStorag, wmsGoods, -wmsAdj.getAdjNum(), lockStock, wmsAdj.getAdjCode());
				// 增加生产件零件的库存
				inventoryUnit.addWmsStock(wmsStorag, goods.get(0), wmsAdj.getAdjNum(), lockStock, wmsAdj.getAdjCode());
				wmsAdj.setStatu(BaseModel.INT_COMPLETE);
				wmsAdj.setAdjTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
				wmsAdjService.updateEntity(wmsAdj);
				resultResp.setRetcode("0");
				resultResp.setRetmsg("转生产件成功！");
				return resultResp;
			} else {// 无生产件不允许转化
				resultResp.setRetcode("-1");
				resultResp.setRetmsg("此物料没有生产件的信息,不允许转化！");
				return resultResp;
			}
		} else {
			resultResp.setRetcode("-1");
			resultResp.setRetmsg("调整数量大于库存数量,不允许调整!");
			return resultResp;
		}
	}

	/**
	 * 转出口件
	 */
	private ResultResp changeSpareParts(WmsAdj wmsAdj, WmsGoods wmsGoods, String wmsStorag, boolean lockStock,
			int stockNum) {
		ResultResp resultResp = new ResultResp();
		if (wmsAdj.getAdjNum() <= stockNum) {
			// 查找该零件的物料信息是否有出口件
			List<WmsGoods> goods = goodsService.findEntityByHQL(
					"from WmsGoods where gcode = ?  and gtype = ? and oraCode=? and whCode=?", wmsAdj.getGcode(), "出口件",
					wmsAdj.getOraCode(), wmsAdj.getWhCode());
			if (goods.size() > 0) {// 有出口件
				// 减去现在类型零件的库存
				inventoryUnit.addWmsStock(wmsStorag, wmsGoods, -wmsAdj.getAdjNum(), lockStock, wmsAdj.getAdjCode());
				// 增加出口件零件的库存
				inventoryUnit.addWmsStock(wmsStorag, goods.get(0), wmsAdj.getAdjNum(), lockStock, wmsAdj.getAdjCode());
				wmsAdj.setStatu(BaseModel.INT_COMPLETE);
				wmsAdj.setAdjTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
				wmsAdjService.updateEntity(wmsAdj);
				resultResp.setRetcode("0");
				resultResp.setRetmsg("转出口件成功！");
				return resultResp;
			} else {// 无出口件不允许转化
				resultResp.setRetcode("-1");
				resultResp.setRetmsg("此物料没有出口件的信息,不允许转化！");
				return resultResp;
			}
		} else {
			resultResp.setRetcode("-1");
			resultResp.setRetmsg("调整数量大于库存数量,不允许调整!");
			return resultResp;
		}
	}

	@Override
	public ResultResp delete(String id) {
		return wmsAdjService.deleteWmsAdj(id);
	}

	private ResultResp delStock(WmsAdj wmsAdj, WmsGoods wmsGoods, String wmsStorag, boolean lockStock, int stockNum) {
		ResultResp resultResp = new ResultResp();
		if (wmsAdj.getAdjNum() <= stockNum) {
			inventoryUnit.addWmsStock(wmsStorag, wmsGoods, -wmsAdj.getAdjNum(), lockStock, wmsAdj.getAdjCode());
			wmsAdj.setUsedNum(stockNum);
			wmsAdj.setGoalNum(stockNum - wmsAdj.getAdjNum());
			wmsAdj.setStatu(BaseModel.INT_COMPLETE);
			wmsAdj.setAdjTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
			wmsAdjService.updateEntity(wmsAdj);
			resultResp.setRetcode("0");
			resultResp.setRetmsg("减少库存成功！");
			return resultResp;
		} else {// 当调整数量大于库存数量时不允许操作
			resultResp.setRetcode("-1");
			resultResp.setRetmsg("调整数量大于库存数量,不允许调整!");
			return resultResp;
		}
	}

	@Override
	public WmsAdj getEntity(Serializable id) {
		return wmsAdjService.getEntity(id);
	}

	@Override
	public PageData<WmsAdj> getPageData(int page, int rows, WmsAdj wmsAdj) {
		return wmsAdjService.getPageData(page, rows, wmsAdj);
	}

	private ResultResp lockStock(WmsAdj wmsAdj, WmsGoods wmsGoods, String wmsStorag, int stockNum) {
		ResultResp resultResp = new ResultResp();
		if (wmsAdj.getAdjNum() <= stockNum) {
			inventoryUnit.lockWmsStock(wmsStorag, wmsGoods, wmsAdj.getAdjNum());// 1这个数字是随意写的,反正没用上
			wmsAdj.setStatu(BaseModel.INT_COMPLETE);
			wmsAdj.setAdjTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
			wmsAdjService.updateEntity(wmsAdj);
			resultResp.setRetcode("0");
			resultResp.setRetmsg("锁定库存成功！");
			return resultResp;
		} else {
			resultResp.setRetcode("-1");
			resultResp.setRetmsg("调整数量大于库存数量,不允许调整!");
			return resultResp;
		}
	}

	@Override
	public ResultResp save(WmsAdj wmsAdj) {
		return wmsAdjService.saveWmsAdj(wmsAdj);
	}

	private ResultResp unlockStock(WmsAdj wmsAdj, WmsGoods wmsGoods, String wmsStorag, int stockNum) {
		ResultResp resultResp = new ResultResp();
		inventoryUnit.unLockWmsStock(wmsStorag, wmsGoods, wmsAdj.getAdjNum());
		wmsAdj.setStatu(BaseModel.INT_COMPLETE);
		wmsAdj.setAdjTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
		wmsAdjService.updateEntity(wmsAdj);
		resultResp.setRetcode("0");
		resultResp.setRetmsg("解锁库存成功！");
		return resultResp;
	}

}
