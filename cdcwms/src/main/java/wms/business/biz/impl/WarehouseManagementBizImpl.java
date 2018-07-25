package wms.business.biz.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.orginfo.ExtOraTruck;
import com.wms.userRelation.UserSessionID;
import com.wms.userRelation.ZoneManager;
import com.wms.warehouse.WmsDeliverDock;
import com.wms.warehouse.WmsDock;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsStorag;
import com.wms.warehouse.WmsStoragGroup;
import com.wms.warehouse.WmsWarehouse;
import com.wms.warehouse.WmsZone;

import wms.business.biz.WarehouseManagementBiz;
import wms.business.service.WmsDeliverDockService;
import wms.business.service.WmsDockService;
import wms.orginfo.service.TruckService;
import wms.userRelation.service.UserSessionIDService;
import wms.userRelation.service.ZoneManagerService;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragGroupService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;
import wms.warehouse.service.ZoneService;

@Service("warehouseManagementBiz")
public class WarehouseManagementBizImpl implements WarehouseManagementBiz {
	@Resource
	private WmsDeliverDockService deliverDockService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private StoragGroupService storagGroupService;
	@Resource
	private StoragService storagService;
	@Resource
	private TruckService truckService;
	@Resource
	private UserSessionIDService userSessionIDService;
	@Resource
	private WarehouseService warehouseService;
	@Resource
	private WmsDockService wmsDockService;
	@Resource
	private ZoneManagerService zoneManagerService;
	@Resource
	private ZoneService zoneService;

	@Override
	public void bindWmsDock(String dockCode, String loginName) {
		UserSessionID userSession = userSessionIDService.getUserInfo(loginName);
		userSession.setDockCode(dockCode);
		userSessionIDService.update(userSession);
	}

	@Override
	public ResultResp delExtOraTruck(String id) {
		return truckService.del(id);
	}

	@Override
	public ResultResp delGoods(String id) {
		return goodsService.del(id);
	}

	@Override
	public ResultResp delStorag(String id) {
		return storagService.del(id);
	}

	@Override
	public ResultResp delWarehouseById(String id) {
		return warehouseService.delWmsById(id);
	}

	@Override
	public ResultResp delWmsDeliverDock(String id) {
		return deliverDockService.del(id);
	}

	@Override
	public ResultResp delWmsDock(String id) {
		return wmsDockService.del(id);
	}

	@Override
	public ResultResp delWmsStoragGroup(String id) {
		return storagGroupService.del(id);
	}

	@Override
	public ResultResp delZone(String id) {
		return zoneService.del(id);
	}

	@Override
	public ResultResp delZoneManager(String id) {
		return zoneManagerService.del(id);
	}

	@Override
	public List<WmsDock> getAllDock(String whCode) {
		return wmsDockService.getAllDock(whCode);
	}

	@Override
	public List<Map<String, String>> getCombobox() {
		return warehouseService.getCombobox();
	}

	@Override
	public List<Map<String, String>> getComboboxByWhCode(String whCode) {
		return zoneService.getComboboxByWhCode(whCode);
	}

	@Override
	public List<Map<String, String>> getComboBoxStorage(String storageCode, String whCode) {
		return storagService.getComboBoxStorage(storageCode, whCode);
	}

	@Override
	public List<Map<String, String>> getComboBoxStorageCode(String key, String whCode) {
		return storagService.getComboBoxStorageCode(key, whCode);
	}

	@Override
	public WmsGoods getGoodsInfo(String gcode, String gtype, String oraCode, String whCode) {
		return goodsService.getGoodsInfo(gcode, gtype, oraCode, whCode);
	}

	@Override
	public List<Map<String, String>> getGroupCombobox(String key, String whCode) {
		return storagGroupService.getGroupCombobox(key, whCode);
	}

	@Override
	public Set<String> getManager(String strageCode) {
		return zoneManagerService.getManager(strageCode);
	}

	@Override
	public String getNameByGcode(String gcode, String whCode) {
		return goodsService.getNameByGcode(gcode, whCode);
	}

	@Override
	public PageData<ExtOraTruck> getPageData(int page, int rows, ExtOraTruck extOraTruck) {
		return truckService.getPageData(page, rows, extOraTruck);
	}

	@Override
	public PageData<ZoneManager> getPageData(int page, int rows, String loginname, String whCode) {
		return zoneManagerService.getPageData(page, rows, loginname, whCode);
	}

	@Override
	public PageData<WmsDeliverDock> getPageData(int page, int rows, WmsDeliverDock wmsDeliverDock) {
		return deliverDockService.getPageData(page, rows, wmsDeliverDock);
	}

	@Override
	public PageData<WmsGoods> getPageData(int page, int rows, WmsGoods goods) {
		return goodsService.getPageData(page, rows, goods);
	}

	@Override
	public PageData<WmsStorag> getPageData(int page, int rows, WmsStorag storag) {
		return storagService.getPageData(page, rows, storag);
	}

	@Override
	public PageData<WmsStoragGroup> getPageData(int page, int rows, WmsStoragGroup storagGroup) {
		return storagGroupService.getPageData(page, rows, storagGroup);
	}

	@Override
	public PageData<WmsZone> getPageData(int page, int rows, WmsZone zone) {
		return zoneService.getPageData(page, rows, zone);
	}

	@Override
	public PageData<WmsDock> getPageDataWmsDock(int page, int rows, String whCode) {
		return wmsDockService.getPageData(page, rows, whCode);
	}

	@Override
	public PageData<WmsWarehouse> getPageDataWmsWarehouse(int page, int rows, String name) {
		return warehouseService.getPageData(page, rows, name);
	}

	@Override
	public WmsStorag getStorageByStorageCode(String storageCode, String whCode) {
		return storagService.getStorageByStorageCode(storageCode, whCode);
	}

	@Override
	public List<ExtOraTruck> getTruck(String key, String whCode) {
		return truckService.getTruck(key, whCode);
	}

	@Override
	public List<Map<String, String>> getWarehouseMap(String loginname) {
		return zoneManagerService.getWarehouseMap(loginname);
	}

	@Override
	public WmsWarehouse getWmsByCode(String whCode) {
		return warehouseService.getWmsByCode(whCode);
	}

	@Override
	public List<Map<String, String>> getWmsDeliverDockCombobox(String whCode) {
		return deliverDockService.getDockCombobox(whCode);
	}

	@Override
	public WmsDock getWmsDockByDockCode(String dockCode, String whCode) {
		return wmsDockService.getWmsDockByDockCode(dockCode, whCode);
	}

	@Override
	public WmsDock getWmsDockByTag(String tagId) {
		return wmsDockService.getWmsDockByTag(tagId);
	}

	@Override
	public List<Map<String, String>> getWmsGoodsByKey(String key, String whCode) {
		return goodsService.getWmsGoodsByKey(key, whCode);
	}

	@Override
	public List<Map<String, String>> getWmsGoodsInfoByKey(String key, String whCode) {
		return goodsService.getWmsGoodsInfoByKey(key, whCode);
	}

	@Override
	public List<Map<String, String>> getZoneCombobox(String whCode) {
		return zoneService.getZoneCombobox(whCode);
	}

	@Override
	public ResultResp save(ExtOraTruck et) {
		return truckService.save(et);
	}

	@Override
	public ResultResp save(WmsDeliverDock dock) {
		return deliverDockService.save(dock);
	}

	@Override
	public ResultResp save(WmsDock dock) {
		return wmsDockService.save(dock);
	}

	@Override
	public ResultResp save(WmsGoods goods) {
		return goodsService.save(goods);
	}

	@Override
	public ResultResp save(WmsStorag storag) {
		return storagService.save(storag);
	}

	@Override
	public ResultResp save(WmsStoragGroup storagGroup) {
		return storagGroupService.save(storagGroup);
	}

	@Override
	public ResultResp save(WmsZone zone) {
		return zoneService.save(zone);
	}

	@Override
	public ResultResp save(ZoneManager zoneManager) {
		return zoneManagerService.save(zoneManager);
	}

	@Override
	public ResultResp saveWarehouse(WmsWarehouse ws) {
		return warehouseService.saveWms(ws);
	}

	@Override
	public ResultResp bindStoragGroup(WmsStorag storag, String[] storags) {
		return storagService.bindStoragGroup(storag, storags);
	}

}
