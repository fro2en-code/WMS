package wms.userRelation.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserWarehouse;
import com.wms.warehouse.WmsWarehouse;

import its.base.service.BaseServiceImpl;
import wms.userRelation.service.UserWarehouseService;
import wms.warehouse.service.WarehouseService;

/**
 * 用户仓库关系实现
 */
@Service("userWarehouseService")
public class UserWarehouseServiceImpl extends BaseServiceImpl<UserWarehouse> implements UserWarehouseService {

	@Resource
	private WarehouseService warehouseService;

	/**
	 * 查询分页数据
	 */

	@Override
	public PageData<UserWarehouse> getPageData(int page, int rows, String loginname) {
		String hql = "From UserWarehouse Where userLoginname = ?";
		int startIndex = (page - 1) * rows;
		int totalRows = count("Select Count(*) " + hql, loginname);
		List<UserWarehouse> userWarehouses = findEntitySplitPage(hql, startIndex, rows, loginname);

		List<WmsWarehouse> ww = warehouseService.getAllWmsWarehouse();
		Map<String, String> nameMap = new HashMap<String, String>();
		for (WmsWarehouse w : ww) {
			nameMap.put(w.getWhCode(), w.getWhName());
		}

		for (UserWarehouse uw : userWarehouses) {
			uw.setWhName(nameMap.get(uw.getWhCode()));
		}

		return new PageData<UserWarehouse>(page, rows, totalRows, userWarehouses);
	}

	/**
	 * 根据登陆名称获取绑定的仓库
	 */
	@Override
	public List<UserWarehouse> getWarehouseMap(String loginname) {
		String hql = "From UserWarehouse Where userLoginname = ?";
		return findEntityByHQL(hql, loginname);
	}

	@Override
	public ResultResp save(UserWarehouse userWarehouse) {
		ResultResp resp = new ResultResp();
		String id = userWarehouse.getId();
		String check = "select count(*) from UserWarehouse where whCode = ? and userLoginname = ?";
		List<Object> params = new ArrayList<>();
		params.add(userWarehouse.getWhCode());
		params.add(userWarehouse.getUserLoginname());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("编号为" + userWarehouse.getWhCode() + "的仓库已经存在！");
			} else {
				saveEntity(userWarehouse);
				resp.setRetcode("0");
				resp.setRetmsg("绑定仓库成功！");
			}
		} else {// 更新
			check += "and id <> ?";
			params.add(userWarehouse.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("编号为" + userWarehouse.getWhCode() + "的仓库已经存在！");
			} else {
				saveEntity(userWarehouse);
				resp.setRetcode("0");
				resp.setRetmsg("绑定仓库成功！");
			}
		}
		return resp;
	}

	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		UserWarehouse uw = getEntity(id);
		deleteEntity(uw);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功！");
		return resp;
	}
	@Override
	public PageData<UserWarehouse> getUserPageData(int page, int rows,UserWarehouse userWarehouse){
		StringBuffer base_hql = new StringBuffer("From UserWarehouse where 1=1 ");
        List<Serializable> params = new ArrayList<>();
        if (!StringUtil.isEmpty(userWarehouse.getUserLoginname())) {
            base_hql.append(" and userLoginname like ? ");
            params.add("%"+userWarehouse.getUserLoginname()+"%");
        }
        if (!StringUtil.isEmpty(userWarehouse.getTruename())) {
            base_hql.append(" and truename like ? ");
            params.add("%"+userWarehouse.getTruename()+"%");
        }
        if (!StringUtil.isEmpty(userWarehouse.getWhCode())) {
            base_hql.append(" And whCode = ? ");
            params.add(userWarehouse.getWhCode());
        }
        return getPageDataByBaseHql(base_hql.toString(), " Order By userLoginname desc", page, rows, params);
}
}
