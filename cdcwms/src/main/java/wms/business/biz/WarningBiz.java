package wms.business.biz;

import java.util.Map;

import com.plat.common.page.PageData;

public interface WarningBiz {

	/**
	 * 库存数量预警分页
	 */
	PageData<Map<String, Object>> queryWarningStock(int page, int rows,
			String G_code, String G_NAME, String G_TYPE, String ora_code,
			String ora_name, String warning_max_num, String warning_min_num,
			String whCode);

	/**
	 * 配车回单预警分页
	 */
	PageData<Map<String, Object>> queryWarningReturnOutTime(int page, int rows,
			String MAP_SHEET_NO, Long IS_EMERGE, String DELIVERY_REC_TYPE,
			String mriCreateTime, String whCode);

	/**
	 * 分拣任务接收超时预警分页
	 */
	PageData<Map<String, Object>> queryWarningSendTaskOutTime(int page,
			int rows, String Taskdesc, Integer Status, String executorName,
			String Billid, Integer Type, String whCode);

	/**
	 * 库存总时长预警分页
	 */
	PageData<Map<String, Object>> queryWarningSaveOutTime(int page, int rows,
			String sup_code, String Sup_name, String G_code, String g_name,
			String G_TYPE, String whCode);
}
