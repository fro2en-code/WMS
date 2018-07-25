package wms.business.service;

import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsDeliverDock;
import its.base.service.BaseService;

/**
 * 送货到达道口信息
 * 
 * @author wangzz
 *
 */
public interface WmsDeliverDockService extends BaseService<WmsDeliverDock> {

	/**
	 * 送货到达道口信息分页查询
	 */
	PageData<WmsDeliverDock> getPageData(int page, int rows, WmsDeliverDock wmsDeliverDock);

	/**
	 * 查询全部送货到达道口信息
	 */
	List<WmsDeliverDock> getAllDock(String whCode);

	/**
	 * 保存、修改送货到达道口信息
	 */
	ResultResp save(WmsDeliverDock dock);

	/**
	 * 删除送货到达道口信息
	 */
	ResultResp del(String id);

	/**
	 * 根据发运地代码查询类型
	 */
	WmsDeliverDock getType(String deliveryRec);

	/**
	 * 查询道口信息
	 */
	List<Map<String, String>> getDockCombobox(String whCode);
}
