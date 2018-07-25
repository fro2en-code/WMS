package wms.business.biz;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsStocktake;
import com.wms.business.WmsStocktakePlan;

public interface StocktakeBiz {
	/**
	 * 盘库清单分页
	 */
	public PageData<WmsStocktake> getPageDataStockTake(int page, int rows, WmsStocktake wmsStocktake);

	public WmsStocktakePlan getWmsStocktakePlanEntity(String id);

	public WmsStocktake getWmsStocktakeEntity(String id);

	/**
	 * 盘库申请单分页
	 */
	public PageData<WmsStocktakePlan> getPageDataStockTakePlan(int page, int rows, WmsStocktakePlan wmsStockTakePlan);

	/**
	 * 新增,修改盘库申请单
	 */
	public ResultResp saveStockTakePlan(WmsStocktakePlan wmsStocktakePlan);

	/**
	 * 删除盘库申请单
	 */
	public ResultResp deleteStockTakePlan(String id);

	/**
	 * 生成库存调整申请单
	 */
	public ResultResp addAdj(WmsStocktake wmsStocktake, String userName);
}
