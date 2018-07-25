package wms.business.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import its.base.service.BaseService;

import com.plat.common.page.PageData;
import com.wms.business.WarningReturnOutTime;

public interface WarningReturnOutTimeService extends
		BaseService<WarningReturnOutTime> {

	/**
	 * 库存总时长预警
	 */
	PageData<Map<String, Object>> queryWarningReturnOutTime(int page, int rows,
			String listsql, String countsql, List<Serializable> params);

}
