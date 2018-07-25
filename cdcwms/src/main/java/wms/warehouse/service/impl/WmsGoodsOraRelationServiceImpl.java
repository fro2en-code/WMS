package wms.warehouse.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import its.base.service.BaseServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import wms.warehouse.service.WmsGoodsOraRelationService;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsGoodsOraRelation;

@Service("wmsGoodsOraRelationService")
public class WmsGoodsOraRelationServiceImpl extends
		BaseServiceImpl<WmsGoodsOraRelation> implements
		WmsGoodsOraRelationService {

	/**
	 * 分页查询
	 */
	@Override
	public PageData<WmsGoodsOraRelation> getPageData(int page, int rows,
			WmsGoodsOraRelation wmsGoodsOraRelation) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsGoodsOraRelation where 1=1");
		if (!StringUtil.isEmpty(wmsGoodsOraRelation.getGcode())) {
			base_hql.append(" and gcode like ?");
			params.add("%" + wmsGoodsOraRelation.getGcode() + "%");
		}
		if (!StringUtil.isEmpty(wmsGoodsOraRelation.getOraCode())) {
			base_hql.append(" and oraCode like ?");
			params.add("%" + wmsGoodsOraRelation.getOraCode() + "%");
		}
		if (!StringUtil.isEmpty(wmsGoodsOraRelation.getWhCode())) {
			base_hql.append(" and whCode = ?");
			params.add(wmsGoodsOraRelation.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(),
				" Order By gcode desc", page, rows, params);
	}

	/**
	 * 增加修改
	 */
	@CacheEvict(value = "service.wmsGoodsOraRelationService.*", allEntries = true)
	@Override
	public ResultResp save(WmsGoodsOraRelation wmsGoodsOraRelation) {
		ResultResp resp = new ResultResp();
		saveEntity(wmsGoodsOraRelation);
		resp.setRetcode("0");
		resp.setRetmsg("完成");
		return resp;
	}

	@CacheEvict(value = "service.wmsGoodsOraRelationService.*", allEntries = true)
	@Override
	public Serializable saveEntity(WmsGoodsOraRelation wmsGoodsOraRelation) {
		if (StringUtils.isEmpty(wmsGoodsOraRelation.getGcode())) {
			throw new RuntimeException("零件编码为空");
		}
		if (StringUtils.isEmpty(wmsGoodsOraRelation.getGname())) {
			throw new RuntimeException("零件" + wmsGoodsOraRelation.getGcode()
					+ "的零件名字为空");
		}
		if (StringUtils.isEmpty(wmsGoodsOraRelation.getOraCode())) {
			throw new RuntimeException("零件" + wmsGoodsOraRelation.getGcode()
					+ "的供应商编码为空");
		}
		if (StringUtils.isEmpty(wmsGoodsOraRelation.getOraName())) {
			throw new RuntimeException("零件" + wmsGoodsOraRelation.getGcode()
					+ "的供应商名字为空");
		}
		if (wmsGoodsOraRelation.getWarningMaxNum() == null) {
			throw new RuntimeException("零件" + wmsGoodsOraRelation.getGcode()
					+ "的最大预警库存数量为空");
		}
		if (wmsGoodsOraRelation.getWarningMinNum() == null) {
			throw new RuntimeException("零件" + wmsGoodsOraRelation.getGcode()
					+ "的最小预警库存数量为空");
		}
		if (wmsGoodsOraRelation.getStorageType() == null) {
			throw new RuntimeException("物料" + wmsGoodsOraRelation.getGcode() + "存储类型为空");
		}
		if (wmsGoodsOraRelation.getBoxType() == null) {
			throw new RuntimeException("物料" + wmsGoodsOraRelation.getGcode() + "存储包装类型为空");
		}
		String id = wmsGoodsOraRelation.getId();
		String check = "select count(*) From WmsGoodsOraRelation where gcode = ? and gtype = ? and oraCode = ? and whCode= ?";
		List<Object> params = new ArrayList<>();
		params.add(wmsGoodsOraRelation.getGcode());
		params.add(wmsGoodsOraRelation.getGtype());
		params.add(wmsGoodsOraRelation.getOraCode());
		params.add(wmsGoodsOraRelation.getWhCode());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				throw new RuntimeException("物料"
						+ wmsGoodsOraRelation.getGcode() + "已存在");
			} else {
				// 保存物料
				super.saveEntity(wmsGoodsOraRelation);
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(wmsGoodsOraRelation.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				throw new RuntimeException("物料"
						+ wmsGoodsOraRelation.getGcode() + "已存在");
			} else {
				updateEntity(wmsGoodsOraRelation);
			}
		}
		return null;
	}

	/**
	 * 删除物料供应商关系
	 */
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		WmsGoodsOraRelation wmsGoodsOraRelation = getEntity(id);
		deleteEntity(wmsGoodsOraRelation);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功!");
		return resp;
	}
}
