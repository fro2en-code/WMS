package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsDeliverDock;
import its.base.service.BaseServiceImpl;
import wms.business.service.WmsDeliverDockService;

/**
 * 送货到达道口信息
 * 
 * @author wangzz
 *
 */
@Service("wmsDeliverDockService")
public class WmsDeliverDockServiceImpl extends BaseServiceImpl<WmsDeliverDock> implements WmsDeliverDockService {

	/**
	 * 查询分页
	 */
	@Override
	public PageData<WmsDeliverDock> getPageData(int page, int rows, WmsDeliverDock wmsDeliverDock) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsDeliverDock where 1=1");
		if (!StringUtil.isEmpty(wmsDeliverDock.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsDeliverDock.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By dockCode desc", page, rows, params);

	}

	/**
	 * 获取全部送货到达道口信息
	 */
	@Override
	public List<WmsDeliverDock> getAllDock(String whCode) {
		return findEntityByHQL("from WmsDeliverDock where whCode = ?", whCode);
	}

	/**
	 * 保存、修改送货到达道口信息
	 */
	@Override
	public ResultResp save(WmsDeliverDock dock) {
		ResultResp resp = new ResultResp();
		String id = dock.getId();
		String check = "select count(*) From WmsDeliverDock Where dockCode= ?";
		List<Object> params = new ArrayList<>();
		params.add(dock.getDockCode());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("送货到达道口信息编号" + dock.getDockCode() + "已经存在！");
			} else {
				dock.setUpdateUser("");
				saveEntity(dock);
				resp.setRetcode("0");
				resp.setRetmsg("新增送货到达道口信息成功！");
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(dock.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("送货到达道口信息编号" + dock.getDockCode() + "已经存在！");
			} else {
				updateEntity(dock);
				resp.setRetcode("0");
				resp.setRetmsg("修改送货到达道口信息成功！");
			}
		}
		return resp;
	}

	/**
	 * 删除送货到达道口信息
	 */
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		WmsDeliverDock dock = getEntity(id);
		deleteEntity(dock);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功!");
		return resp;
	}

	/**
	 * 根据目的地代码查询gtype
	 */
	@Override
	public WmsDeliverDock getType(String deliveryRec) {
		return (WmsDeliverDock) uniqueResult("from WmsDeliverDock where dockCode=?", deliveryRec);
	}

	/**
	 * 查询道口信息
	 */
	@Override
	public List<Map<String, String>> getDockCombobox(String whCode) {
		List<WmsDeliverDock> wmsDeliverDock = this.getAllDock(whCode);
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (WmsDeliverDock dock : wmsDeliverDock) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("deliveryRec", dock.getDockCode());
			dataList.add(map);
		}
		return dataList;
	}
}
