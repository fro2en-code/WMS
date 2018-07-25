package wms.business.unit.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.StockReport;

import wms.business.service.StockReportService;
import wms.business.unit.ReportUnit;

@Service("reportUnit")
public class ReportUnitImpl implements ReportUnit {
	@Resource
	private StockReportService stockReportService;

	@Override
	public void createStockReport(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date endTime = calendar.getTime();
		calendar.add(Calendar.MONTH, -1);
		Date startTime = calendar.getTime();
		String reportDate = StringUtil.DateToStr(startTime, "yyyy-MM");
		List<StockReport> list = stockReportService.getStockReportData(startTime, endTime);
		for (StockReport stockReport : list) {
			// 没有出入库记录,不作统计
			if (null == stockReport.getInStock() && null == stockReport.getOutStock()) {
				continue;
			}
			if (null != stockReport.getOutStock()) {// 这里出库是用负数表示的
				stockReport.setOutStock(0 - stockReport.getOutStock());
			}
			stockReport.setReportDate(reportDate);
			stockReportService.saveEntity(stockReport);
		}
	}

	@Override
	public void runSotckReport() {
		createStockReport(new Date());
	}

	@Override
	public PageData<StockReport> queryStockMonthlyStatement(int page, int rows, StockReport stockReport) {
		return stockReportService.queryStockMonthlyStatement(page, rows, stockReport);
	}

	@Override
	public PageData<Map<String, Object>> queryOutStorage(int page, int rows, String SUPPL_NO, String PART_NO,
			String MAP_SHEET_NO, String begin_time, String end_time, String whCode) {
		List<Serializable> params = new ArrayList<>();
		StringBuffer listsql = new StringBuffer(
				"SELECT t.MAP_SHEET_NO,t.DELIVERY_REC,t.end_time end_time,t1.SUPPL_NO,t5.ora_name,t1.PART_NO,t5.inner_coding,t1.SEND_PACKAGE_NUM,t1.REQ_PACKAGE_NUM,t1.REQ_QTY,t.begin_time begin_time,t5.G_TYPE FROM wms_les_send t INNER JOIN wms_les_send_list t1 ON t1.MAP_SHEET_NO = t.MAP_SHEET_NO and t1.wh_code = t.wh_code INNER JOIN wms_goods t5 ON t5.G_CODE = t1.PART_NO AND t5.Wh_code = t.wh_code AND t5.G_TYPE = t.g_type AND t5.ora_code = t1.SUPPL_NO WHERE t.Status IN (2,4) AND t.wh_code = ? ");
		params.add(whCode);
		if (!StringUtil.isEmpty(SUPPL_NO)) {
			listsql.append(" AND t1.SUPPL_NO like ?");
			params.add("%" + SUPPL_NO + "%");
		}
		if (!StringUtil.isEmpty(PART_NO)) {
			listsql.append(" AND t1.PART_NO like ?");
			params.add("%" + PART_NO + "%");
		}
		if (!StringUtil.isEmpty(MAP_SHEET_NO)) {
			listsql.append(" AND t.MAP_SHEET_NO like ?");
			params.add("%" + MAP_SHEET_NO + "%");
		}
		if (!StringUtil.isEmpty(begin_time)) {
			listsql.append(" AND begin_time > ?");
			params.add(begin_time);
		}
		if (!StringUtil.isEmpty(end_time)) {
			listsql.append(" AND end_time< ?");
			params.add(end_time);
		}
		listsql.append(
				"UNION ALL SELECT t.MAP_SHEET_NO,t.DELIVERY_REC,t.end_time AS end_time,t1.SUPPL_NO, t5.ora_name,t1.PART_NO,t5.inner_coding,t1.SEND_PACKAGE_NUM,t1.REQ_PACKAGE_NUM,t1.REQ_QTY,t.begin_time begin_time,t5.G_TYPE FROM wms_handwork_send t INNER JOIN wms_handwork_send_list t1 ON t1.MAP_SHEET_NO = t.MAP_SHEET_NO and t1.wh_code = t.wh_code INNER JOIN wms_goods t5 ON t5.G_CODE = t1.PART_NO AND t5.Wh_code = t.wh_code AND t5.G_TYPE = t.g_type AND t5.ora_code = t1.SUPPL_NO WHERE t.Status IN (2,4) AND t.wh_code = ? ");
		params.add(whCode);
		if (!StringUtil.isEmpty(SUPPL_NO)) {
			listsql.append(" AND t1.SUPPL_NO like ?");
			params.add("%" + SUPPL_NO + "%");
		}
		if (!StringUtil.isEmpty(PART_NO)) {
			listsql.append(" AND t1.PART_NO like ?");
			params.add("%" + PART_NO + "%");
		}
		if (!StringUtil.isEmpty(MAP_SHEET_NO)) {
			listsql.append(" AND t.MAP_SHEET_NO like ?");
			params.add("%" + MAP_SHEET_NO + "%");
		}
		if (!StringUtil.isEmpty(begin_time)) {
			listsql.append(" AND begin_time > ?");
			params.add(begin_time);
		}
		if (!StringUtil.isEmpty(end_time)) {
			listsql.append(" AND end_time< ?");
			params.add(end_time);
		}
		StringBuffer countsql = new StringBuffer("Select  count(*) From (" + listsql + ") allInfo");
		return stockReportService.queryOutStroage(page, rows, listsql.toString() + " order by end_time desc",
				countsql.toString(), params);
	}

	@Override
	public PageData<Map<String, Object>> queryIntoStorage(int page, int rows, String SUPPL_NO, String PART_NO,
			String MAP_SHEET_NO, String begin_time, String end_time, String whCode) {
		List<Serializable> params = new ArrayList<>();
		StringBuffer listsql = new StringBuffer(
				"SELECT t.End_time, t1.SUPPL_NO, t5.ora_name, t1.PART_NO, t5.inner_coding , t1.SEND_PACKAGE_NUM, t1.REQ_PACKAGE_NUM, t1.receive_QTY AS REQ_QTY, t.begin_time, t5.G_TYPE , t.MAP_SHEET_NO FROM wms_les_receive t INNER JOIN wms_les_receive_list t1 ON t1.MAP_SHEET_NO = t.MAP_SHEET_NO and t1.wh_code = t.wh_code INNER JOIN wms_goods t5 ON t5.G_CODE = t1.PART_NO AND t5.Wh_code = t.wh_code AND t5.G_TYPE = t1.G_type AND t5.ora_code = t1.SUPPL_NO WHERE t.wh_code = ? and t.Status in (2,4) ");
		params.add(whCode);
		if (!StringUtil.isEmpty(SUPPL_NO)) {
			listsql.append(" And t5.ora_code like ?");
			params.add("%" + SUPPL_NO + "%");
		}
		if (!StringUtil.isEmpty(PART_NO)) {
			listsql.append(" AND t5.g_code like ?");
			params.add("%" + PART_NO + "%");
		}
		if (!StringUtil.isEmpty(MAP_SHEET_NO)) {
			listsql.append(" AND t.MAP_SHEET_NO like ?");
			params.add("%" + MAP_SHEET_NO + "%");
		}
		if (!StringUtil.isEmpty(begin_time)) {
			listsql.append(" AND t.begin_time > ?");
			params.add(begin_time);
		}
		if (!StringUtil.isEmpty(end_time)) {
			listsql.append(" AND t.end_time < ?");
			params.add(end_time);
		}

		listsql.append(
				" UNION ALL SELECT t.end_time, t1.SUPPL_NO, t5.ora_name, t1.PART_NO, t5.inner_coding , t1.SEND_PACKAGE_NUM, t1.REQ_PACKAGE_NUM, t1.receive_QTY AS REQ_QTY, t.begin_time, t5.G_TYPE , t.MAP_SHEET_NO FROM wms_handwork_receive t INNER JOIN wms_handwork_receive_list t1 ON t1.MAP_SHEET_NO = t.MAP_SHEET_NO and t1.wh_code = t.wh_code INNER JOIN wms_goods t5 ON t5.G_CODE = t1.PART_NO AND t5.Wh_code = t.wh_code AND t5.G_TYPE = t1.G_TYPE AND t5.ora_code = t1.SUPPL_NO WHERE t.wh_code = ? and t.Status in (2,4) ");
		params.add(whCode);
		if (!StringUtil.isEmpty(SUPPL_NO)) {
			listsql.append(" And t5.ora_code like ?");
			params.add("%" + SUPPL_NO + "%");
		}
		if (!StringUtil.isEmpty(PART_NO)) {
			listsql.append(" AND t5.g_code like ?");
			params.add("%" + PART_NO + "%");
		}
		if (!StringUtil.isEmpty(MAP_SHEET_NO)) {
			listsql.append(" AND t.MAP_SHEET_NO like ?");
			params.add("%" + MAP_SHEET_NO + "%");
		}
		if (!StringUtil.isEmpty(begin_time)) {
			listsql.append(" AND t.begin_time > ?");
			params.add(begin_time);
		}
		if (!StringUtil.isEmpty(end_time)) {
			listsql.append(" AND t.end_time < ?");
			params.add(end_time);
		}
		StringBuffer countsql = new StringBuffer("Select  count(*) From (" + listsql + ") allInfo");
		return stockReportService.queryIntoStorage(page, rows, listsql.toString() + " order by end_time desc",
				countsql.toString(), params);
	}

}
