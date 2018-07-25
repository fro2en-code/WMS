package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.BaseModel;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.ExcelUtil.Excel;
import com.wms.business.WmsStocktake;
import com.ymt.utils.ExcelUtils;
import com.ymt.utils.Lock;
import com.ymt.utils.ExcelUtils.ExportExcelHelper;

import wms.business.biz.StocktakeBiz;

/**
 * 盘库清单
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/stocktake")
public class StockTakeAction extends BaseAction {
	@Resource
	private StocktakeBiz stocktakeBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletRequest req) {
		new Excel().setColumn("takeWokerCode", "操作人").setColumn("takePlanCode", "盘库计划编码").setColumn("lineNum", "行号")
				.setColumn("storageCode", "货位编码").setColumn("whCode", "仓库代码").setColumn("gcode", "物料编码")
				.setColumn("gname", "物料名称").setColumn("oraCode", "供应商编码").setColumn("batchCode", "批次")
				.setColumn("quantity", "库存数量").setColumn("actQuantity", "实际库存数量").setColumn("statu", "状态")
				.setClassName(WmsStocktake.class.getName()).setTempName("盘库清单").Finish(req);
		return "stockTakeList";
	}

	/**
	 * 盘库清单分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsStocktake> getPageDataStockTakePlan(int page, int rows, HttpSession session,
			WmsStocktake wmsStocktake) {
		String whCode = getBindWhCode(session);
		wmsStocktake.setWhCode(whCode);
		return stocktakeBiz.getPageDataStockTake(page, rows, wmsStocktake);
	}

	/**
	 * 生成库存调整申请单
	 */
	@RequestMapping("/addAdj.action")
	@ResponseBody
	@Lock
	public ResultResp addAdj(HttpServletResponse resp, HttpSession session, String id) {
		WmsStocktake wmsStocktake = stocktakeBiz.getWmsStocktakeEntity(id);
		if (wmsStocktake.getQuantity().equals(wmsStocktake.getActQuantity())) {// 如果库存数量和实际数量想等则不允许生成库存调整申请单
			return new ResultResp(ResultResp.ERROR_CODE, "库存数量和实际数量相同,不允许生成库存调整申请单!");
		} else {
			if (wmsStocktake.getStatu().equals(BaseModel.INT_INIT)
					|| wmsStocktake.getStatu().equals(BaseModel.INT_CREATE)) {
				UserBean user = getUserInfo(session);
				return stocktakeBiz.addAdj(wmsStocktake, user.getLoginname());
			}
			return new ResultResp(ResultResp.ERROR_CODE, "该单据状态不允许生成库存调整申请单!");
		}
	}

	@RequestMapping("/export.action")
	@ResponseBody
	public void export(HttpServletResponse resp, HttpSession session, final String keyNo,
			final WmsStocktake wmsStocktake) {
		wmsStocktake.setWhCode(getBindWhCode(session));
		ExportExcelHelper helper = new ExcelUtils.ExportExcelHelper() {

			@Override
			public PageData<? extends BaseModel> getPageDate(int page, int size) throws Exception {
				return stocktakeBiz.getPageDataStockTake(page, ExcelUtils.pageSize, wmsStocktake);
			}

			@Override
			public String getKey() {
				return keyNo;
			}
		};
		helper.run(resp);
	}
}
