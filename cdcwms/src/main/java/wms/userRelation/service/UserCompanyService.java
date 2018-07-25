package wms.userRelation.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.userRelation.UserCompany;

import its.base.service.BaseService;

public interface UserCompanyService extends BaseService<UserCompany> {
	PageData<UserCompany> getPageData(int page, int rows, UserCompany userCompany);

	List<UserCompany> getUserCompany(UserCompany userCompany);

	public ResultResp save(UserCompany userCompany);

	/**
	 * 根据用户名查询绑定的公司
	 */
	List<UserCompany> getCompanyByName(String UserName, String whCode);
}
