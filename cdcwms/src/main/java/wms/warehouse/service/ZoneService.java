package wms.warehouse.service;



import its.base.service.BaseService;

import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsZone;

/**
 * 库区接口
 * @author wlx
 */
public interface ZoneService extends BaseService<WmsZone>{

	/**
	 * 库区分页查询
	 */
	PageData<WmsZone> getPageData(int page, int rows,WmsZone zone);
	
	/**
	 * 查询全部库区
	 */
	List<WmsZone> getAllZone();
	
	
	/**
	 * 保存、修改库区
	 */
	ResultResp save(WmsZone zone);
	
	/**
	 * 删除库区
	 */
	ResultResp del(String id);
	
	
	/**
	 * 根据仓库代码获取库区信息
	 */
	List<Map<String,String>>getComboboxByWhCode(String whCode);

	
	/**
	 * 获取下拉库区列表
	 */
	List<Map<String, String>> getZoneCombobox(String whCode);

	/**
	 * 根据库区代码获取库区信息	
	 */
	WmsZone getComboboxByZoneCode(String zoneCode);
	
	
	
}
