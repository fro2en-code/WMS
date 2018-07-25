package wms.business.biz.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import wms.business.biz.WarningBiz;
import wms.business.service.WarningReturnOutTimeService;
import wms.business.service.WarningSaveOutTimeService;
import wms.business.service.WarningSendTaskOutTimeService;
import wms.business.service.WarningStockService;
import wms.business.unit.WarningUnit;

@Service("WarningBiz")
public class WarningBizImpl implements WarningBiz {
	@Resource
	private WarningStockService warningStockService;
	@Resource
	private WarningSendTaskOutTimeService warningSendTaskOutTimeService;
	@Resource
	private WarningReturnOutTimeService warningReturnOutTimeService;
	@Resource
	private WarningSaveOutTimeService warningSaveOutTimeService;
	@Resource
	private WarningUnit warningUnit;

	@Override
	public PageData<Map<String, Object>> queryWarningStock(int page, int rows,
			String G_code, String G_NAME, String G_TYPE, String ora_code,
			String ora_name, String warning_max_num, String warning_min_num,
			String whCode) {
		return warningUnit.queryWarningStock(page, rows, G_code, G_NAME,
				G_TYPE, ora_code, ora_name, warning_max_num, warning_min_num,
				whCode);
	}

	@Override
	public PageData<Map<String, Object>> queryWarningSendTaskOutTime(int page,
			int rows, String Taskdesc, Integer Status, String executorName,
			String Billid, Integer Type, String whCode) {
		return warningUnit.queryWarningSendTaskOutTime(page, rows, Taskdesc,
				Status, executorName, Billid, Type, whCode);
	}

	@Override
	public PageData<Map<String, Object>> queryWarningReturnOutTime(int page,
			int rows, String MAP_SHEET_NO, Long IS_EMERGE,
			String DELIVERY_REC_TYPE, String mriCreateTime, String whCode) {
		return warningUnit.queryWarningReturnOutTime(page, rows, MAP_SHEET_NO,
				IS_EMERGE, DELIVERY_REC_TYPE, mriCreateTime, whCode);
	}

	@Override
	public PageData<Map<String, Object>> queryWarningSaveOutTime(int page,
			int rows, String sup_code, String Sup_name, String G_code,
			String g_name, String G_TYPE, String whCode) {
		return warningUnit.queryWarningSaveOutTime(page, rows, sup_code,
				Sup_name, G_code, g_name, G_TYPE, whCode);
	}

}
