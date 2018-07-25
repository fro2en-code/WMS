package wms.business.biz.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.business.StockReport;

import wms.business.biz.ReportBiz;
import wms.business.unit.ReportUnit;

@Service("reportBiz")
public class ReportBizImpl implements ReportBiz {

	@Resource
	private ReportUnit reportUnit;

	@Override
	public PageData<StockReport> queryStockMonthlyStatement(int page, int rows, StockReport stockReport) {
		return reportUnit.queryStockMonthlyStatement(page, rows, stockReport);
	}

	@Override
	public PageData<Map<String, Object>> queryOutStorage(int page, int rows, String SUPPL_NO, String PART_NO,
			String MAP_SHEET_NO, String begin_time, String end_time, String whCode) {
		return reportUnit.queryOutStorage(page, rows, SUPPL_NO, PART_NO, MAP_SHEET_NO, begin_time, end_time, whCode);
	}

	@Override
	public PageData<Map<String, Object>> queryIntoStorage(int page, int rows, String SUPPL_NO, String PART_NO,
			String MAP_SHEET_NO, String begin_time, String end_time, String whCode) {
		return reportUnit.queryIntoStorage(page, rows, SUPPL_NO, PART_NO, MAP_SHEET_NO, begin_time, end_time, whCode);
	}

}
