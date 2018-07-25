package wms.warehouse.service;

import its.base.service.BaseService;
import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsStoragGroup;

/**
 * 库位组信息
 * 
 * @author wzz
 *
 */
public interface StoragGroupService extends BaseService<WmsStoragGroup> {

	/**
	 * 库位组信息分页查询
	 */
	PageData<WmsStoragGroup> getPageData(int page, int rows, WmsStoragGroup storagGroup);

	/**
	 * 查询全部库位组信息
	 */
	List<WmsStoragGroup> getAllStoragGroup();

	/**
	 * 保存、修改库位组信息
	 */
	ResultResp save(WmsStoragGroup storagGroup);

	/**
	 * 删除库位组信息
	 */
	ResultResp del(String id);

	/**
	 * 获取下拉库位组列表
	 */

	List<Map<String, String>> getGroupCombobox(String key, String whCode);

	/**
	 * 根据库位组名字获取库位组信息
	 */
	WmsStoragGroup getGroupByName(String groupName, String whCode);
}
