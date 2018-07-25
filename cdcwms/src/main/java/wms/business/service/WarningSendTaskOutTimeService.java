package wms.business.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.wms.business.WarningSendTaskOutTime;

import its.base.service.BaseService;

public interface WarningSendTaskOutTimeService extends
		BaseService<WarningSendTaskOutTime> {

	/**
	 * 分拣任务接收超时预警
	 */
	PageData<Map<String, Object>> queryWarningSendTaskOutTime(int page,
			int rows, String listsql, String countsql, List<Serializable> params);

}
