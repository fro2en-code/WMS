package wms.business.biz;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.systemConfig.SystemConfig;

public interface SystemConfigBiz {
	/**
	 * 分页查询
	 */
	PageData<SystemConfig> getPageData(int page, int rows,
			SystemConfig systemConfig);

	/**
	 * 保存修改
	 */
	ResultResp save(SystemConfig systemconfig);

	/**
	 * 删除
	 */
		ResultResp del(String id);

	/**
	 * 根据key查询参数
	 */
	SystemConfig getSystemBysystemKey(String systemKey, String whCode);
}
