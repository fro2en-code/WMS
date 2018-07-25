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
import com.plat.common.page.PageData;
import com.plat.common.utils.ExcelUtil.Excel;
import com.wms.business.StockReport;
import com.ymt.utils.ExcelUtils;
import com.ymt.utils.ExcelUtils.ExportExcelHelper;

import wms.business.biz.ReportBiz;

/**
 * 库存月报表
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/stockReport")
public class StockReportAction extends BaseAction {
	@Resource
	private ReportBiz reportBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletRequest req) {
		new Excel().setColumn("reportDate", "日期").setColumn("oraCode", "奇瑞供应商代码").setColumn("oraName", "供应商名称")
				.setColumn("goodsCode", "物料编码").setColumn("goodsAlias", "内部零件号").setColumn("goodsName", "物料名称")
				.setColumn("goodsType", "用途").setColumn("beforeStock", "上月结存").setColumn("inStock", "当月入库量")
				.setColumn("outStock", "当月出库量").setColumn("nowStock", "现余库存").setClassName(StockReport.class.getName())
				.setTempName("库存月报表").Finish(req);
		return "stockReportList";
	}

	@RequestMapping(value = "/list.action")
	@ResponseBody
	public PageData<StockReport> list(HttpSession session, int page, int rows, StockReport stockReport) {
		stockReport.setWhCode(getBindWhCode(session));
		return reportBiz.queryStockMonthlyStatement(page, rows, stockReport);
	}

	@RequestMapping("/export.action")
	@ResponseBody
	public void export(HttpServletResponse resp, HttpSession session, final String keyNo,
			final StockReport stockReport) {
		stockReport.setWhCode(getBindWhCode(session));
		ExportExcelHelper helper = new ExcelUtils.ExportExcelHelper() {

			@Override
			public PageData<? extends BaseModel> getPageDate(int page, int size) throws Exception {
				return reportBiz.queryStockMonthlyStatement(page, ExcelUtils.pageSize, stockReport);
			}

			@Override
			public String getKey() {
				return keyNo;
			}
		};
		helper.run(resp);
	}
}
