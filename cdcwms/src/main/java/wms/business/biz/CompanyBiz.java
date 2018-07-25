package wms.business.biz;

import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.orginfo.OrgConpany;

public interface CompanyBiz {
	/**
	 * 根据公司类型查询下拉公司
	 */
	List<Map<String, String>> getComboboxByConCode(String name, String whCode);

	/**
	 * 根据公司代码获取公司数据
	 */
	OrgConpany getConpanyByConCode(String conCode);

	/**
	 * 公司分页查询
	 */
	PageData<OrgConpany> getPageData(int page, int rows, OrgConpany conpany);

	/**
	 * 保存、修改公司
	 */
	ResultResp save(OrgConpany conpany);

	/**
	 * 删除公司
	 */
	ResultResp del(String id);
}
