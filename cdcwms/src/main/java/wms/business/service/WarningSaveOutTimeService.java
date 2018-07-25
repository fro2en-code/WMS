package wms.business.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.wms.business.WarningSaveOutTime;

import its.base.service.BaseService;

public interface WarningSaveOutTimeService extends
		BaseService<WarningSaveOutTime> {
	/**
	 * 库存总时间预警
	 */
	PageData<Map<String, Object>> queryWarningSaveOutTime(int page, int rows,
			String listsql, String countsql, List<Serializable> params);

}
