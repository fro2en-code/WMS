package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WmsStocktake;

import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsStocktakeService extends BaseService<WmsStocktake> {

	/**
	 * 分页查询
	 */
	PageData<WmsStocktake> getPageData(int page, int rows, WmsStocktake wmsStocktake);

	void update(WmsStocktake wmsStocktake);
}
