package wms.business.biz;

import java.util.Map;

import com.plat.common.page.PageData;
import com.wms.business.StockReport;

/**
 * 报表
 * 
 * @author Administrator
 *
 */
public interface ReportBiz {
	/**
	 * 查询库存月报表
	 */
	PageData<StockReport> queryStockMonthlyStatement(int page, int rows, StockReport stockReport);

	/**
	 * 查询出库明细
	 */
	PageData<Map<String, Object>> queryOutStorage(int page, int rows, String SUPPL_NO, String PART_NO,
			String MAP_SHEET_NO, String begin_time, String end_time, String whCode);

	/**
	 * 查询入库明细
	 */
	PageData<Map<String, Object>> queryIntoStorage(int page, int rows, String SUPPL_NO, String PART_NO,
			String MAP_SHEET_NO, String begin_time, String end_time, String whCode);
}
