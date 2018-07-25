package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.wms.warehouse.WmsStock;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsStockService extends BaseService<WmsStock> {

	/**
	 * 分页查询
	 */
	PageData<WmsStock> getPageData(int page, int rows, WmsStock wmsStock);

	/**
	 * 根据根据仓库代码、库区代码,供应商代码、物料代码,零件类型 查询零件实体,按批次号排序
	 */
	List<WmsStock> getWmsStock(WmsStock wmsStock);

	/**
	 * 获取库位零件总数
	 */
	List<WmsStock> getWmsStockCount(String storageCode, String whCode);

	/**
	 * 获取库位零件总数
	 */
	int getWmsStockCount(WmsStock wmsStock, boolean lock);

	/**
	 * 获取库位上所有零件信息
	 */
	List<WmsStock> getWmsStockObject(String storageCode, String whCode);
}
