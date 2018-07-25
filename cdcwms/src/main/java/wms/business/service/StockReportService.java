package wms.business.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.wms.business.StockReport;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年6月8日
 */
public interface StockReportService extends BaseService<StockReport> {
	List<StockReport> getStockReportData(Date startTime, Date endTime);

	/**
	 * 库存月报表查询
	 */
	PageData<StockReport> queryStockMonthlyStatement(int page, int rows, StockReport stockReport);

	/**
	 * 出库明细查询
	 */
	PageData<Map<String, Object>> queryOutStroage(int page, int rows, String listsql, String countsql,
			List<Serializable> params);

	/**
	 * 入库明细查询
	 */
	PageData<Map<String, Object>> queryIntoStorage(int page, int rows, String listsql, String countsql,
			List<Serializable> params);
}
