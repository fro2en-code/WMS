package wms.warehouse.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.Function;
import com.plat.common.utils.StringUtil;
import com.wms.orginfo.OrgConpany;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsWarehouse;

import its.base.service.BaseServiceImpl;
import wms.business.service.TotalAccountService;
import wms.orginfo.service.CompanyService;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.WarehouseService;

/**
 * 物料实现
 *
 */
@Service("goodsService")
public class GoodsServiceImpl extends BaseServiceImpl<WmsGoods> implements GoodsService {
	@Resource
	private CompanyService companyService;

	@Resource
	private TotalAccountService totalAccountService;
	@Resource
	private WarehouseService warehouseService;

	/**
	 * 删除物料
	 */
	@CacheEvict(value = "service.goodsService.*", allEntries = true)
	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		WmsGoods goods = getEntity(id);
		deleteEntity(goods);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功!");
		return resp;
	}

	/**
	 * 根据物料编号查询物料信息
	 */
	@Cacheable(value = "service.goodsService.getAbcByGcode")
	@Override
	public String getAbcByGcode(String gcode, String whCode) {
		WmsGoods wmsGoods = getWmsGoodsByGcode(gcode, whCode);
		if (null != wmsGoods) {
			return wmsGoods.getAbcType();
		}
		return null;
	}

	@Cacheable(value = "service.goodsService.getGoodsInfo")
	@Override
	public WmsGoods getGoodsInfo(String gcode, String gtype, String oraCode, String whCode) {
		try {
			return (WmsGoods) uniqueResult("from WmsGoods where gcode = ?  and gtype = ? and oraCode=? and whCode=?",
					gcode, gtype, oraCode, whCode);
		} catch (Exception e) {
			throw new RuntimeException(gcode + " 物料基础资料有误", e);
		}
	}

	@Cacheable(value = "service.goodsService.getNameByGcode")
	@Override
	public String getNameByGcode(String gcode, String whCode) {
		WmsGoods wmsGoods = getWmsGoodsByGcode(gcode, whCode);
		if (null != wmsGoods) {
			return wmsGoods.getGname();
		}
		return null;
	}

	/**
	 * 分页查询
	 */
	@Override
	public PageData<WmsGoods> getPageData(int page, int rows, WmsGoods goods) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsGoods where 1=1");
		if (!StringUtil.isEmpty(goods.getGcode())) {
			base_hql.append(" and gcode like ?");
			params.add("%" + goods.getGcode() + "%");
		}
		if (!StringUtil.isEmpty(goods.getOraCode())) {
			base_hql.append(" and oraCode like ?");
			params.add("%" + goods.getOraCode() + "%");
		}
		if (!StringUtil.isEmpty(goods.getStoragezoneId())) {
			base_hql.append(" and storagezoneId like ?");
			params.add("%" + goods.getStoragezoneId() + "%");
		}
		if (!StringUtil.isEmpty(goods.getWhCode())) {
			base_hql.append(" and whCode = ?");
			params.add(goods.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By gcode desc", page, rows, params);
	}

	/**
	 * 获取 WmsGoods ,如果有多个,则取第一个
	 */
	private WmsGoods getWmsGoodsByGcode(String gcode, String whCode) {
		Query query = getSession().createQuery("from WmsGoods where gcode =? And whCode = ?");
		query.setString(0, gcode);
		query.setString(1, whCode);
		query.setMaxResults(1);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if (list.isEmpty()) {
			return null;
		} else {
			return (WmsGoods) list.get(0);
		}
	}

	@Cacheable(value = "service.goodsService.getWmsGoodsByKey")
	@Override
	public List<Map<String, String>> getWmsGoodsByKey(String key, String whCode) {
		List<Map<String, String>> listMap = new ArrayList<>();
		if (StringUtil.isEmpty(key) || key.length() < minSearchLength) {
			return listMap;
		}
		key = "%" + key + "%";
		List<WmsGoods> list = findEntityByHQL(
				"From WmsGoods Where whCode=? and (gcode like ? or gname like ? or oraCode like ? or oraName like ?)",
				new Function<Query>() {
					@Override
					public void run(Query t) {
						t.setMaxResults(searchRows);
					}
				}, whCode, key, key, key, key);
		for (WmsGoods wmsGoods : list) {
			Map<String, String> map = new HashMap<>();
			map.put("id", wmsGoods.getId());
			map.put("gcode", wmsGoods.getGcode());
			map.put("gname", wmsGoods.getGname());
			map.put("oraCode", wmsGoods.getOraCode());
			map.put("oraName", wmsGoods.getOraName());
			map.put("gtype", wmsGoods.getGtype());
			map.put("valueStr",
					StringUtils.join(wmsGoods.getOraCode(), " - ", wmsGoods.getGcode(), " - ", wmsGoods.getGtype()));
			listMap.add(map);
		}
		return listMap;
	}

	@Cacheable(value = "service.goodsService.getWmsGoodsInfoByKey")
	@Override
	public List<Map<String, String>> getWmsGoodsInfoByKey(String key, String whCode) {
		List<Map<String, String>> listMap = new ArrayList<>();
		if (StringUtil.isEmpty(key) || key.length() < minSearchLength) {
			return listMap;
		}
		key = "%" + key + "%";
		List<WmsGoods> list = findEntityByHQL(
				"select distinct wmsGoods.oraCode as oraCode,wmsGoods.oraName as oraName,wmsGoods.gcode as gcode,wmsGoods.gname as gname,wmsGoods.units as units From WmsGoods as wmsGoods Where whCode=? and( oraCode like ? or oraName like ? or gcode like ? or gname like ?)",
				new Function<Query>() {
					@Override
					public void run(Query t) {
						t.setResultTransformer(Transformers.aliasToBean(WmsGoods.class));
						t.setMaxResults(searchRows);
					}
				}, whCode, key, key, key, key);
		for (WmsGoods wmsGoods : list) {
			Map<String, String> map = new HashMap<>();
			map.put("oraCode", wmsGoods.getOraCode());
			map.put("oraName", wmsGoods.getOraName());
			map.put("gcode", wmsGoods.getGcode());
			map.put("gname", wmsGoods.getGname());
			map.put("units", wmsGoods.getUnits());
			map.put("valueStr", StringUtils.join(wmsGoods.getOraCode(), " - ", wmsGoods.getGcode()));
			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * 保存、修改物料
	 */
	@CacheEvict(value = "service.goodsService.*", allEntries = true)
	@Override
	public ResultResp save(WmsGoods goods) {
		ResultResp resp = new ResultResp();
		saveEntity(goods);
		resp.setRetcode("0");
		resp.setRetmsg("完成");
		return resp;
	}

	@CacheEvict(value = "service.goodsService.*", allEntries = true)
	@Override
	public Serializable saveEntity(WmsGoods goods) {
		if (StringUtils.isEmpty(goods.getGcode())) {
			throw new RuntimeException("物料编码为空");
		}
		if (StringUtils.isEmpty(goods.getWhCode())) {
			throw new RuntimeException("物料" + goods.getWhCode() + "仓库代码为空");
		}
		if (StringUtils.isEmpty(goods.getGname())) {
			throw new RuntimeException("物料" + goods.getGcode() + "的物料名字为空");
		}
		if (goods.getUnits() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "的计量单位为空");
		}
		if (StringUtils.isEmpty(goods.getOraCode())) {
			throw new RuntimeException("物料" + goods.getGcode() + "的供应商编码为空");
		}
		if (StringUtils.isEmpty(goods.getOraName())) {
			throw new RuntimeException("物料" + goods.getGcode() + "的供应商名字为空");
		}
		if (StringUtils.isEmpty(goods.getAbcType())) {
			throw new RuntimeException("物料" + goods.getGcode() + "ABC类型为空");
		}
		if (goods.getStorageType() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "存储类型为空");
		}
		if (goods.getBoxType() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "存储包装类型为空");
		}
		if (goods.getBatchType() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "批次管理为空");
		}
		if (goods.getStoragezoneType() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "指定库位/库区动态为空");
		}
		if (StringUtils.isEmpty(goods.getStoragezoneId())) {
			throw new RuntimeException("物料" + goods.getGcode() + "库位/库区编号为空");
		}
		if (goods.getMaxNum() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "最大的库存数为空");
		}
		if (StringUtils.isEmpty(goods.getGtype())) {
			throw new RuntimeException("物料" + goods.getGcode() + "零件用途为空");
		}
		if (goods.getStorageMaxNum() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "库位最大库存数为空");
		}
		if (goods.getWarningMaxNum() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "的最大预警库存数量为空");
		}
		if (goods.getWarningMinNum() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "的最小预警库存数量为空");
		}
		if (goods.getForkliftNum() == null) {// 当叉车数量为空时,默认为0
			goods.setForkliftNum(0);
		}
		if (goods.getSkipPutaway() == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "搬运是否跳过上架为空");
		}
		WmsWarehouse warehouse = warehouseService.getWmsByCode(goods.getWhCode());
		if (warehouse == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "的仓库代码不正确");
		}
		OrgConpany orgConpany = companyService.getConpanyByConCode(goods.getOraCode());
		if (orgConpany == null) {
			throw new RuntimeException("物料" + goods.getGcode() + "的供应商编码不正确");
		}
		String storage = goods.getStoragezoneId();
		if (storage.endsWith(",")) {// 如果最后一个字符是逗号则去掉逗号
			storage = storage.substring(0, storage.length() - 1);
		}
		goods.setStoragezoneId(storage);
		// 判断当前仓库是否存在此供应商
		String Conpany = "select count(*) from OrgConpany where whCode = ? and companyid = ?";
		if (count(Conpany, goods.getWhCode(), goods.getOraCode()) == 0) {
			throw new RuntimeException("物料" + goods.getGcode() + "中的供应商在相应的仓库中不存在!");
		}
		if (goods.getStoragezoneType() == 1) {// 指定库位
			String[] arr = storage.split(",");// 根据“,”区分
			for (int i = 0; i < arr.length; i++) {
				String storageCode = "select count(*) From WmsStorag where storageCode = ? and whCode= ?";
				if (count(storageCode, arr[i], goods.getWhCode()) > 0) {
					String id = goods.getId();
					String check = "select count(*) From WmsGoods where gcode = ? and gtype = ? and oraCode = ? and whCode= ?";
					List<Object> params = new ArrayList<>();
					params.add(goods.getGcode());
					params.add(goods.getGtype());
					params.add(goods.getOraCode());
					params.add(goods.getWhCode());
					if (StringUtil.isEmpty(id)) {// 新增
						if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
							throw new RuntimeException("物料" + goods.getGcode() + "已存在");
						} else {
							// 保存物料
							super.saveEntity(goods);
						}
					} else {// 更新
						check += " and id <> ?";
						params.add(goods.getId());
						if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
							throw new RuntimeException("物料" + goods.getGcode() + "已存在");
						} else {
							updateEntity(goods);
						}
					}
				} else {
					throw new RuntimeException("所选择库位编号" + arr[i] + "不存在！");
				}
			}
		} else if (goods.getStoragezoneType() == 0) {// 动态库区
			String storageCode = "select count(*) From WmsZone where zoneCode = ? and whCode = ?";
			if (count(storageCode, storage, goods.getWhCode()) > 0) {
				String id = goods.getId();
				String check = "select count(*) From WmsGoods where gcode = ?  and gtype = ? and oraCode=? and whCode=?";
				List<Object> params = new ArrayList<>();
				params.add(goods.getGcode());
				params.add(goods.getGtype());
				params.add(goods.getOraCode());
				params.add(goods.getWhCode());
				if (StringUtil.isEmpty(id)) {// 新增
					if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
						throw new RuntimeException("物料" + goods.getGcode() + "已存在");
					} else {
						// 保存物料
						super.saveEntity(goods);
					}
				} else {// 更新
					check += " and id <> ?";
					params.add(goods.getId());
					if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
						throw new RuntimeException("物料" + goods.getGcode() + "已存在");
					} else {
						updateEntity(goods);
					}
				}
			} else {
				throw new RuntimeException("所选择库区编号" + storage + "不存在！");
			}

		} else {
			throw new RuntimeException("请选择指定库位/库区动态");
		}
		return null;
	}
}
