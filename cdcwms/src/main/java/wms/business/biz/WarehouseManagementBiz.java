package wms.business.biz;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.orginfo.ExtOraTruck;
import com.wms.userRelation.ZoneManager;
import com.wms.warehouse.WmsDeliverDock;
import com.wms.warehouse.WmsDock;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsStorag;
import com.wms.warehouse.WmsStoragGroup;
import com.wms.warehouse.WmsWarehouse;
import com.wms.warehouse.WmsZone;

/**
 * 仓库管理
 *
 * @author Administrator
 *
 */
public interface WarehouseManagementBiz {

	/**
	 * 绑写收货道口
	 */
	public void bindWmsDock(String dockCode, String loginName);

	/**
	 * 删除车辆
	 */
	ResultResp delExtOraTruck(String id);

	/**
	 * 删除物料
	 */
	ResultResp delGoods(String id);

	/**
	 * 删除库位
	 */
	ResultResp delStorag(String id);

	/**
	 * 根据编号删除仓库
	 */
	ResultResp delWarehouseById(String id);

	/**
	 * 删除送货到达道口信息
	 */
	ResultResp delWmsDeliverDock(String id);

	/**
	 * 删除月台
	 */
	ResultResp delWmsDock(String id);

	/**
	 * 删除库位组信息
	 */
	ResultResp delWmsStoragGroup(String id);

	/**
	 * 删除库区
	 */
	ResultResp delZone(String id);

	/**
	 * 删除库区绑定
	 */
	ResultResp delZoneManager(String id);

	/**
	 * 查询全部月台
	 */
	List<WmsDock> getAllDock(String whCode);

	/**
	 * 获取下拉仓库列表
	 */
	List<Map<String, String>> getCombobox();

	/**
	 * 根据仓库代码获取库区信息
	 */
	List<Map<String, String>> getComboboxByWhCode(String whCode);

	/**
	 * 获取库位下拉框
	 */
	public List<Map<String, String>> getComboBoxStorage(String storageCode, String whCode);

	public List<Map<String, String>> getComboBoxStorageCode(String key, String whCode);

	/**
	 * 根据物料编码,零件用途来查找物料信息
	 */
	WmsGoods getGoodsInfo(String gcode, String gtype, String oraCode, String whCode);

	/**
	 * 获取下拉库位组列表
	 */

	List<Map<String, String>> getGroupCombobox(String key, String whCode);

	/**
	 * 获取库位责任人
	 */
	Set<String> getManager(String strageCode);

	String getNameByGcode(String gcode, String whCode);

	/**
	 * 车辆分页查询
	 */
	PageData<ExtOraTruck> getPageData(int page, int rows, ExtOraTruck extOraTruck);

	/**
	 * 查询分页数据
	 */
	PageData<ZoneManager> getPageData(int page, int rows, String loginname, String whCode);

	/**
	 * 送货到达道口信息分页查询
	 */
	PageData<WmsDeliverDock> getPageData(int page, int rows, WmsDeliverDock wmsDeliverDock);

	/**
	 * 物料分页查询
	 */
	PageData<WmsGoods> getPageData(int page, int rows, WmsGoods goods);

	/**
	 * 库位分页查询
	 */
	PageData<WmsStorag> getPageData(int page, int rows, WmsStorag storag);

	/**
	 * 库位组信息分页查询
	 */
	PageData<WmsStoragGroup> getPageData(int page, int rows, WmsStoragGroup storagGroup);

	/**
	 * 库区分页查询
	 */
	PageData<WmsZone> getPageData(int page, int rows, WmsZone zone);

	/**
	 * 月台分页查询
	 */
	PageData<WmsDock> getPageDataWmsDock(int page, int rows, String whCode);

	/**
	 * 仓库分页查询
	 */
	PageData<WmsWarehouse> getPageDataWmsWarehouse(int page, int rows, String name);

	/**
	 * 根据库位id查询库位信息
	 */
	WmsStorag getStorageByStorageCode(String storageCode, String whCode);

	List<ExtOraTruck> getTruck(String key, String whCode);

	/**
	 * 根据登陆名称获取库区集合
	 */
	List<Map<String, String>> getWarehouseMap(String loginname);

	/**
	 * 根据仓库代码获取仓库
	 */
	WmsWarehouse getWmsByCode(String whCode);

	/**
	 * 查询道口信息
	 */
	List<Map<String, String>> getWmsDeliverDockCombobox(String whCode);

	/**
	 * 根据月台号,查询收货道口
	 */

	WmsDock getWmsDockByDockCode(String dockCode, String whCode);

	WmsDock getWmsDockByTag(String tagId);

	/**
	 * 根据物料编号查询供货商
	 */
	List<Map<String, String>> getWmsGoodsByKey(String key, String whCode);

	/**
	 * 根据物料编号查询供货商
	 */
	List<Map<String, String>> getWmsGoodsInfoByKey(String key, String whCode);

	/**
	 * 获取下拉库区列表
	 */
	List<Map<String, String>> getZoneCombobox(String whCode);

	/**
	 * 保存、修改车辆
	 */
	ResultResp save(ExtOraTruck et);

	/**
	 * 保存、修改送货到达道口信息
	 */
	ResultResp save(WmsDeliverDock dock);

	/**
	 * 保存、修改月台
	 */
	ResultResp save(WmsDock dock);

	/**
	 * 保存、修改物料
	 */
	ResultResp save(WmsGoods goods);

	/**
	 * 保存、修改库位
	 */
	ResultResp save(WmsStorag storag);

	/**
	 * 保存、修改库位组信息
	 */
	ResultResp save(WmsStoragGroup storagGroup);

	/**
	 * 保存、修改库区
	 */
	ResultResp save(WmsZone zone);

	/**
	 * 保存库区绑定
	 */
	ResultResp save(ZoneManager zoneManager);

	/**
	 * 增加、修改仓库
	 **/
	ResultResp saveWarehouse(WmsWarehouse ws);

	/**
	 * 批量绑定库位组
	 */
	public ResultResp bindStoragGroup(WmsStorag storag, String[] storags);
}
