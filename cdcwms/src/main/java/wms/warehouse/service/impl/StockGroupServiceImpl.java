package wms.warehouse.service.impl;

import its.base.service.BaseServiceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import wms.warehouse.service.StoragGroupService;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.Function;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsStoragGroup;

/**
 * 库位组信息实现
 * 
 * @author wzz
 *
 */
@Service("storagGroupService")
public class StockGroupServiceImpl extends BaseServiceImpl<WmsStoragGroup> implements StoragGroupService {

	/**
	 * 查询分页
	 */
	@Override
	public PageData<WmsStoragGroup> getPageData(int page, int rows, WmsStoragGroup storagGroup) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsStoragGroup where 1=1");
		if (!StringUtil.isEmpty(storagGroup.getGroupName())) {
			base_hql.append("and groupName = ?");
			params.add(storagGroup.getGroupName());
		}
		if (!StringUtil.isEmpty(storagGroup.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(storagGroup.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By groupName desc", page, rows, params);
	}

	/**
	 * 获取全部库位组信息
	 */
	@Override
	public List<WmsStoragGroup> getAllStoragGroup() {
		return findEntityByHQL("from WmsStoragGroup");
	}

	/**
	 * 保存、修改库位组信息
	 */
	@Override
	public ResultResp save(WmsStoragGroup storagGroup) {
		ResultResp resp = new ResultResp();
		String id = storagGroup.getId();
		String check = "select count(*) From WmsStoragGroup Where groupName= ?";
		List<Object> params = new ArrayList<>();
		params.add(storagGroup.getGroupName());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("库位组名称" + storagGroup.getGroupName() + "已经存在！");
			} else {
				storagGroup.setUpdateUser("");
				saveEntity(storagGroup);
				resp.setRetcode("0");
				resp.setRetmsg("新增库位组信息成功！");
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(storagGroup.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("库位组名称" + storagGroup.getGroupName() + "已经存在！");
			} else {
				updateEntity(storagGroup);
				resp.setRetcode("0");
				resp.setRetmsg("修改库位组信息成功！");
			}
		}
		return resp;
	}

	/**
	 * 删除月台信息
	 */
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		WmsStoragGroup storagGroup = getEntity(id);
		deleteEntity(storagGroup);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功!");
		return resp;
	}

	/**
	 * 获取下拉库位组列表
	 */
	@Override
	public List<Map<String, String>> getGroupCombobox(String key, String whCode) {
		List<Map<String, String>> listMap = new ArrayList<>();
		if (StringUtil.isEmpty(key) || key.length() < minSearchLength) {
			return listMap;
		}
		key = "%" + key + "%";
		List<WmsStoragGroup> list = findEntityByHQL("from WmsStoragGroup where whCode = ? and groupName like ?",
				new Function<Query>() {
					@Override
					public void run(Query t) {
						t.setMaxResults(searchRows);
					}
				}, whCode, key);
		for (WmsStoragGroup wmsStoragGroup : list) {
			Map<String, String> map = new HashMap<>();
			map.put("groupid", wmsStoragGroup.getGroupName());
			map.put("whCode", wmsStoragGroup.getWhCode());
			map.put("valueStr", StringUtils.join(wmsStoragGroup.getGroupName(), " - ", wmsStoragGroup.getWhCode()));

			listMap.add(map);
		}
		return listMap;

	}

	@Override
	public WmsStoragGroup getGroupByName(String groupName, String whCode) {
		return (WmsStoragGroup) uniqueResult("from WmsStoragGroup where groupName = ? and whCode = ?", groupName,
				whCode);
	}

}
