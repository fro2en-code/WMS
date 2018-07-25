package wms.orginfo.service;

import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.orginfo.OrgConpany;

import its.base.service.BaseService;

/**
 * 公司管理接口
 * 
 * @author wlx
 */
public interface CompanyService extends BaseService<OrgConpany> {

	/**
	 * 公司分页查询
	 */
	PageData<OrgConpany> getPageData(int page, int rows, OrgConpany conpany);

	/**
	 * 查询全部公司数据
	 */
	List<OrgConpany> getAllConpany();

	/**
	 * 根据公司代码获取公司数据
	 */
	OrgConpany getConpanyByConCode(String conCode);

	/**
	 * 保存、修改公司
	 */
	ResultResp save(OrgConpany conpany);

	/**
	 * 删除公司
	 */
	ResultResp del(String id);

	/**
	 * 根据公司类型查询下拉公司
	 */
	List<Map<String, String>> getComboboxByConCode(String name, String whCode);

}
