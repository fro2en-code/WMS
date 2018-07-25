package wms.warehouse.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsWarehouse;

import its.base.service.BaseServiceImpl;
import wms.warehouse.service.WarehouseService;

@Service("warehouseService")
public class WarehouseServiceImpl extends BaseServiceImpl<WmsWarehouse> implements WarehouseService {
	/**
	 * 查询分页
	 */
	@Override
	public PageData<WmsWarehouse> getPageData(int page, int rows, String name) {
		StringBuilder base_hql = new StringBuilder();
		base_hql.append("from WmsWarehouse");
		return getPageDataByBaseHql(base_hql.toString(), " Order By whCode desc", page, rows);
	}

	/**
	 * 保存 修改仓库
	 */
	@Override
	public ResultResp saveWms(WmsWarehouse ws) {
		ResultResp resp = new ResultResp();
		String id = ws.getId();
		String check = "select count(*) From WmsWarehouse Where whCode= ?";
		List<Object> params = new ArrayList<>();
		params.add(ws.getWhCode());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("仓库编号" + ws.getWhCode() + "已经存在！");
			} else {
				ws.setUpdateUser("");
				saveEntity(ws);
				resp.setRetcode("0");
				resp.setRetmsg("新增仓库成功！");
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(ws.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("仓库编号" + ws.getWhCode() + "已经存在！");
			} else {
				updateEntity(ws);
				resp.setRetcode("0");
				resp.setRetmsg("修改仓库成功！");
			}
		}
		return resp;
	}

	/**
	 * 删除仓库
	 */
	@Override
	public ResultResp delWmsById(String id) {
		ResultResp resp = new ResultResp();
		WmsWarehouse ws = getEntity(id);
		deleteEntity(ws);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功!");
		return resp;
	}

	/**
	 * 获取全部仓库
	 */
	@Override
	public List<WmsWarehouse> getAllWmsWarehouse() {
		return findEntityByHQL("From WmsWarehouse");
	}

	/**
	 * 获取下拉仓库列表
	 */
	@Override
	public List<Map<String, String>> getCombobox() {
		List<WmsWarehouse> warehouses = this.getAllWmsWarehouse();
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (WmsWarehouse wwh : warehouses) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", wwh.getId());
			map.put("whCode", wwh.getWhCode());
			map.put("whName", wwh.getWhName());
			dataList.add(map);
		}
		return dataList;
	}

	/**
	 * 根据仓库代码获取仓库
	 */
	@Override
	public WmsWarehouse getWmsByCode(String whCode) {
		return (WmsWarehouse) uniqueResult("From WmsWarehouse Where whCode=?", whCode);
	}

	@Override
	public Map<String, String> getAllWmsWarehouseMap() {
		List<WmsWarehouse> list = getAllWmsWarehouse();
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		for (WmsWarehouse wh : list) {
			wareHouseMap.put(wh.getWhCode(), wh.getWhName());
		}
		return wareHouseMap;
	}

}
