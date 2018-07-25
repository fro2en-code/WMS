package wms.warehouse.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.Function;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsStorag;
import com.wms.warehouse.WmsStoragGroup;
import com.wms.warehouse.WmsWarehouse;
import com.wms.warehouse.WmsZone;

import its.base.service.BaseServiceImpl;
import wms.warehouse.service.StoragGroupService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;
import wms.warehouse.service.ZoneService;

/**
 * 库位实现
 *
 */
@Service("storagService")
public class StoragServiceImpl extends BaseServiceImpl<WmsStorag> implements StoragService {
	@Resource
	private WarehouseService warehouseService;
	@Resource
	private ZoneService zoneService;
	@Resource
	private StoragGroupService storagGroupService;

	/**
	 * 删除库位
	 */
	@CacheEvict(value = "service.storagService.*", allEntries = true)
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		WmsStorag storag = getEntity(id);
		deleteEntity(storag);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功!");
		return resp;
	}

	/**
	 * 分页查询
	 */
	@Override
	public PageData<WmsStorag> getPageData(int page, int rows, WmsStorag storag) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsStorag where 1=1");
		if (!StringUtil.isEmpty(storag.getStorageCode())) {
			base_hql.append(" and storageCode like ?");
			params.add("%" + storag.getStorageCode() + "%");
		}
		if (!StringUtil.isEmpty(storag.getZoneCode())) {
			base_hql.append(" and zoneCode like ?");
			params.add("%" + storag.getZoneCode() + "%");
		}
		if (!StringUtil.isEmpty(storag.getWhCode())) {
			base_hql.append(" and whCode = ?");
			params.add(storag.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By storageCode desc", page, rows, params);
	}

	@Cacheable("service.storagService.getStorageByCode")
	@Override
	public List<WmsStorag> getStorageByCode(String whCode, String... codes) {
		StringBuilder hql = new StringBuilder("From WmsStorag Where whCode=? and storageCode in (");
		List<Serializable> params = new ArrayList<>();
		params.add(whCode);
		for (String string : codes) {
			hql.append("?,");
			params.add(string);
		}
		hql.deleteCharAt(hql.length() - 1);
		hql.append(")");
		return findEntityByHQL(hql.toString(), params.toArray(new Serializable[params.size()]));
	}

	/**
	 * 保存、修改库位
	 */
	@CacheEvict(value = "service.storagService.*", allEntries = true)
	@Override
	public ResultResp save(WmsStorag storag) {
		ResultResp resp = new ResultResp();
		saveEntity(storag);
		resp.setRetcode("0");
		resp.setRetmsg("完成");
		return resp;
	}

	@CacheEvict(value = "service.storagService.*", allEntries = true)
	@Override
	public Serializable saveEntity(WmsStorag storag) {
		if (StringUtil.isEmpty(storag.getStorageCode())) {
			throw new RuntimeException("库位编码为空");
		}
		if (StringUtil.isEmpty(storag.getWhCode())) {
			throw new RuntimeException("库位编码为 " + storag.getStorageCode() + " 的仓库编码为空");
		}
		if (StringUtil.isEmpty(storag.getZoneCode())) {
			throw new RuntimeException("库位编码为 " + storag.getStorageCode() + " 的库区编码为空");
		}
		if (storag.getsType() == null) {
			throw new RuntimeException("库位编码为 " + storag.getStorageCode() + " 库位类别为空");
		}
		if (storag.getMulBth() == null) {
			throw new RuntimeException("库位编码为 " + storag.getStorageCode() + " 批次混放标志为空");
		}
		WmsWarehouse warehouse = warehouseService.getWmsByCode(storag.getWhCode());
		if (warehouse == null) {
			throw new RuntimeException("库位编码为 " + storag.getStorageCode() + " 的仓库编码不正确");
		}
		WmsZone wmsZone = zoneService.getComboboxByZoneCode(storag.getZoneCode());
		if (wmsZone == null) {
			throw new RuntimeException("库位编码为 " + storag.getStorageCode() + " 的库区编码不正确");
		}
		if (!StringUtil.isEmpty(storag.getGroupCode())) {// 当库位组不为空时,判断库位组是否正确
			WmsStoragGroup storagGroup = storagGroupService.getGroupByName(storag.getGroupCode(), storag.getWhCode());
			if (storagGroup == null) {
				throw new RuntimeException("请输入正确的库位组编码");
			}
		}
		String id = storag.getId();
		String check = "select count(*) From WmsStorag Where storageCode= ? and whCode = ?";
		List<Object> params = new ArrayList<>();
		params.add(storag.getStorageCode());
		params.add(storag.getWhCode());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				throw new RuntimeException("库位" + storag.getStorageCode() + "已经存在");
			} else {
				super.saveEntity(storag);
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(storag.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				throw new RuntimeException("库位" + storag.getStorageCode() + "已经存在");
			} else {
				updateEntity(storag);
			}
		}
		return null;
	};

	@SuppressWarnings("unchecked")
	@Override
	public List<WmsStorag> getStorageByZone(String whCode, String... zones) {
		StringBuilder sql = new StringBuilder("SELECT " + "wms_storage.ID, " + "wms_storage.STORAGE_CODE, "
				+ "wms_storage.S_type, " + "wms_storage.MUL_BTH, " + "wms_storage.MUL_SUP " + "FROM " + "wms_storage "
				+ "INNER JOIN " + "wms_zone ON wms_storage.ZONE_CODE = wms_zone.ZONE_CODE " + "LEFT JOIN "
				+ "wms_goods ON  wms_goods.storage_zone_id LIKE CONCAT('%',wms_storage.STORAGE_CODE , '%') "
				+ "AND wms_goods.storage_zone_type = 1 " + "WHERE " + "wms_goods.id IS NULL "
				+ "and wms_storage.wh_code =? AND wms_storage.ZONE_CODE IN (");
		List<Serializable> params = new ArrayList<>();
		params.add(whCode);
		for (String string : zones) {
			sql.append("?,");
			params.add(string);
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		Query query = getSession().createSQLQuery(sql.toString());
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		query.setResultTransformer(Transformers.aliasToBean(WmsStorag.class));
		return query.list();
	}

	@Cacheable("service.storagService.getStorageByStorageCode")
	@Override
	public WmsStorag getStorageByStorageCode(String storageCode, String whCode) {
		return (WmsStorag) uniqueResult("from WmsStorag where whCode=? and storageCode = ?", whCode, storageCode);
	}

	@Cacheable("service.storagService.getStorageByZoneCode")
	@Override
	public List<WmsStorag> getStorageByZoneCode(String zoneCode, String whCode) {
		return findEntityByHQL("from WmsStorag where whCode=? and zoneCode = ?", whCode, zoneCode);
	}

	@Cacheable("service.storagService.getComboBoxStorage")
	@Override
	public List<Map<String, String>> getComboBoxStorage(String storageCode, String whCode) {
		List<WmsStorag> storags = null;
		if (storageCode != null) {
			storags = findEntityByHQL("from WmsStorag where whCode=? and storageCode like ? order by storageCode",
					new Function<Query>() {
						@Override
						public void run(Query t) {
							t.setMaxResults(searchRows);
						}
					}, whCode, "%" + storageCode + "%");
		} else {
			storags = findEntityByHQL("from WmsStorag where whCode=?  order by storageCode", whCode);
		}
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (WmsStorag storag : storags) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("storagecode", storag.getStorageCode());
			dataList.add(map);
		}
		return dataList;
	}

	@Cacheable("service.storagService.getComboBoxStorageCode")
	@Override
	public List<Map<String, String>> getComboBoxStorageCode(String key, String whCode) {
		List<Map<String, String>> listMap = new ArrayList<>();
		if (StringUtil.isEmpty(key) || key.length() < minSearchLength) {
			return listMap;
		}
		key = "%" + key + "%";
		List<WmsStorag> list = findEntityByHQL(
				"from WmsStorag where whCode = ? and storageCode like ?  order by storageCode", new Function<Query>() {
					@Override
					public void run(Query t) {
						t.setMaxResults(searchRows);
					}
				}, whCode, key);
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (WmsStorag storag : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("storagecode", storag.getStorageCode());
			dataList.add(map);
		}
		return dataList;
	}

	@Override
	public ResultResp bindStoragGroup(WmsStorag storag, String[] storags) {
		ResultResp resp = new ResultResp();
		WmsStoragGroup wmsStoragGroup = storagGroupService.getGroupByName(storag.getGroupCode(), storag.getWhCode());
		if (wmsStoragGroup == null) {
			throw new RuntimeException("该库位组不存在,请重新核实!");
		}
		for (int i = 0; i < storags.length; i++) {
			WmsStorag wmsStorag = getStorageByStorageCode(storags[i], storag.getWhCode());
			wmsStorag.setGroupCode(storag.getGroupCode());
			wmsStorag.setUpdateUser(storag.getUpdateUser());
			wmsStorag.setUpdateTime(storag.getUpdateTime());
			updateEntity(wmsStorag);
		}
		resp.setRetcode("0");
		resp.setRetmsg("绑定成功!");
		return resp;
	}

}
