package wms.warehouse.service.impl;

import its.base.service.BaseServiceImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import wms.warehouse.service.WarehouseService;
import wms.warehouse.service.ZoneService;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsZone;

/**
 * 库区实现
 *
 */
@Service("zoneService")
public class ZoneServiceImpl extends BaseServiceImpl<WmsZone> implements ZoneService {

	@Resource
	private WarehouseService warehouseService;

	/**
	 * 查询分页
	 */
	@Override
	public PageData<WmsZone> getPageData(int page, int rows, WmsZone zone) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsZone where 1=1");
		if (!StringUtil.isEmpty(zone.getZoneCode())) {
			base_hql.append("and zoneCode = ?");
			params.add(zone.getZoneCode());
		}
		if (!StringUtil.isEmpty(zone.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(zone.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By zoneCode desc", page, rows, params);
	}

	/**
	 * 获取全部库区
	 */
	@Override
	public List<WmsZone> getAllZone() {
		return findEntityByHQL("From WmsZone ");
	}

	/**
	 * 保存、修改库区
	 */
	@Override
	public ResultResp save(WmsZone zone) {
		ResultResp resp = new ResultResp();
		String id = zone.getId();
		String check = "select count(*) From WmsZone Where zoneCode= ?";
		List<Object> params = new ArrayList<>();
		params.add(zone.getZoneCode());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("库区编号" + zone.getZoneCode() + "已经存在！");
			} else {
				zone.setUpdateUser("");
				saveEntity(zone);
				resp.setRetcode("0");
				resp.setRetmsg("新增库区成功！");
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(zone.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("库区编号" + zone.getZoneCode() + "已经存在！");
			} else {
				updateEntity(zone);
				resp.setRetcode("0");
				resp.setRetmsg("修改库区成功！");
			}
		}
		return resp;
	}

	/**
	 * 删除库区
	 */
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		WmsZone zone = getEntity(id);
		deleteEntity(zone);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功!");
		return resp;
	}

	/**
	 * 根据仓库代码获取库区信息
	 */
	@Override
	public List<Map<String, String>> getComboboxByWhCode(String whCode) {
		List<WmsZone> zones = findEntityByHQL("From WmsZone Where whCode=?", whCode);
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (WmsZone wz : zones) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", wz.getId());
			map.put("zoneCode", wz.getZoneCode());
			map.put("ZName", wz.getZName());
			dataList.add(map);
		}
		return dataList;
	}

	/**
	 * 获取下拉库区列表
	 */
	@Override
	public List<Map<String, String>> getZoneCombobox(String whCode) {
		List<WmsZone> wmszone = findEntityByHQL("From WmsZone where whCode=?", whCode);
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (WmsZone zone : wmszone) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", zone.getId());
			map.put("zonecode", zone.getZoneCode());
			map.put("zname", zone.getZName());
			map.put("whCode", zone.getWhCode());
			dataList.add(map);
		}
		return dataList;
	}

	/**
	 * 根据库区代码获取库区信息
	 */
	@Override
	public WmsZone getComboboxByZoneCode(String zoneCode) {
		return (WmsZone) uniqueResult("From WmsZone Where zoneCode=?", zoneCode);
	}

}
