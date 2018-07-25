package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import wms.systemConfig.service.SystemConfigService;
import cn.rtzltech.user.utils.StringUtil;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.systemConfig.SystemConfig;

import its.base.service.BaseServiceImpl;

@Service("systemConfigService")
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig>
		implements SystemConfigService {
	/**
	 * 分页
	 */
	@Override
	public PageData<SystemConfig> getPageData(int page, int rows,
			SystemConfig systemConfig) {
		StringBuffer base_hql = new StringBuffer();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from SystemConfig where 1=1 ");
		if (!StringUtil.isEmpty(systemConfig.getSystemKey())) {
			base_hql.append(" And systemKey like ?");
			params.add("%" + systemConfig.getSystemKey() + "%");
		}
		if (!StringUtil.isEmpty(systemConfig.getWhCode())) {
			base_hql.append(" And whCode = ?");
			params.add(systemConfig.getWhCode());
		}

		return getPageDataByBaseHql(base_hql.toString(),
				" Order By systemKey desc", page, rows, params);
	}

	/**
	 * 增加修改
	 */

	@Override
	public ResultResp save(SystemConfig systemConfig) {
		ResultResp resp = new ResultResp();
		String id = systemConfig.getId();
		String check = "select count (*) from SystemConfig where systemKey = ? and whCode = ?";
		List<Object> params = new ArrayList<>();
		params.add(systemConfig.getSystemKey());
		params.add(systemConfig.getWhCode());
		if (StringUtil.isEmpty(id)) {
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("添加失败,标识已存在");
			} else {
				saveEntity(systemConfig);
				resp.setRetcode("0");
				resp.setRetmsg("添加成功");
			}
		} else {
			check += " and id <> ?";
            params.add(systemConfig.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("修改失败，标识已存在");
			} else {
				updateEntity(systemConfig);
				resp.setRetcode("0");
				resp.setRetmsg("修改成功");
			}

		}
		return resp;
	}

	/**
	 * 删除
	 */
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		try {
			SystemConfig wh = getEntity(id);
			deleteEntity(wh);
			resp.setRetcode("0");
			resp.setRetmsg("删除成功");
		} catch (Exception e) {
			resp.setRetcode("-1");
			resp.setRetmsg("删除失败！<br>" + e.getMessage());
		}
		return resp;
	}

	@Override
	public SystemConfig getSystemBysystemKey(String systemKey, String whCode) {
		return (SystemConfig) uniqueResult(
				"from SystemConfig where systemKey = ? and whCode = ?",
				systemKey, whCode);
	}

}
