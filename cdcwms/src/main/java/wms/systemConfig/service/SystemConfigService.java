package wms.systemConfig.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.systemConfig.SystemConfig;

import its.base.service.BaseService;

public interface SystemConfigService extends BaseService<SystemConfig> {
	PageData<SystemConfig> getPageData(int page, int rows,
			SystemConfig systemConfig);

	SystemConfig getSystemBysystemKey(String systemKey, String whCode);

	ResultResp del(String id);

	ResultResp save(SystemConfig systemconfig);

}
