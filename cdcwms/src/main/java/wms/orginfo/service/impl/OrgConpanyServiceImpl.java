package wms.orginfo.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.utils.StringUtil;
import com.wms.orginfo.OrgConpany;
import com.wms.warehouse.WmsWarehouse;

import wms.warehouse.service.WarehouseService;

/**
 * 公司信息数据导入
 *
 */
@Service("orgConpanyService")
public class OrgConpanyServiceImpl extends CompanyServiceImpl {

	@Resource
	private WarehouseService warehouseService;

	public Serializable saveEntity(OrgConpany conpany) {
		if (StringUtil.isEmpty(conpany.getCompanyid())) {
			throw new RuntimeException("公司编码为空");
		}
		if (StringUtil.isEmpty(conpany.getType())) {
			throw new RuntimeException("公司编码为 " + conpany.getCompanyid() + "的公司类型为空");
		}
		if (StringUtil.isEmpty(conpany.getName())) {
			throw new RuntimeException("公司编码为 " + conpany.getCompanyid() + "的公司名字为空");
		}
		if (StringUtil.isEmpty(conpany.getWhCode())) {
			throw new RuntimeException("公司编码为 " + conpany.getCompanyid() + "的仓库代码为空");
		}
		WmsWarehouse warehouse = warehouseService.getWmsByCode(conpany.getWhCode());
		if (warehouse == null) {
			throw new RuntimeException("物料" + conpany.getCompanyid() + "的仓库代码不正确");
		}
		if (!(conpany.getType().equals("运行公司") || conpany.getType().equals("供应商") || conpany.getType().equals("承运商")
				|| conpany.getType().equals("业务中心"))) {
			throw new RuntimeException("公司编码为 " + conpany.getCompanyid() + "的数据,请填写正确的公司类型");
		}
		conpany.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
		String id = conpany.getId();
		String check = "select count(*) From OrgConpany Where companyid= ?";
		List<Object> params = new ArrayList<>();
		params.add(conpany.getCompanyid());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				throw new RuntimeException("公司编码 " + conpany.getCompanyid() + "已经存在");
			} else {
				super.saveEntity(conpany);
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(conpany.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				throw new RuntimeException("公司编码 " + conpany.getCompanyid() + "已经存在");
			} else {
				updateEntity(conpany);
			}
		}
		return null;

	}
}
