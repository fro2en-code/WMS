package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsDock;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsDockService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsDockService")
public class WmsDockServiceImpl extends BaseServiceImpl<WmsDock> implements WmsDockService {

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsDock> getPageData(int page, int rows, WmsDock wmsDock) {
		return getPageDataByBaseHql("From WmsDock where 1=1 ", null, page, rows, new ArrayList<Serializable>());
	}

	@Override
	public WmsDock getWmsDockByDockCode(String dockCode, String whCode) {
		return (WmsDock) uniqueResult("from WmsDock where dockCode =? and whCode = ?", dockCode, whCode);
	}

	@Override
	public WmsDock getWmsDockByTag(String tagId) {
		return (WmsDock) uniqueResult("from WmsDock where tagId =? ", tagId);
	}

	/**
	 * 获取全部月台
	 */
	@Override
	public List<WmsDock> getAllDock(String whCode) {
		return findEntityByHQL("from WmsDock where whCode=?", whCode);
	}

	/**
	 * 
	 * 查询分页
	 * 
	 */
	@Override
	public PageData<WmsDock> getPageData(int page, int rows, String whCode) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsDock where 1=1");
		if (!StringUtil.isEmpty(whCode)) {
			base_hql.append(" and whCode = ?");
			params.add(whCode);
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By dockCode desc", page, rows, params);
	}

	/**
	 * 
	 * 保存、修改月台
	 * 
	 */
	@Override
	public ResultResp save(WmsDock dock) {
		ResultResp resp = new ResultResp();
		String id = dock.getId();
		String check = "select count(*) From WmsDock Where dockCode = ? and whCode = ?";
		List<Object> params = new ArrayList<>();
		params.add(dock.getDockCode());
		params.add(dock.getWhCode());
		if (StringUtil.isEmpty(id)) {// 新增
			if (!StringUtil.isEmpty(dock.getTagId())) {
				String sql = "select count(*) From WmsDock Where tagId = ? and whCode = ?";
				if (count(sql, dock.getTagId(), dock.getWhCode()) > 0) {
					resp.setRetcode("-1");
					resp.setRetmsg("tag编码" + dock.getTagId() + "重复或者存在于其它仓库！");
					return resp;
				}
			}
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("月台编号" + dock.getDockCode() + "重复或者存在于其它仓库！");
			} else {
				dock.setUpdateUser("");
				saveEntity(dock);
				resp.setRetcode("0");
				resp.setRetmsg("新增月台成功！");
			}
		} else {// 更新
			if (!StringUtil.isEmpty(dock.getTagId())) {
				String sql = "select count(*) From WmsDock Where tagId = ? and whCode = ? and id <> ?";
				if (count(sql, dock.getTagId(), dock.getWhCode(), dock.getId()) > 0) {
					resp.setRetcode("-1");
					resp.setRetmsg("tag编码" + dock.getTagId() + "重复或者存在于其它仓库！");
					return resp;
				}
			}
			check += " and id <> ?";
			params.add(dock.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("月台编号" + dock.getDockCode() + "重复或者存在于其它仓库！");
			} else {
				updateEntity(dock);
				resp.setRetcode("0");
				resp.setRetmsg("修改月台成功！");
			}
		}
		return resp;
	}

	/**
	 * 
	 * 删除月台
	 * 
	 */
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		WmsDock dock = getEntity(id);
		deleteEntity(dock);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功!");
		return resp;
	}
}
