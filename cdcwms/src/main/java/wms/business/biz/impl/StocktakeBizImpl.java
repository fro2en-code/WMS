package wms.business.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsAdj;
import com.wms.business.WmsStocktake;
import com.wms.business.WmsStocktakePlan;

import wms.business.biz.StocktakeBiz;
import wms.business.service.WmsAdjService;
import wms.business.service.WmsStocktakePlanService;
import wms.business.service.WmsStocktakeService;

@Service("stocktakeBiz")
public class StocktakeBizImpl implements StocktakeBiz {
	@Resource
	private WmsStocktakePlanService wmsStocktakePlanService;
	@Resource
	private WmsStocktakeService wmsStocktakeService;
	@Resource
	private WmsAdjService wmsAdjService;

	@Override
	public ResultResp deleteStockTakePlan(String id) {
		return wmsStocktakePlanService.deleteStocktakePlan(id);
	}

	@Override
	public WmsStocktakePlan getWmsStocktakePlanEntity(String id) {
		return wmsStocktakePlanService.getEntity(id);
	}

	@Override
	public PageData<WmsStocktake> getPageDataStockTake(int page, int rows, WmsStocktake wmsStocktake) {
		return wmsStocktakeService.getPageData(page, rows, wmsStocktake);
	}

	@Override
	public PageData<WmsStocktakePlan> getPageDataStockTakePlan(int page, int rows, WmsStocktakePlan wmsStockTakePlan) {
		return wmsStocktakePlanService.getPageData(page, rows, wmsStockTakePlan);
	}

	@Override
	public ResultResp saveStockTakePlan(WmsStocktakePlan wmsStocktakePlan) {
		return wmsStocktakePlanService.saveStocktakePlan(wmsStocktakePlan);
	}

	@Override
	public WmsStocktake getWmsStocktakeEntity(String id) {
		return wmsStocktakeService.getEntity(id);
	}

	@Override
	public ResultResp addAdj(WmsStocktake wmsStocktake, String userName) {
		ResultResp resultResp = new ResultResp();
		WmsAdj wmsAdj = new WmsAdj();
		wmsAdj.setWhCode(wmsStocktake.getWhCode());
		wmsAdj.setAdjTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
		wmsAdj.setAdjReason("根据编号为:" + wmsStocktake.getTakePlanCode() + "的盘库单生成");
		wmsAdj.setUpdateUser(userName);
		wmsAdj.setGcode(wmsStocktake.getGcode());
		wmsAdj.setGtype(wmsStocktake.getGtype());
		wmsAdj.setOraCode(wmsStocktake.getOraCode());
		wmsAdj.setStorageCode(wmsStocktake.getStorageCode());
		if (wmsStocktake.getQuantity() > wmsStocktake.getActQuantity()) {// 如果实际数量大于库存数量则减少库存,否则库存增加
			wmsAdj.setAdjNum(wmsStocktake.getQuantity() - wmsStocktake.getActQuantity());
			wmsAdj.setType(BaseModel.INT_CREATE);
		} else {
			wmsAdj.setAdjNum(wmsStocktake.getActQuantity() - wmsStocktake.getQuantity());
			wmsAdj.setType(BaseModel.INT_INIT);
		}
		wmsAdj.setLockStock(BaseModel.INT_CREATE);
		wmsAdj.setStatu(BaseModel.INT_INIT);
		wmsAdjService.saveWmsAdj(wmsAdj);
		wmsStocktake.setStatu(BaseModel.INT_COMPLETE);
		wmsStocktakeService.update(wmsStocktake);
		resultResp.setRetcode("0");
		resultResp.setRetmsg("新增库存调整申请单成功！");
		return resultResp;
	}
}
