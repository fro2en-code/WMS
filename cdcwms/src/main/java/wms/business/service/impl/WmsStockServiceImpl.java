package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.warehouse.WmsStock;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsStockService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsStockService")
public class WmsStockServiceImpl extends BaseServiceImpl<WmsStock> implements WmsStockService {

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsStock> getPageData(int page, int rows, WmsStock wmsStock) {
		return getPageDataByBaseHql("From WmsStock where 1=1 ", null, page, rows, new ArrayList<Serializable>());
	}

	@Override
	public List<WmsStock> getWmsStock(WmsStock wmsStock) {
		StringBuilder hql = new StringBuilder(
				"from WmsStock where whCode = ? and gcode = ? and gtype = ? and supCode = ?");
		List<Serializable> params = new ArrayList<>();
		params.add(wmsStock.getWhCode());
		params.add(wmsStock.getGcode());
		params.add(wmsStock.getGtype());
		params.add(wmsStock.getSupCode());
		if (!StringUtils.isEmpty(wmsStock.getBatchCode())) {
			hql.append(" and batchCode=?");
			params.add(wmsStock.getBatchCode());
		}
		if (!StringUtils.isEmpty(wmsStock.getZoneCode())) {
			hql.append(" and zoneCode=?");
			params.add(wmsStock.getZoneCode());
		}
		if (!StringUtils.isEmpty(wmsStock.getStorageCode())) {
			hql.append(" and storageCode=?");
			params.add(wmsStock.getStorageCode());
		}
		hql.append(" order by batchCode,quantity desc");
		return findEntityByHQL(hql.toString(), params.toArray(new Serializable[params.size()]));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WmsStock> getWmsStockCount(String storageCode, String whCode) {
		String sql = "Select g_code as gcode ,g_name as gname,g_type as gtype,sup_code as supCode,sum(quantity+ifnull(pre_Pick_Num,0)) as quantity From Wms_Stock where storage_Code=? and Wh_code = ? group by g_code,g_name,g_type,sup_code having sum(quantity+ifnull(pre_Pick_Num,0)) > 0";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, storageCode);
		query.setParameter(1, whCode);
		query.addScalar("gcode", StandardBasicTypes.STRING);
		query.addScalar("gname", StandardBasicTypes.STRING);
		query.addScalar("gtype", StandardBasicTypes.STRING);
		query.addScalar("supCode", StandardBasicTypes.STRING);
		query.addScalar("quantity", StandardBasicTypes.INTEGER);
		query.setResultTransformer(Transformers.aliasToBean(WmsStock.class));
		return query.list();
	}

	@Override
	public int getWmsStockCount(WmsStock wmsStock, boolean lock) {
		StringBuilder hql = new StringBuilder();
		if (lock) {
			hql.append("select sum(ifnull(prePickNum,0)+ifnull(lockNum,0)+quantity)");
		} else {
			hql.append("select sum(ifnull(prePickNum,0)+quantity)");
		}
		hql.append(" from WmsStock where whCode = ? and gcode = ? and gtype = ? and supCode = ? ");
		List<Serializable> params = new ArrayList<>();
		params.add(wmsStock.getWhCode());
		params.add(wmsStock.getGcode());
		params.add(wmsStock.getGtype());
		params.add(wmsStock.getSupCode());
		if (StringUtils.isNotEmpty(wmsStock.getStorageCode())) {
			hql.append(" and storageCode = ? ");
			params.add(wmsStock.getStorageCode());
		}
		Number count = (Number) uniqueResult(hql.toString(), params.toArray(new Serializable[params.size()]));
		return null != count ? count.intValue() : 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WmsStock> getWmsStockObject(String storageCode, String whCode) {
		String sql = "SELECT DISTINCT g_code as gcode ,g_name as gname,g_type as gtype,sup_code as supCode FROM wms_stock WHERE Storage_code = ? and Wh_code = ?;";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, storageCode);
		query.setParameter(1, whCode);
		query.addScalar("gcode", StandardBasicTypes.STRING);
		query.addScalar("gname", StandardBasicTypes.STRING);
		query.addScalar("gtype", StandardBasicTypes.STRING);
		query.addScalar("supCode", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(WmsStock.class));
		return query.list();
	}
}
