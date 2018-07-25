package wms.business.service;

import javax.servlet.http.HttpSession;

import com.plat.common.page.PageData;
import com.wms.business.SalesReturnAccountList;

import its.base.service.BaseService;

public interface SalesReturnAccountListService extends BaseService<SalesReturnAccountList> {
	public PageData<SalesReturnAccountList> getPageData(int rows, int page, HttpSession session,
			SalesReturnAccountList salesReturnAccountList);

	public void save(SalesReturnAccountList salesReturnAccountList);
}
