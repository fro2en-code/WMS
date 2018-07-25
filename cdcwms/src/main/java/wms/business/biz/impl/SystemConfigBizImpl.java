package wms.business.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.systemConfig.SystemConfig;

import wms.business.biz.SystemConfigBiz;
import wms.systemConfig.service.SystemConfigService;

@Service("SystemConfigBiz")
public class SystemConfigBizImpl implements SystemConfigBiz {
	@Resource
	private SystemConfigService systemConfigService;

	@Override
	public PageData<SystemConfig> getPageData(int page, int rows,
			SystemConfig systemConfig) {
		return systemConfigService.getPageData(page, rows, systemConfig);
	}

	@Override
	public ResultResp save(SystemConfig systemconfig) {
		return systemConfigService.save(systemconfig);
	}

	@Override
	public ResultResp del(String id) {
		return systemConfigService.del(id);
	}

	@Override
	public SystemConfig getSystemBysystemKey(String systemKey, String whCode) {
		return systemConfigService.getSystemBysystemKey(systemKey, whCode);
	}

}
