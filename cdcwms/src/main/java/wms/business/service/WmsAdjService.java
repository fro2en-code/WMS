package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsAdj;

import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsAdjService extends BaseService<WmsAdj> {

	/**
	 * 分页查询
	 */
	PageData<WmsAdj> getPageData(int page, int rows, WmsAdj wmsAdj);

	/**
	 * 新增,修改
	 */

	ResultResp saveWmsAdj(WmsAdj wmsAdj);

	/**
	 * 删除
	 */
	ResultResp deleteWmsAdj(String id);
}
