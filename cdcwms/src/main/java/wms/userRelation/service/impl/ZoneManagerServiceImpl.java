package wms.userRelation.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.ZoneManager;
import com.wms.warehouse.WmsZone;

import its.base.service.BaseServiceImpl;
import wms.userRelation.service.ZoneManagerService;
import wms.warehouse.service.StoragGroupService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;
import wms.warehouse.service.ZoneService;

/**
 * 用户库区管理关系实现
 */
@Service("zoneManagerService")
public class ZoneManagerServiceImpl extends BaseServiceImpl<ZoneManager> implements ZoneManagerService {

	@Resource
	private WarehouseService warehouseService;

	@Resource
	private ZoneService zoneService;
	@Resource
	private StoragService storagService;
	@Resource
	private StoragGroupService storagGroupService;

	/**
	 * 删除库区绑定
	 */
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		ZoneManager uw = getEntity(id);
		deleteEntity(uw);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功！");
		return resp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getManager(String strageCode) {
		String sql = "SELECT " + "wms_zone_manager.User_loginname " + "FROM " + "wms_storage " + "INNER JOIN "
				+ "wms_zone_manager ON (wms_zone_manager.Groupid = wms_storage.group_code "
				+ "OR (wms_storage.group_code IS NULL " + "AND wms_zone_manager.Zone_code = wms_storage.ZONE_CODE)) "
				+ "WHERE " + "wms_storage.STORAGE_CODE = ?";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter(0, strageCode);
		return new HashSet<>(query.list());
	}

	/**
	 * 查询分页数据
	 */
	@Override
	public PageData<ZoneManager> getPageData(int page, int rows, String loginname, String whCode) {
		List<ZoneManager> zoneManager = new ArrayList<ZoneManager>();
		int totalRows = 0;
		if (!StringUtil.isEmpty(whCode)) {
			String hql = "From ZoneManager Where userloginname = ? and whcode = ?";
			int startIndex = (page - 1) * rows;
			totalRows = count("Select Count(*) " + hql, loginname, whCode);
			zoneManager = findEntitySplitPage(hql, startIndex, rows, loginname, whCode);
		} else {
			String hql = "From ZoneManager Where userloginname = ?";
			int startIndex = (page - 1) * rows;
			totalRows = count("Select Count(*) " + hql, loginname);
			zoneManager = findEntitySplitPage(hql, startIndex, rows, loginname);
		}

		// 获取所有的库区
		List<WmsZone> ww = zoneService.getAllZone();
		// 创建一个保存库区名字的集合
		Map<String, String> zoneName = new HashMap<>();

		for (WmsZone w : ww) {
			zoneName.put(w.getZoneCode(), w.getZName());
		}

		for (ZoneManager uw : zoneManager) {
			uw.setZonename(zoneName.get(uw.getZonecode()));
		}

		return new PageData<>(page, rows, totalRows, zoneManager);
	}

	/**
	 * 根据登陆名称获取绑定的库区
	 */
	@Override
	public List<Map<String, String>> getWarehouseMap(String loginname) {

		return null;
	}

	/**
	 * 保存库区
	 */
	@Override
	public ResultResp save(ZoneManager zoneManager) {
		ResultResp resp = new ResultResp();
		String groupId = zoneManager.getGroupid();// 库位组编码
		String zoneCode = zoneManager.getZonecode();// 库区编码
		if (groupId == null || groupId.isEmpty()) {// 库位组编码为空
			if (zoneCode == null || zoneCode.isEmpty()) {// 库区编码为空
				resp.setRetcode("-1");
				resp.setRetmsg("库位组和库区请填写一个");
			} else {// 库区编码不为空
				String check = "select count(*) from ZoneManager where zonecode = ? and userloginname = ?";
				List<Object> params = new ArrayList<>();
				params.add(zoneCode);
				params.add(zoneManager.getUserloginname());
				if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
					resp.setRetcode("-1");
					resp.setRetmsg("编号为:" + zoneCode + "的库区已经存在！");
				} else {
					saveEntity(zoneManager);
					resp.setRetcode("0");
					resp.setRetmsg("绑定成功！");
				}
			}
		} else {
			// 根据库位组编码获取到库位组信息
			String check = "select count(*) from ZoneManager where groupid = ? and userloginname = ?";
			List<Object> params = new ArrayList<>();
			params.add(groupId);
			params.add(zoneManager.getUserloginname());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("编号为:" + groupId + "的库位组已经存在！");
			} else {
				if (zoneCode == null || zoneCode.isEmpty()) {// 库位组编码不为空,库区编码为空
					saveEntity(zoneManager);
					resp.setRetcode("0");
					resp.setRetmsg("绑定成功！");
				} else {// 库区,库位组编码都不为空,直接绑定
					resp.setRetcode("-1");
					resp.setRetmsg("库位组和库区只能填写一个");
				}
			}

		}

		return resp;
	}

}
