package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsStocktake;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsStocktakeService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsStocktakeService")
public class WmsStocktakeServiceImpl extends BaseServiceImpl<WmsStocktake> implements WmsStocktakeService {

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsStocktake> getPageData(int page, int rows, WmsStocktake wmsStocktake) {
		StringBuilder base_hql = new StringBuilder();
		base_hql.append("from WmsStocktake where 1 = 1");
		List<Serializable> params = new ArrayList<>();
		if (!StringUtil.isEmpty(wmsStocktake.getTakePlanCode())) {
			base_hql.append("and takePlanCode like ?");
			params.add("%" + wmsStocktake.getTakePlanCode() + "%");
		}
		if (!StringUtil.isEmpty(wmsStocktake.getStorageCode())) {
			base_hql.append("and storageCode like ?");
			params.add("%" + wmsStocktake.getStorageCode() + "%");
		}
		if (!StringUtil.isEmpty(wmsStocktake.getGcode())) {
			base_hql.append("and gcode like ?");
			params.add("%" + wmsStocktake.getGcode() + "%");
		}
		if (!StringUtil.isEmpty(wmsStocktake.getOraCode())) {
			base_hql.append("and oraCode like ?");
			params.add("%" + wmsStocktake.getOraCode() + "%");
		}
		if (!StringUtil.isEmpty(wmsStocktake.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsStocktake.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By takePlanCode desc", page, rows, params);

	}

	@Override
	public void update(WmsStocktake wmsStocktake) {
		updateEntity(wmsStocktake);
	}
}
