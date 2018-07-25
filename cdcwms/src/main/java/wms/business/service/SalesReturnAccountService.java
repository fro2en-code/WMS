package wms.business.service;

import javax.servlet.http.HttpSession;

import com.plat.common.page.PageData;
import com.wms.business.SalesReturnAccount;

import its.base.service.BaseService;

/**
 * 退货台账
 * 
 * @author Administrator
 *
 */
public interface SalesReturnAccountService extends BaseService<SalesReturnAccount> {
	public PageData<SalesReturnAccount> getPageData(int rows, int page, HttpSession session,
			SalesReturnAccount salesReturnAccount);

	SalesReturnAccount getSalesReturnAccount(String gcode, String gtype, String oraCode, String whCode);

	public void save(SalesReturnAccount salesReturnAccount);

	public void update(SalesReturnAccount salesReturnAccount);
}
