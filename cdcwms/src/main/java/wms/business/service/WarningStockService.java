package wms.business.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.wms.business.WarningStock;

import its.base.service.BaseService;

public interface WarningStockService extends BaseService<WarningStock> {
	/**
	 * 库存数量预警
	 */
	PageData<Map<String, Object>> queryWarningStock(int page, int rows, String listsql, String countsql,
			List<Serializable> params);

}
