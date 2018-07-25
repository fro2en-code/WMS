package wms.warehouse.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsStorag;

import its.base.service.BaseService;

/**
 * 库位接口
 *
 * @author wlx
 */
public interface StoragService extends BaseService<WmsStorag> {

	List<WmsStorag> getStorageByCode(String whCode, String... codes);

	List<WmsStorag> getStorageByZone(String whCode, String... zones);

	/**
	 * 库位分页查询
	 */
	PageData<WmsStorag> getPageData(int page, int rows, WmsStorag storag);

	/**
	 * 保存、修改库位
	 */
	ResultResp save(WmsStorag storag);

	/**
	 * 删除库位
	 */
	ResultResp del(String id);

	/**
	 * 根据库位id查询库位信息
	 */
	WmsStorag getStorageByStorageCode(String storageCode, String whCode);

	/**
	 * 根据库区id查询所有的库位
	 */
	List<WmsStorag> getStorageByZoneCode(String zoneCode, String whCode);

	/**
	 * 获取库位下拉框
	 */
	public List<Map<String, String>> getComboBoxStorage(String storageCode, String whCode);

	public List<Map<String, String>> getComboBoxStorageCode(String key, String whCode);

	@Override
	public Serializable saveEntity(WmsStorag storag);

	/**
	 * 批量绑定库位组
	 */
	public ResultResp bindStoragGroup(WmsStorag storag, String[] storags);
}
